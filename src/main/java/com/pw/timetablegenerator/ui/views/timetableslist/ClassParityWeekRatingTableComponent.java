package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.ui.components.RatingTableComponent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.apache.commons.lang3.StringUtils;

public class ClassParityWeekRatingTableComponent extends RatingTableComponent<Class> {

    public ClassParityWeekRatingTableComponent() {
        super("Class on even/odd week");
    }

    @Override
    protected void addColumns() {
        ValueProvider<Class, String> cssClassProvider
                = (c) -> "cell " + StringUtils.lowerCase(c.getClassType().toString());

        getRatingTable().addColumn(TemplateRenderer.<Class>
                of("<div class$=\"[[item.class]]\">[[item.name]]</div>")
                .withProperty("class", cssClassProvider)
                .withProperty("name", Class::getName)).setHeader("Class");

        getRatingTable().addComponentColumn(c -> {
            final ComboBox<ParityOfTheWeek> parityOfWeekComboBox = new ComboBox<>();
            parityOfWeekComboBox.setItems(Lists.newArrayList(ParityOfTheWeek.EVEN, ParityOfTheWeek.ODD));
            return parityOfWeekComboBox;
        }).setHeader("Parity of the week");
    }
}
