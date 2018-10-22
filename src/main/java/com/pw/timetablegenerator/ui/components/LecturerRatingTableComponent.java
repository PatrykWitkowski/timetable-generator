package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;
import com.vaadin.flow.component.ItemLabelGenerator;

public class LecturerRatingTableComponent extends RatingTableComponent<Lecturer> {

    public LecturerRatingTableComponent() {
        super();
        setComponentName(getTranslation(Lecturer_.LECTURER));
    }

    @Override
    protected void addColumns() {
        getRatingTable().addColumn(Lecturer::getName).setHeader(getTranslation(Lecturer_.LECTURER));
    }


    @Override
    protected ItemLabelGenerator<Lecturer> setItemLabelGenerator() {
        return (ItemLabelGenerator<Lecturer>) lecturer -> lecturer.getName();
    }
}
