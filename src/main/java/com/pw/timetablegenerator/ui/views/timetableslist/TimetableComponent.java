package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Tag("timetable-component")
@HtmlImport("frontend://src/views/timetableslist/timetable.html")
public class TimetableComponent extends PolymerTemplate<TimetableComponent.TimetablesModel> {

    public interface TimetablesModel extends TemplateModel {
        void setCourses(String courses);
    }

    public void setTimetable(Timetable timetable){
        final String s = formatCoursesToString(timetable.getCourses());
        getModel().setCourses(s);
    }

    private String formatCoursesToString(List<Course> courses) {
        final List<String> coursesToString = courses.stream()
                .map(course -> String.format("{\"title\" : \"%s\", \"start\" : \"%sT%s\"}",
                        course.getGroupCode(), course.getCoursesDate().toLocalDate().toString(),
                        course.getCoursesDate().toLocalTime().toString()))
                .collect(Collectors.toList());

        String result = "[";
        for(String c : coursesToString){
            result += c;
            result +=",";
        }
        result = StringUtils.removeEnd(result, ",");
        result += "]";
        return result;
    }
}