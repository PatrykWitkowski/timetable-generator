package com.pw.timetablegenerator.ui.components;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.dts.ClassParityWeekPreferenceDts;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassParityWeekRatingTableComponent extends RatingTableComponent<Class> {

    private Map<Class, ComboBox<ParityOfTheWeek>> parityForClass = new HashMap<>();

    public ClassParityWeekRatingTableComponent() {
        super();
        setComponentName(getTranslation(Class_.EVEN_ODD_WEEK));
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
            final ComboBox<ParityOfTheWeek> parityOfWeekComboBox = new ComboBox<>();
            parityOfWeekComboBox.setItems(Lists.newArrayList(ParityOfTheWeek.EVEN, ParityOfTheWeek.ODD));
            parityOfWeekComboBox.setItemLabelGenerator((ItemLabelGenerator<ParityOfTheWeek>) parityOfTheWeek ->
                    getTranslation(parityOfTheWeek.getProperty()));
            parityForClass.put(c, parityOfWeekComboBox);
            return parityOfWeekComboBox;
        }).setHeader(getTranslation(Class_.WEEK_PARITY));
    }


    @Override
    protected ItemLabelGenerator<Class> setItemLabelGenerator() {
        return (ItemLabelGenerator<Class>) cl ->
                String.format("%s [%s]", cl.getName(), getTranslation(cl.getClassType().getProperty()));
    }

    @Override
    public List<PreferenceDts> getPreferences() {
        List<PreferenceDts> classParityWeekePreferencesDts = new ArrayList<>();
        getRatingStarsComponents().keySet().stream()
                .forEach(c -> {
                    ClassParityWeekPreferenceDts classParityWeekPreferenceDts
                            = new ClassParityWeekPreferenceDts(c, parityForClass.get(c).getValue(),
                            getRatingStarsComponents().get(c).getStarValue());
                    classParityWeekePreferencesDts.add(classParityWeekPreferenceDts);
                });
        return classParityWeekePreferencesDts;
    }
}
