package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.*;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.components.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimetableEditorDialog extends AbstractEditorDialog<Timetable> {

    private TextField timetableTitle = new TextField();
    private ComboBox<EnrollmentGroup> enrollmentGroupComboBox = new ComboBox<>();
    private DatePicker startSemesterDataPicker = new DatePicker();
    private DatePicker endSemesterDataPicker = new DatePicker();
    private ComboBox<String> dayTime = new ComboBox<>();
    private ComboBox<DayOfWeek> freeDay = new ComboBox<>();
    private RatingTableComponent lecturersTable = new LecturerRatingTableComponent();
    private ClassOnDayRatingTableComponent classOnDayTable = new ClassOnDayRatingTableComponent();
    private ClassParityWeekRatingTableComponent classParityWeekRatingTable = new ClassParityWeekRatingTableComponent();
    private Checkbox avoidTimeBreak = new Checkbox();
    private FormLayout preferenceFormLayout;
    private Tab tabPreference;

    protected TimetableEditorDialog(BiConsumer<Timetable, Operation> saveHandler,
                                    Consumer<Timetable> deleteHandler) {
        super("timetable", saveHandler, deleteHandler);

        createTimetableTitle();
        createEnrollmentGroupChoose();
        createStartEndSemesterDataPickers();

        createPreferenceForm();
        createDayTimePreference();
        createFreeDayPreference();
        createLecturerPreference();
        createClassOnDayPreference();
        createClassParityWeekPreference();
        createAvoidTimeBreakPreference();
        tabPreference = addNewTab("Preferences", new Div(preferenceFormLayout));
    }

    private void createAvoidTimeBreakPreference() {
        avoidTimeBreak.setLabel("Avoid time break?");
        preferenceFormLayout.add(createFieldWithRating(avoidTimeBreak));
    }

    private void createClassParityWeekPreference() {
        preferenceFormLayout.add(classParityWeekRatingTable);
    }

    private void createClassOnDayPreference() {
        preferenceFormLayout.add(classOnDayTable);
    }

    private void createPreferenceForm() {
        preferenceFormLayout = new FormLayout();
        preferenceFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2));
    }

    @Override
    public void open(Timetable item, Operation operation) {
        updateTab(tabPreference, new Div(preferenceFormLayout));
        super.open(item, operation);
    }

    private void createLecturerPreference() {
        preferenceFormLayout.add(lecturersTable);
    }

    private void createFreeDayPreference() {
        freeDay.setLabel("Free Day");
        freeDay.setAllowCustomValue(false);
        freeDay.setItems(DayOfWeek.values());
        preferenceFormLayout.add(createFieldWithRating(freeDay));
    }

    private void createDayTimePreference() {
        dayTime.setLabel("Day time");
        dayTime.setAllowCustomValue(false);
        dayTime.setItems(Stream.of("Morning", "Afternoon", "Evening"));
        preferenceFormLayout.add(createFieldWithRating(dayTime));
    }

    private HorizontalLayout createFieldWithRating(Component field) {
        RatingStarsComponent ratingStarsComponentForDayTime = new RatingStarsComponent();
        HorizontalLayout dayTimeLayout = new HorizontalLayout();
        dayTimeLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        dayTimeLayout.setWidth("100%");
        dayTimeLayout.expand(field);
        dayTimeLayout.setFlexGrow(0.5, ratingStarsComponentForDayTime);
        dayTimeLayout.add(field, ratingStarsComponentForDayTime);
        return dayTimeLayout;
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
        enrollmentGroupComboBox.addValueChangeListener(e -> {
            if(e.getValue() != null){
                lecturersTable.updateSearchComboBox(e.getValue().getClasses().stream()
                .flatMap(c -> c.getCourses().stream()).map(Course::getLecturer)
                .collect(Collectors.toSet()));

                classOnDayTable.updateSearchComboBox(new HashSet<>(e.getValue().getClasses()));
                final Set<Class> classesInEvenOrOddWeeks = e.getValue().getClasses().stream()
                        .filter(c -> c.getCourses().stream()
                                .noneMatch(course -> course.getParityOfTheWeek() == ParityOfTheWeek.WEEKLY))
                        .filter(c -> !c.getCourses().stream()
                                .allMatch(course -> course.getParityOfTheWeek() == ParityOfTheWeek.EVEN))
                        .filter(c -> !c.getCourses().stream()
                                .allMatch(course -> course.getParityOfTheWeek() == ParityOfTheWeek.ODD))
                        .collect(Collectors.toSet());
                classParityWeekRatingTable.updateSearchComboBox(classesInEvenOrOddWeeks);
            }
        });
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

    @Override
    protected void saveClicked(Operation operation) {
        if(dayTime.isEmpty() && freeDay.isEmpty() && lecturersTable.isEmpty()
        && classOnDayTable.isEmpty() && classParityWeekRatingTable.isEmpty()){
            Notification.show("You have to choose at least one preference!", 3000, Notification.Position.MIDDLE);
            return;
        }
        super.saveClicked(operation);
    }
}
