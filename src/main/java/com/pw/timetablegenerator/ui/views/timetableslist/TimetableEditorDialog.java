package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.components.RatingStarsComponent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TimetableEditorDialog extends AbstractEditorDialog<Timetable> {

    private TextField timetableTitle = new TextField();
    private ComboBox<EnrollmentGroup> enrollmentGroupComboBox = new ComboBox<>();
    private DatePicker startSemesterDataPicker = new DatePicker();
    private DatePicker endSemesterDataPicker = new DatePicker();
    private ComboBox<String> dayTime = new ComboBox<>();
    private RatingStarsComponent ratingStarsComponentForDayTime = new RatingStarsComponent();

    protected TimetableEditorDialog(BiConsumer<Timetable, Operation> saveHandler,
                                    Consumer<Timetable> deleteHandler) {
        super("timetable", saveHandler, deleteHandler);

        createTimetableTitle();
        createEnrollmentGroupChoose();
        createStartEndSemesterDataPickers();
        getFormLayout().add(new H3("Preferences"));
        getFormLayout().add(new Div());
        createDayTimePreference();
    }

    private void createDayTimePreference() {
        dayTime.setLabel("Day time");
        dayTime.setAllowCustomValue(false);
        dayTime.setItems(Stream.of("Morning", "Afternoon", "Evening"));
        getFormLayout().add(dayTime);
        getFormLayout().add(ratingStarsComponentForDayTime);
    }

    private void createStartEndSemesterDataPickers() {
        startSemesterDataPicker.setLabel("Start semester");
        startSemesterDataPicker.setRequired(true);
        getFormLayout().add(startSemesterDataPicker);
        startSemesterDataPicker.addValueChangeListener(e -> {
           if(e.getValue() != null){
               getBinder().forField(endSemesterDataPicker)
                       .withValidator(Objects::nonNull,
                               "The date should be in MM/dd/yyyy format.")
                       .withValidator(new DateRangeValidator(
                               "The date should be after start semester.",
                               startSemesterDataPicker.getValue().plusDays(1), LocalDate.MAX))
                       .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
           } else {
               getBinder().forField(endSemesterDataPicker)
                       .withValidator(Objects::nonNull,
                               "The date should be in MM/dd/yyyy format.")
                       .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
           }
        });

        getBinder().forField(startSemesterDataPicker)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .bind(Timetable::getSemesterStartDate, Timetable::setSemesterStartDate);

        endSemesterDataPicker.setLabel("End semester");
        endSemesterDataPicker.setRequired(true);
        getFormLayout().add(endSemesterDataPicker);

        getBinder().forField(endSemesterDataPicker)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
    }

    private void createEnrollmentGroupChoose() {
        enrollmentGroupComboBox.setLabel("Enrollment group");
        enrollmentGroupComboBox.setRequired(true);
        enrollmentGroupComboBox.setAllowCustomValue(false);
        getFormLayout().add(enrollmentGroupComboBox);

        getBinder().forField(enrollmentGroupComboBox)
                .withValidator(Objects::nonNull, "You have to choose some enrollment group.")
                .withConverter(new Converter<EnrollmentGroup, Long>() {
                    @Override
                    public Result<Long> convertToModel(EnrollmentGroup enrollmentGroup, ValueContext valueContext) {
                        return Result.ok(enrollmentGroup.getSemester());
                    }

                    @Override
                    public EnrollmentGroup convertToPresentation(Long aLong, ValueContext valueContext) {
                        return null;
                    }
                })
                .bind(Timetable::getSemester, Timetable::setSemester);
    }

    private void createTimetableTitle() {
        timetableTitle.setLabel("Name");
        timetableTitle.setRequired(true);
        getFormLayout().add(timetableTitle);

        getBinder().forField(timetableTitle)
                .withValidator(new StringLengthValidator(
                        "Title must contain at least 5 characters",
                        5, 255))
                .bind(Timetable::getName, Timetable::setName);
    }


    @Override
    protected void afterDialogOpen() {
        enrollmentGroupComboBox.setItems(Optional.ofNullable(getCurrentItem().getOwner())
                .map(User::getEnrollmentGroups)
                .orElse(Lists.newArrayList()));
    }

    @Override
    protected void confirmDelete() {
        // nothing to do
    }
}
