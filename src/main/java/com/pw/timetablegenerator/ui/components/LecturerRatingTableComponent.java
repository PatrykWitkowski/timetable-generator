package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Lecturer;

public class LecturerRatingTableComponent extends RatingTableComponent<Lecturer> {

    public LecturerRatingTableComponent() {
        super("Lecturer");
    }

    @Override
    protected void addColumns() {
        getRatingTable().addColumn(Lecturer::getName).setHeader("Lecturer");
    }
}
