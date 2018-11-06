package com.pw.timetablegenerator.ui.views.groupslist;

import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.components.TimePickerComponent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CourseEditorDialog extends AbstractEditorDialog<Course> {

    private ComboBox<Class> classOwner = new ComboBox<>(getTranslation(Class_.CLASS));
    private TextField groupCode = new TextField(getTranslation(Course_.GROUP_CODE));
    private ComboBox<DayOfWeek> coursesDay = new ComboBox<>(getTranslation(Class_.DAY));
    private Checkbox evenWeek = new Checkbox(getTranslation(Course_.PARITY_EVEN));
    private Checkbox oddWeek = new Checkbox(getTranslation(Course_.PARITY_ODD));
    private ComboBox<Lecturer> lecturer = new ComboBox<>(getTranslation(Lecturer_.LECTURER));
    private TimePickerComponent startTime = new TimePickerComponent(getTranslation(Course_.START_TIME));


    protected CourseEditorDialog(BiConsumer<Course, Operation> itemSaver, Consumer<Course> itemDeleter) {
        super(StringUtils.EMPTY, itemSaver, itemDeleter);
        setItemType(StringUtils.lowerCase(getTranslation(Course_.COURSE)));

        createClassField();
        createGroupCodeField();
        createCoursesDayField();
        createLecturerField();
        createStartTimeField();
    }

    private void createStartTimeField() {
        getFormLayout().add(startTime);

        getBinder().forField(startTime.getTextField())
                .withConverter(new Converter<String, LocalTime>() {
                    @Override
                    public Result<LocalTime> convertToModel(String s, ValueContext valueContext) {
                        try {
                            final LocalTime localTime = LocalTime.parse(s, DateTimeFormatter.ISO_TIME);
                            return Result.ok(localTime);
                        } catch (DateTimeParseException e) {
                            return Result.error(getTranslation(Course_.MSG_INVALID_TIME_FORMAT));
                        }
                    }

                    @Override
                    public String convertToPresentation(LocalTime o, ValueContext valueContext) {
                        return o != null ? o.toString() : "7:00";
                    }
                })
                .bind(Course::getCourseStartTime, Course::setCourseStartTime);
    }

    private void createLecturerField() {
        lecturer.setRequired(true);
        //check if no contains duplications
        final Set<Lecturer> allUserLectures = findAllUserLecturers();
        lecturer.setItems(allUserLectures);
        lecturer.setAllowCustomValue(true);
        lecturer.addCustomValueSetListener(e -> {
            Lecturer newLecturer = new Lecturer(e.getDetail());
            Set<Lecturer> lecturers = findAllUserLecturers();
            lecturers.add(newLecturer);
            lecturer.setItems(lecturers);
        });
        getFormLayout().add(lecturer);

        getBinder().forField(lecturer)
                .withValidator(Objects::nonNull, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Course::getLecturer, Course::setLecturer);
    }

    private Set<Lecturer> findAllUserLecturers() {
        return SecurityUtils.getCurrentUser().getUser().getOwnerClasses().stream()
                    .flatMap(c -> c.getCourses().stream())
                    .map(Course::getLecturer)
                    .collect(Collectors.toSet());
    }

    private void createCoursesDayField() {
        coursesDay.setRequired(true);
        coursesDay.setAllowCustomValue(false);
        coursesDay.setItems(DayOfWeek.values());
        coursesDay.setItemLabelGenerator((ItemLabelGenerator<DayOfWeek>) dayOfWeek ->
                dayOfWeek.getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale()));

        getBinder().forField(coursesDay)
                .withValidator(Objects::nonNull, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Course::getCourseDay, Course::setCourseDay);

        evenWeek.setValue(true);
        evenWeek.addValueChangeListener(e -> {
            oddWeek.setEnabled(evenWeek.getValue());
            setParityOfWeek();
        });
        oddWeek.setValue(true);
        oddWeek.addValueChangeListener(e -> {
            evenWeek.setEnabled(oddWeek.getValue());
            setParityOfWeek();
        });

        HorizontalLayout dayWithWeekParityLayout = new HorizontalLayout(coursesDay, evenWeek, oddWeek);
        dayWithWeekParityLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        getFormLayout().add(dayWithWeekParityLayout);
    }

    private void setParityOfWeek() {
        if(evenWeek.getValue() && oddWeek.getValue()){
            getCurrentItem().setParityOfTheWeek(ParityOfTheWeek.WEEKLY);
        } else if(evenWeek.getValue()) {
            getCurrentItem().setParityOfTheWeek(ParityOfTheWeek.EVEN);
        } else {
            getCurrentItem().setParityOfTheWeek(ParityOfTheWeek.ODD);
        }
    }

    private void createGroupCodeField() {
        groupCode.setRequired(true);
        getFormLayout().add(groupCode);

        getBinder().forField(groupCode)
                .withValidator(StringUtils::isNotBlank, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Course::getGroupCode, Course::setGroupCode);
    }

    private void createClassField() {
        classOwner.setRequired(true);
        classOwner.setAllowCustomValue(false);
        classOwner.setItems(SecurityUtils.getCurrentUser().getUser().getOwnerClasses());
        getFormLayout().add(classOwner);

        getBinder().forField(classOwner)
                .withValidator(Objects::nonNull, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Course::getClassOwner, Course::setClassOwner);
    }

    @Override
    protected void afterDialogOpen() {

    }

    @Override
    protected void confirmDelete() {

    }

}
