package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Tag("lecturer-component")
public class LecturerComponent extends Component implements HasComponents, HasValue<ValueChangeEvent<Lecturer>, Lecturer> {

    private ComboBox<Lecturer> lecturer = new ComboBox<>(getTranslation(Lecturer_.LECTURER));
    private TextField newLecturerField = new TextField();

    public  LecturerComponent(){
        lecturer.setRequired(true);
        lecturer.setItems(findAllUserLecturers());
        lecturer.setSizeFull();
        newLecturerField.setPlaceholder(getTranslation(Lecturer_.NEW_LECTURER));
        newLecturerField.setSizeFull();

        add(lecturer, newLecturerField);
    }

    private Set<Lecturer> findAllUserLecturers() {
        return SecurityUtils.getCurrentUser().getUser().getOwnerClasses().stream()
                .flatMap(c -> c.getCourses().stream())
                .map(Course::getLecturer)
                .collect(Collectors.toSet());
    }

    @Override
    public void setValue(Lecturer fieldValue) {
        lecturer.setItems(findAllUserLecturers());
        lecturer.setValue(fieldValue);
        newLecturerField.setValue(StringUtils.EMPTY);
    }

    @Override
    public Lecturer getValue() {
        if(newLecturerField.isEmpty()){
            return lecturer.getValue();
        }
        Lecturer newLecturer = new Lecturer(newLecturerField.getValue());
        final Optional<Lecturer> uniqueLecturer = checkIfNewLecturerIsUnique(newLecturer);
        return uniqueLecturer.orElse(newLecturer);
    }

    private Optional<Lecturer> checkIfNewLecturerIsUnique(Lecturer newLecturer) {
        return findAllUserLecturers().stream()
                .filter(l -> StringUtils.equalsIgnoreCase(l.getName(), newLecturer.getName()))
                .findFirst();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Lecturer>> valueChangeListener) {
        return this.lecturer.addValueChangeListener(valueChangeListener);
    }

    @Override
    public void setReadOnly(boolean b) {
        this.lecturer.setReadOnly(b);
    }

    @Override
    public boolean isReadOnly() {
        return this.lecturer.isReadOnly();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean b) {
        this.lecturer.setRequiredIndicatorVisible(b);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return this.lecturer.isRequiredIndicatorVisible();
    }

    @Override
    public void clear() {
        lecturer.clear();
        newLecturerField.clear();
    }
}
