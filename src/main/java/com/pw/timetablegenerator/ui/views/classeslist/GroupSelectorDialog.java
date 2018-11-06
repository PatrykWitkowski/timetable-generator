package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.pw.timetablegenerator.backend.entity.properties.Group_;
import com.pw.timetablegenerator.backend.entity.properties.Timetable_;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GroupSelectorDialog extends Dialog {

    private final H3 titleField = new H3(getTranslation(Group_.MSG_SELECT_GROUP));
    private final Button enrollmentGroupButton = new Button(getTranslation(Timetable_.ENROLLMENT));
    private final Button classButton = new Button(getTranslation(Class_.CLASS));
    private final Button courseButton = new Button(getTranslation(Course_.COURSE));

    private final EnrollmentGroupEditorDialog enrollmentGroupEditorDialog;

    public GroupSelectorDialog(BiConsumer<EnrollmentGroup, AbstractEditorDialog.Operation> itemSaver,
                               Consumer<EnrollmentGroup> itemDeleter) {
        HorizontalLayout titleLayout = new HorizontalLayout(titleField);
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(titleLayout);

        HorizontalLayout buttonsBar = new HorizontalLayout(enrollmentGroupButton, classButton, courseButton);
        add(buttonsBar);

        enrollmentGroupEditorDialog = new EnrollmentGroupEditorDialog(itemSaver, itemDeleter);
        enrollmentGroupButton.addClickListener(e -> {
            final EnrollmentGroup newEnrollmentGroup = new EnrollmentGroup(SecurityUtils.getCurrentUser().getUser());
            enrollmentGroupEditorDialog.open(newEnrollmentGroup, AbstractEditorDialog.Operation.ADD);
        });
    }

    public void editEnrollmentGroup(EnrollmentGroup item){
        enrollmentGroupEditorDialog.open(item, AbstractEditorDialog.Operation.EDIT);
    }

}
