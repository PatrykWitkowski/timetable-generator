package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.Group;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GroupEditorDialog extends AbstractEditorDialog<Group> {

    protected GroupEditorDialog(BiConsumer<Group, Operation> itemSaver, Consumer<Group> itemDeleter) {
        super("Group", itemSaver, itemDeleter);
    }

    @Override
    protected void afterDialogOpen() {

    }

    @Override
    protected void confirmDelete() {

    }
}
