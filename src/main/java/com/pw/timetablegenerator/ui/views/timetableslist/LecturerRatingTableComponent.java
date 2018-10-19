package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.ui.components.RatingTableComponent;

public class LecturerRatingTableComponent extends RatingTableComponent<Lecturer> {

    public LecturerRatingTableComponent() {
        super("Lecturer");
    }

    @Override
    protected void addColumns() {
        getRatingTable().addColumn(Lecturer::getName).setHeader("Lecturer");
    }
}
