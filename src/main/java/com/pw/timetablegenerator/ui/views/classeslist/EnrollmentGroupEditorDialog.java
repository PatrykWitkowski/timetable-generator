package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Group_;
import com.pw.timetablegenerator.backend.entity.properties.Timetable_;
import com.pw.timetablegenerator.backend.utils.converter.RomanNumber;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EnrollmentGroupEditorDialog extends AbstractEditorDialog<EnrollmentGroup> {

    private TextField enrollmentGroupName = new TextField();
    private ComboBox<String> semester = new ComboBox<>();
    private TextField ectsField = new TextField();
    private FormLayout classesFormLayout;
    private Tab tabClasses;
    private ClassManager classManager;
    private boolean classManagerAlreadyAdded = false;


    protected EnrollmentGroupEditorDialog(BiConsumer<EnrollmentGroup, Operation> itemSaver,
                                          Consumer<EnrollmentGroup> itemDeleter) {
        super("", itemSaver, itemDeleter);

        createNameField();
        createSemesterField();
        createEctsField();
    }

    private void createEctsField() {
        ectsField.setRequired(true);
        ectsField.setLabel("ects");
        ectsField.setPreventInvalidInput(true);
        ectsField.setPattern("^[0-9]+$");
        ectsField.setValue("30");
        getFormLayout().add(ectsField);

        getBinder().forField(ectsField)
                .withValidator(StringUtils::isNotBlank, "msg")
                .withConverter(new StringToLongConverter("msg"))
                .bind(EnrollmentGroup::getEctsSum, EnrollmentGroup::setEctsSum);
    }

    private void createSemesterField() {
        semester.setRequired(true);
        semester.setLabel(getTranslation(Timetable_.SEMESTER));
        semester.setItems("I", "II", "III", "IV", "V", "VI", "VII");
        semester.setValue("I");
        getFormLayout().add(semester);

        getBinder().forField(semester)
                .withValidator(Objects::nonNull, "msg")
                .withConverter(new Converter<String, Long>() {
                    @Override
                    public Result<Long> convertToModel(String s, ValueContext valueContext) {
                        return Result.ok(RomanNumber.toNumber(s));
                    }

                    @Override
                    public String convertToPresentation(Long aLong, ValueContext valueContext) {
                        return RomanNumber.toRoman(aLong.intValue());
                    }
                })
                .bind(EnrollmentGroup::getSemester, EnrollmentGroup::setSemester);
    }

    private void createNameField() {
        enrollmentGroupName.setRequired(true);
        enrollmentGroupName.setLabel(getTranslation(Group_.NAME));
        getFormLayout().add(enrollmentGroupName);

        getBinder().forField(enrollmentGroupName)
                .withValidator(StringUtils::isNotBlank, "msg")
                .bind(EnrollmentGroup::getName, EnrollmentGroup::setName);
    }

    @Override
    protected void afterDialogOpen() {

    }

    @Override
    protected void confirmDelete() {

    }

    @Override
    public void open(EnrollmentGroup item, Operation operation) {
        createClassManager(item);
        super.open(item, operation);
    }

    private void createClassManager(EnrollmentGroup item) {
        ClassManager classManager;
        if(!classManagerAlreadyAdded){
            classManager = new ClassManager(item);
            tabClasses = addNewTab(getTranslation(Class_.CLASS), new Div(classManager));
            classManagerAlreadyAdded = true;
        } else{
            classManager = new ClassManager(item);
            updateTab(tabClasses, new Div(classManager));
        }
    }
}
