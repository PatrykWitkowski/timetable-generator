package com.pw.timetablegenerator.ui.views.timetableslist;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.common.DayTime;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.dts.DayTimePreferenceDts;
import com.pw.timetablegenerator.backend.dts.FreeDayPreferenceDts;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.*;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Timetable_;
import com.pw.timetablegenerator.backend.utils.converter.RomanNumber;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.components.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
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
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TimetableEditorDialog extends AbstractEditorDialog<Timetable> {

    private TextField timetableTitle = new TextField();
    private ComboBox<EnrollmentGroup> enrollmentGroupComboBox = new ComboBox<>();
    private DatePicker startSemesterDataPicker = new DatePicker();
    private DatePicker endSemesterDataPicker = new DatePicker();
    private ComboBox<DayTime> dayTime = new ComboBox<>();
    private RatingStarsComponent dayTimeRating;
    private ComboBox<DayOfWeek> freeDay = new ComboBox<>();
    private RatingStarsComponent freeDayRating;
    private RatingTableComponent lecturersTable = new LecturerRatingTableComponent();
    private ClassOnDayRatingTableComponent classOnDayTable = new ClassOnDayRatingTableComponent();
    private ClassParityWeekRatingTableComponent classParityWeekRatingTable = new ClassParityWeekRatingTableComponent();
    private Checkbox avoidTimeBreak = new Checkbox();
    private FormLayout preferenceFormLayout;
    private Tab tabPreference;
    @Getter
    private List<PreferenceDts> preferences = new ArrayList<>();

    protected TimetableEditorDialog(BiConsumer<Timetable, Operation> saveHandler,
                                    Consumer<Timetable> deleteHandler) {
        super("", saveHandler, deleteHandler);
        setItemType(Timetable_.NEW, Timetable_.EDIT);

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
        tabPreference = addNewTab(getTranslation(Timetable_.PREFERENCES), new Div(preferenceFormLayout));
    }

    private void createAvoidTimeBreakPreference() {
        avoidTimeBreak.setLabel(getTranslation(Timetable_.AVOID_TIME_BREAK));
        createFieldWithRating(avoidTimeBreak);
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
        freeDay.setLabel(getTranslation(Timetable_.FREE_DAY));
        freeDay.setAllowCustomValue(false);
        freeDay.setItems(DayOfWeek.values());
        freeDay.setItemLabelGenerator((ItemLabelGenerator<DayOfWeek>) dayOfWeek ->
                dayOfWeek.getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale()));
        freeDayRating = createFieldWithRating(freeDay);
    }

    private void createDayTimePreference() {
        dayTime.setLabel(getTranslation(Timetable_.DAY_TIME));
        dayTime.setAllowCustomValue(false);
        dayTime.setItems(DayTime.values());
        dayTime.setItemLabelGenerator((ItemLabelGenerator<DayTime>) dayTime -> getTranslation(dayTime.getProperty()));
        dayTimeRating = createFieldWithRating(dayTime);
    }

    private RatingStarsComponent createFieldWithRating(Component field) {
        RatingStarsComponent ratingStarsComponent = new RatingStarsComponent();
        HorizontalLayout ratingStartLayout = new HorizontalLayout();
        ratingStartLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        ratingStartLayout.setWidth("100%");
        ratingStartLayout.expand(field);
        ratingStartLayout.setFlexGrow(0.5, ratingStarsComponent);
        ratingStartLayout.add(field, ratingStarsComponent);
        preferenceFormLayout.add(ratingStartLayout);
        return ratingStarsComponent;
    }

    private void createStartEndSemesterDataPickers() {
        startSemesterDataPicker.setLabel(getTranslation(Timetable_.SEMESTER_START));
        startSemesterDataPicker.setRequired(true);
        getFormLayout().add(startSemesterDataPicker);
        startSemesterDataPicker.addValueChangeListener(e -> {
           if(e.getValue() != null){
               getBinder().forField(endSemesterDataPicker)
                       .withValidator(Objects::nonNull,
                               getTranslation(App_.MSG_DATE_FORMAT))
                       .withValidator(new DateRangeValidator(
                               getTranslation(Timetable_.MSG_SEMESTER_END_WARNING),
                               startSemesterDataPicker.getValue().plusDays(1), LocalDate.MAX))
                       .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
           } else {
               getBinder().forField(endSemesterDataPicker)
                       .withValidator(Objects::nonNull,
                               getTranslation(App_.MSG_DATE_FORMAT))
                       .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
           }
        });

        getBinder().forField(startSemesterDataPicker)
                .withValidator(Objects::nonNull,
                        getTranslation(App_.MSG_DATE_FORMAT))
                .bind(Timetable::getSemesterStartDate, Timetable::setSemesterStartDate);

        endSemesterDataPicker.setLabel(getTranslation(Timetable_.SEMESTER_END));
        endSemesterDataPicker.setRequired(true);
        getFormLayout().add(endSemesterDataPicker);

        getBinder().forField(endSemesterDataPicker)
                .withValidator(Objects::nonNull,
                        getTranslation(App_.MSG_DATE_FORMAT))
                .bind(Timetable::getSemesterEndDate, Timetable::setSemesterEndDate);
    }

    private void createEnrollmentGroupChoose() {
        enrollmentGroupComboBox.setLabel(getTranslation(Timetable_.ENROLLMENT));
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

        enrollmentGroupComboBox.setItemLabelGenerator((ItemLabelGenerator<EnrollmentGroup>) enrollmentGroup ->
                String.format("%s [%s: %s]", enrollmentGroup.getName(), getTranslation(Timetable_.SEMESTER),
                RomanNumber.toRoman(enrollmentGroup.getSemester().intValue())));
        getBinder().forField(enrollmentGroupComboBox)
                .withValidator(Objects::nonNull, getTranslation(Timetable_.MSG_ENROLLMENT_WARNING))
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
        final String name = getTranslation(Timetable_.NAME);
        timetableTitle.setLabel(name);
        timetableTitle.setRequired(true);
        getFormLayout().add(timetableTitle);

        final int minLength = 5;
        getBinder().forField(timetableTitle)
                .withValidator(new StringLengthValidator(
                        name + " " + String.format(getTranslation(App_.MSG_MUST_CONTAIN_AT_LEAST), minLength),
                        minLength, 255))
                .bind(Timetable::getName, Timetable::setName);
    }


    @Override
    protected void afterDialogOpen(Operation operation) {
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
            Notification.show(getTranslation(Timetable_.MSG_PREFERENCE_WARNING), 3000, Notification.Position.MIDDLE);
            return;
        }
        addPreferences();

        super.saveClicked(operation);
    }

    private void addPreferences() {
        preferences.add(new DayTimePreferenceDts(dayTime.getValue(), dayTimeRating.getStarValue()));
        preferences.add(new FreeDayPreferenceDts(freeDay.getValue(), freeDayRating.getStarValue()));
        preferences.addAll(lecturersTable.getPreferences());
        preferences.addAll(classOnDayTable.getPreferences());
        preferences.addAll(classParityWeekRatingTable.getPreferences());
    }
}
