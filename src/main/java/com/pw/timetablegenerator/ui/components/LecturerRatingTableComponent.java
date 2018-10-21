package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;

public class LecturerRatingTableComponent extends RatingTableComponent<Lecturer> {

    public LecturerRatingTableComponent() {
        super();
        setComponentName(getTranslation(Lecturer_.LECTURER));
    }

    @Override
    protected void addColumns() {
        getRatingTable().addColumn(Lecturer::getName).setHeader(getTranslation(Lecturer_.LECTURER));
    }
}
