package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DeleteCourseConfirmationDialog extends Dialog {

    public static final String THEME = "theme";
    public static final String TERTIARY = "tertiary";
    private final H3 titleField = new H3(getTranslation(Course_.MSG_DELETE_CONFIRMATION_TITLE));
    private final Div messageLabel = new Div();
    private final RadioButtonGroup<String> deleteOptions = new RadioButtonGroup<>();
    private final Button confirmButton = new Button(getTranslation(App_.DELETE));
    private final Button cancelButton = new Button(getTranslation(App_.CANCEL));
    private final String deleteTimetablesOptions = getTranslation(Course_.MSG_DELETE_TIMETABLES);
    private final String deleteCoursesOptions = getTranslation(Course_.MSG_DELETE_COURSES_FROM_TIMETABLES);

    public DeleteCourseConfirmationDialog(){
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);

        confirmButton.setAutofocus(true);
        confirmButton.getElement().setAttribute(THEME, "tertiary error");
        cancelButton.addClickListener(e -> close());
        cancelButton.getElement().setAttribute(THEME, TERTIARY);

        HorizontalLayout buttonBar = new HorizontalLayout(confirmButton,
                cancelButton);
        buttonBar.setClassName("buttons confirm-buttons");

        Div labels = new Div(messageLabel);
        labels.setClassName("confirm-text");

        deleteOptions.setItems(deleteTimetablesOptions, deleteCoursesOptions);
        deleteOptions.setValue(deleteTimetablesOptions);
        labels.add(deleteOptions);

        titleField.setClassName("confirm-title");

        add(titleField, labels, buttonBar);
    }

    public void open(Course course, Consumer<Timetable> saveTimetableConsumer,
                     Consumer<Timetable> deleteTimetableConsumer, Consumer<Course> deleteCourseConsumer){
        messageLabel.setText(String.format(getTranslation(Course_.MSG_DELETE_CONFIRMATION_MESSAGE),
                course.getName(), course.getTimetables().stream()
                        .map(Timetable::getName)
                        .collect(Collectors.toList())));

        confirmButton.addClickListener(e -> {
            if(StringUtils.equals(deleteOptions.getValue(), deleteTimetablesOptions)){
                deleteCourseConsumer.accept(course);
                course.getTimetables().stream()
                        .findFirst().ifPresent(t -> deleteTimetableConsumer.accept(t));
            } else if(StringUtils.equals(deleteOptions.getValue(), deleteCoursesOptions)){
                course.getTimetables().stream()
                        .forEach(t -> {
                            t.getCourses().remove(this);
                            saveTimetableConsumer.accept(t);
                            deleteCourseConsumer.accept(course);
                        });
            }
            close();
        });

        open();
    }

}
