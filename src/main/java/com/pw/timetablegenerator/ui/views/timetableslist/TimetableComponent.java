package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.vaadin.flow.component.Tag;
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
    }

    public void setTimetable(Timetable timetable){
        final String s = formatCoursesToString(timetable);
        getModel().setCourses(s);
    }

    private String formatCoursesToString(Timetable timetable) {
        List<String> events = Lists.newArrayList();
        for(DayOfWeek day : DayOfWeek.values()){
            LocalDate semesterStartDate = timetable.getSemesterStartDate();
            //znajdz wszystkie kursy z tego dnia
            final List<Course> coursesForSpecificDay = timetable.getCourses().stream()
                    .filter(c -> c.getCourseDay() == day)
                    .collect(Collectors.toList());

            //znajdz ten pierwszy dzien w semestrze
            while(semesterStartDate.getDayOfWeek() != day){
                semesterStartDate = semesterStartDate.plusDays(1);
            }
            LocalDate firstDate = semesterStartDate;

            //dodaj dla wszystkich tych dni do konca semestru
            while(firstDate.isBefore(timetable.getSemesterEndDate())){
                addEvents(events, coursesForSpecificDay, firstDate);
                firstDate = firstDate.plusWeeks(1);
            }
        }

        String result = "[";
        for(String c : events){
            result += c;
            result +=",";
        }
        result = StringUtils.removeEnd(result, ",");
        result += "]";
        return result;
    }

    private void addEvents(List<String> events, List<Course> coursesForSpecificDay, final LocalDate firstDate) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        events.addAll(coursesForSpecificDay.stream()
                .filter(c -> c.getEvenWeek() == null
                        || (c.getEvenWeek() == true && firstDate.get(woy) % 2 == 0)
                        || (c.getEvenWeek() == false && firstDate.get(woy) % 2 == 1))
                .map(c -> String.format("{%s, %s, %s, %s}", addTitle(c),
                        addStartDateTime(firstDate, c.getCourseStartTime()), addEndDateTime(firstDate, c.getCourseEndTime()),
                        addCategory(c.getClassOwner().getClassType().name())))
                .collect(Collectors.toList()));
    }

    private String addTitle(Course course){
        return String.format("\"title\" : \"%s\\n\\n%s\\n%s\"", course.getClassOwner().getName(),
                course.getCoursesPlace(), course.getLecturer());
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