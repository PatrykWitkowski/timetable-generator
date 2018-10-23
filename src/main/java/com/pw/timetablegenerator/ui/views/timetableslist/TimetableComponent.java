package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.common.ClassType;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Tag("timetable-component")
@HtmlImport("frontend://src/views/timetableslist/timetable.html")
public class TimetableComponent extends PolymerTemplate<TimetableComponent.TimetablesModel> {

    public interface TimetablesModel extends TemplateModel {
        void setCourses(String courses);
        void setCurrentLocale(String currentLocale);
        void setCategories(String categories);
    }

    public void setTimetable(Timetable timetable){
        getModel().setCourses(formatCoursesToString(timetable));
        getModel().setCurrentLocale(UI.getCurrent().getLocale().getLanguage());
        getModel().setCategories(createCategories());
    }

    private String createCategories(){
        String result = "[";
        result += createCategory(getTranslation(Class_.TYPE_LECTURE), "#59B373") + ",";
        result += createCategory(getTranslation(Class_.TYPE_EXERCISE), "#F4AA4C") + ",";
        result += createCategory(getTranslation(Class_.TYPE_LABORATORY), "#549EC5") + ",";
        result += createCategory(getTranslation(Class_.TYPE_PROJECT), "#5ACAF1") + ",";
        result += createCategory(getTranslation(Class_.TYPE_SEMINAR), "#5BC1BC");
        result += "]";
        return result;
    }

    private String createCategory(String categoryName, String color){
        return String.format("{ \"label\": \"%s\", \"color\": \"%s\"}", categoryName, color);
    }

    private String formatCoursesToString(Timetable timetable) {
        List<String> events = Lists.newArrayList();
        for(DayOfWeek day : DayOfWeek.values()){
            LocalDate semesterStartDate = timetable.getSemesterStartDate();
            final List<Course> coursesForSpecificDay = findAllCoursesForSpecificDay(timetable, day);
            LocalDate firstDate = findFirstSpecificDayInSemester(day, semesterStartDate);
            addCourseForAllSpecificDayToSemestersEnd(timetable, events, coursesForSpecificDay, firstDate);
        }

        return prepareResultEventsString(events);
    }

    private String prepareResultEventsString(List<String> events) {
        String result = "[";
        for(String event : events){
            result += event;
            result +=",";
        }
        result = StringUtils.removeEnd(result, ",");
        result += "]";

        return result;
    }

    private void addCourseForAllSpecificDayToSemestersEnd(Timetable timetable, List<String> events,
                                                          List<Course> coursesForSpecificDay, LocalDate firstDate) {
        while(firstDate.isBefore(timetable.getSemesterEndDate())){
            addEvents(events, coursesForSpecificDay, firstDate);
            firstDate = firstDate.plusWeeks(1);
        }
    }

    private LocalDate findFirstSpecificDayInSemester(DayOfWeek day, LocalDate semesterStartDate) {
        while(semesterStartDate.getDayOfWeek() != day){
            semesterStartDate = semesterStartDate.plusDays(1);
        }
        return semesterStartDate;
    }

    private List<Course> findAllCoursesForSpecificDay(Timetable timetable, DayOfWeek day) {
        return timetable.getCourses().stream()
                        .filter(c -> c.getCourseDay() == day)
                        .collect(Collectors.toList());
    }

    private void addEvents(List<String> events, List<Course> coursesForSpecificDay, final LocalDate firstDate) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        events.addAll(coursesForSpecificDay.stream()
                .filter(c -> c.getParityOfTheWeek() == ParityOfTheWeek.WEEKLY
                        || actualDateIsInEvenWeek(firstDate, weekOfYear, c)
                        || actualDateIsInOddWeek(firstDate, weekOfYear, c))
                .map(c -> String.format("{%s, %s, %s, %s}",
                        addTitle(c),
                        addStartDateTime(firstDate, c.getCourseStartTime()),
                        addEndDateTime(firstDate, c.getCourseEndTime()),
                        addCategory(getTranslation(c.getClassOwner().getClassType().getProperty()))))
                .collect(Collectors.toList()));
    }

    private boolean actualDateIsInOddWeek(LocalDate firstDate, TemporalField woy, Course c) {
        return c.getParityOfTheWeek() == ParityOfTheWeek.ODD && firstDate.get(woy) % 2 == 1;
    }

    private boolean actualDateIsInEvenWeek(LocalDate firstDate, TemporalField woy, Course c) {
        return c.getParityOfTheWeek() == ParityOfTheWeek.EVEN && firstDate.get(woy) % 2 == 0;
    }

    private String addTitle(Course course){
        return String.format("\"title\" : \"%s\\n\\n%s\\n%s\"", course.getClassOwner().getName(),
                course.getCoursesPlace(), course.getLecturer().getName());
    }

    private String addStartDateTime(LocalDate startDate, LocalTime startTime){
        return String.format("\"start\" : \"%sT%s\"", startDate.toString(), startTime.toString());
    }

    private String addEndDateTime(LocalDate startDate, LocalTime startTime){
        return String.format("\"end\" : \"%sT%s\"", startDate.toString(), startTime.toString());
    }

    private String addCategory(String category){
        return String.format("\"category\" : \"%s\"", category);
    }
}