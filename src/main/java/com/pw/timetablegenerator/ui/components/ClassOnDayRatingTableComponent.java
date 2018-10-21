package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class ClassOnDayRatingTableComponent extends RatingTableComponent<Class> {

    public ClassOnDayRatingTableComponent() {
        super();
        setComponentName(getTranslation(Class_.CLASS_ON_DAY));
    }

    @Override
    protected void addColumns() {
        ValueProvider<Class, String> cssClassProvider
                = (c) -> "cell " + StringUtils.lowerCase(c.getClassType().toString());

        getRatingTable().addColumn(TemplateRenderer.<Class>
                of("<div class$=\"[[item.class]]\">[[item.name]]</div>")
                .withProperty("class", cssClassProvider)
                .withProperty("name", Class::getName)).setHeader(getTranslation(Class_.CLASS));

        getRatingTable().addComponentColumn(c -> {
            final List<DayOfWeek> classDays = c.getCourses().stream()
                    .map(Course::getCourseDay)
                    .collect(Collectors.toList());
            final ComboBox<DayOfWeek> dayOfWeekComboBox = new ComboBox<>();
            dayOfWeekComboBox.setItems(classDays);
            return dayOfWeekComboBox;
        }).setHeader(getTranslation(Class_.DAY));
    }
}
