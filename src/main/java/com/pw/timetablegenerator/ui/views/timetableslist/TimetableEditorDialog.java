package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimetableEditorDialog extends AbstractEditorDialog<Timetable> {

    /**
     * Constructs a new instance.
     *
     * @param itemType    The readable name of the item type
     * @param itemSaver   Callback to save the edited item
     * @param itemDeleter
     */
    protected TimetableEditorDialog(BiConsumer<Timetable, Operation> saveHandler,
                                    Consumer<Timetable> deleteHandler) {
        super("timetable", saveHandler, deleteHandler);
    }

    @Override
    protected void confirmDelete() {

    }
}
