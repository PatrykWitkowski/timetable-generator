package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.ui.components.RatingTableComponent;
import com.vaadin.flow.component.grid.Grid;

public class LecturerRatingTableComponent extends RatingTableComponent {

    public LecturerRatingTableComponent() {
        super("Lecturer");
    }

    @Override
    protected void addColumns() {
        ((Grid<Lecturer>)getRatingTable()).addColumn(Lecturer::getName).setHeader("Lecturer");
    }
}
