package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.html.Div;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimetableEditorDialog extends AbstractEditorDialog<Timetable> {

    protected TimetableEditorDialog(BiConsumer<Timetable, Operation> saveHandler,
                                    Consumer<Timetable> deleteHandler) {
        super("timetable", saveHandler, deleteHandler);

        Div div = new Div(new TimetableComponent());
        getFormLayout().add(div);
    }

    @Override
    protected void confirmDelete() {

    }
}
