package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.common.TimetableType;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.CourseRepository;
import com.pw.timetablegenerator.backend.jpa.TimetableRepository;
import com.pw.timetablegenerator.backend.service.TimetableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Integer preferenceForceSum = 0;

    @Override
    public List<Timetable> findTimetables(User user, String value) {
        List<Timetable> timetables = timetableRepository.findByOwner(user);

        if(StringUtils.isNotBlank(value)){
            List<Timetable> filteredTimetables = filterTimetablesByName(timetables, value);
            filteredTimetables = filterTimetablesByStatus(timetables, filteredTimetables, value);
            timetables = filteredTimetables;
        }

        return timetables;
    }

    private List<Timetable> filterTimetablesByStatus(List<Timetable> timetables,
                                                     List<Timetable> filteredTimetables, String value) {
        if(filteredTimetables.isEmpty()){
            filteredTimetables = timetables.stream()
                    .filter(timetable -> StringUtils.containsIgnoreCase(timetable.getTimetableType().toString(), value))
                    .collect(Collectors.toList());
        }
        return filteredTimetables;
    }

    private List<Timetable> filterTimetablesByName(List<Timetable> timetables, String value) {
        return timetables.stream()
                .filter(timetable -> StringUtils.containsIgnoreCase(timetable.getName(), value))
                .collect(Collectors.toList());
    }

    @Override
    public Timetable findByTimetableId(long id) {
        return timetableRepository.findByTimetableId(id);
    }

    @Override
    public void delete(Timetable timetable) {
        timetableRepository.delete(timetable);
    }

    @Override
    public void save(Timetable timetable) {
        timetableRepository.save(timetable);
    }

    @Override
    public void generateTimetable(Timetable timetable, EnrollmentGroup enrollmentGroup,
                                  List<PreferenceDts> preferences) {
        List<Course> coursesToEnroll = new ArrayList<>();
        timetable.setCourses(coursesToEnroll);

        //2
        Map<Course, Integer> preferenceForces =  assignEachCoursePreferenceForce(enrollmentGroup, preferences);
        //1
        enrollCoursesWithOneTerm(timetable, coursesToEnroll, enrollmentGroup);
        for(int i = 0; i < 20; i++){
            //3
            enrollCoursesByPreferenceForce(coursesToEnroll, preferenceForces);
            //4
            if(checkIfStudentIsEnrolledForAllClasses(timetable, enrollmentGroup)){
                break;
            } else {
                tryToEnrollFailedEnrollCourses(coursesToEnroll, preferenceForces);
            }
        }

        fillTimetableInformation(timetable, preferenceForces, enrollmentGroup);
        save(timetable);
    }

    private void tryToEnrollFailedEnrollCourses(List<Course> coursesToEnroll, Map<Course, Integer> preferenceForces) {
        final List<Class> enrolledClasses = coursesToEnroll.stream()
                .map(Course::getClassOwner)
                .collect(Collectors.toList());

        preferenceForces.keySet().stream()
                .forEach(course -> {
                    if(!enrolledClasses.contains(course.getClassOwner())){
                        findBlockingCourse(course, preferenceForces);
                    }
                });
    }

    private void findBlockingCourse(Course course, Map<Course, Integer> preferenceForces) {
        for(Course c : preferenceForces.keySet()){
            if(!checkIfCourseNotBlockingOthers(c, course)){
                preferenceForces.replace(course, preferenceForces.get(c) + 1);
                return;
            }
        }
    }

    private void fillTimetableInformation(Timetable timetable, Map<Course, Integer> preferenceForces,
                                          EnrollmentGroup enrollmentGroup) {
        final double timetableQuality = calculateTimetableQuality(timetable, preferenceForces);
        timetable.setQuality(timetableQuality);
        timetable.setTimetableType(calculateTimetableType(timetableQuality, timetable, enrollmentGroup));
        timetable.setGenerationDate(LocalDate.now());
    }

    private TimetableType calculateTimetableType(double timetableQuality, Timetable timetable,
                                                 EnrollmentGroup enrollmentGroup) {

        if(!checkIfStudentIsEnrolledForAllClasses(timetable, enrollmentGroup)){
            return TimetableType.INVALID;
        }
        if(timetableQuality < 40.){
            return TimetableType.BAD;
        } else if(timetableQuality < 80.){
            return TimetableType.GOOD;
        } else {
            return TimetableType.EXCELLENT;
        }
    }

    private boolean checkIfStudentIsEnrolledForAllClasses(Timetable timetable, EnrollmentGroup enrollmentGroup) {
        return enrollmentGroup.getClasses().containsAll(timetable.getCourses().stream()
                .map(Course::getClassOwner)
                .collect(Collectors.toList()));
    }

    private double calculateTimetableQuality(Timetable timetable, Map<Course, Integer> preferenceForces) {
        final Integer maxQuality = calculateMaxQuality(preferenceForces);

        Integer actualQuality = 0;
        for(Course course : timetable.getCourses()){
            actualQuality += preferenceForces.get(course);
        }

        return actualQuality.doubleValue() / maxQuality.doubleValue() * 100.;
    }

    private Integer calculateMaxQuality(Map<Course, Integer> preferenceForces) {
        List<Course> alreadyCounted = new ArrayList<>();
        preferenceForces.keySet().forEach(course -> {
                    if(alreadyCounted.stream()
                            .map(Course::getClassOwner)
                            .noneMatch(c -> Objects.equals(c, course.getClassOwner()))){
                        alreadyCounted.add(course);
                    }
                });

        Integer maxQuality = 0;
        for(Course course : alreadyCounted){
            maxQuality += preferenceForces.get(course);
        }

        return maxQuality;
    }

    private void enrollCoursesByPreferenceForce(List<Course> coursesToEnroll, Map<Course, Integer> preferenceForces) {
        preferenceForces.keySet().stream()
                .forEach(course -> {
                    //3.1
                    if(checkIfCourseIsAlreadyEnrolled(course, coursesToEnroll)){
                        return;
                    }
                    //3.2
                    if(checkIfTermIsFree(course, coursesToEnroll)){
                        coursesToEnroll.add(course);
                    }
                });
    }

    private boolean checkIfTermIsFree(Course course, List<Course> coursesToEnroll) {
        return coursesToEnroll.stream().allMatch(c -> checkIfCourseNotBlockingOthers(course, c));
    }

    private boolean checkIfCourseNotBlockingOthers(Course courseToEnroll, Course courseAlreadyEnrolled) {
        if(courseToEnroll.getCourseDay() == courseAlreadyEnrolled.getCourseDay() &&
                (courseToEnroll.getParityOfTheWeek() == courseAlreadyEnrolled.getParityOfTheWeek() ||
                        courseToEnroll.getParityOfTheWeek() == ParityOfTheWeek.WEEKLY ||
                        courseAlreadyEnrolled.getParityOfTheWeek() == ParityOfTheWeek.WEEKLY)){
            return courseToEnroll.getCourseStartTime().isAfter(courseAlreadyEnrolled.getCourseEndTime()) ||
                    courseToEnroll.getCourseStartTime().equals(courseAlreadyEnrolled.getCourseEndTime()) ||
                    courseToEnroll.getCourseEndTime().isBefore(courseAlreadyEnrolled.getCourseStartTime()) ||
                    courseToEnroll.getCourseEndTime().equals(courseAlreadyEnrolled.getCourseStartTime());
        } else {
            return true;
        }
    }

    private boolean checkIfCourseIsAlreadyEnrolled(Course course, List<Course> coursesToEnroll) {
        return coursesToEnroll.stream()
                .anyMatch(c -> Objects.equals(c.getClassOwner(), course.getClassOwner()));
    }

    private Map<Course, Integer> assignEachCoursePreferenceForce(EnrollmentGroup enrollmentGroup, List<PreferenceDts> preferences) {
        Map<Course, Integer> preferenceForces = new HashMap<>();

        enrollmentGroup.getClasses().stream()
                .flatMap(c -> c.getCourses().stream())
                .forEach(course -> {
                    preferenceForceSum = 0;
                    preferences.forEach(preference -> sumPreferenceForces(course, preference));
                    preferenceForces.put(course, preferenceForceSum);
                });

        return preferenceForces.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void sumPreferenceForces(Course course, PreferenceDts preference) {
        final Integer preferenceForce = preference.calculatePreferenceForce(course);
        preferenceForceSum += preferenceForce;
    }

    private void enrollCoursesWithOneTerm(Timetable timetable, List<Course> coursesToEnroll,
                                          EnrollmentGroup enrollmentGroup) {
        enrollmentGroup.getClasses().stream()
                .filter(c -> c.getCourses().size() == 1)
                .flatMap(c -> c.getCourses().stream())
                .forEach(c -> {
                    c.getTimetables().add(timetable);
                    coursesToEnroll.add(c);
                    courseRepository.save(c);
                });
    }
}
