package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.vaadin.flow.component.dialog.Dialog;

public class TimetablePreviewDialog extends Dialog {

    private final TimetableComponent timetable = new TimetableComponent();

    public TimetablePreviewDialog(Timetable timetable){
        initPreview(timetable);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
    }

    private void initPreview(Timetable currentTimetable) {
        timetable.setTimetable(currentTimetable);
        add(timetable);
    }
}
