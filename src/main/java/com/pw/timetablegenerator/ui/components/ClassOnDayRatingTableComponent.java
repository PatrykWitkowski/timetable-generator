package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.dts.ClassOnDayPreferenceDts;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassOnDayRatingTableComponent extends RatingTableComponent<Class> {

    private Map<Class, ComboBox<DayOfWeek>> dayForClass = new HashMap<>();

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
            dayOfWeekComboBox.setItemLabelGenerator((ItemLabelGenerator<DayOfWeek>) dayOfWeek ->
                    dayOfWeek.getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale()));
            dayForClass.put(c, dayOfWeekComboBox);
            return dayOfWeekComboBox;
        }).setHeader(getTranslation(Class_.DAY));
    }

    @Override
    protected ItemLabelGenerator<Class> setItemLabelGenerator() {
        return (ItemLabelGenerator<Class>) cl ->
                String.format("%s [%s]", cl.getName(), getTranslation(cl.getClassType().getProperty()));
    }

    @Override
    public List<PreferenceDts> getPreferences() {
        List<PreferenceDts> classOnDayPreferencesDts = new ArrayList<>();
        getRatingStarsComponents().keySet().forEach(c -> {
            ClassOnDayPreferenceDts classOnDayPreferenceDts = new ClassOnDayPreferenceDts(c, dayForClass.get(c).getValue(),
                    getRatingStarsComponents().get(c).getStarValue());
            classOnDayPreferencesDts.add(classOnDayPreferenceDts);
        });

        return  classOnDayPreferencesDts;
    }
}
