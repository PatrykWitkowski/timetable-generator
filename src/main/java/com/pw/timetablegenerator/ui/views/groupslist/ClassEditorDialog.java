package com.pw.timetablegenerator.ui.views.groupslist;

import com.pw.timetablegenerator.backend.common.ClassType;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.properties.*;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToLongConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClassEditorDialog extends AbstractEditorDialog<Class> {

    private TextField className = new TextField(getTranslation(Timetable_.NAME));
    private TextField classEcts = new TextField(getTranslation(Group_.ECTS));
    private ComboBox<ClassType> classType = new ComboBox<>(getTranslation(Class_.CLASS_TYPE));

    protected ClassEditorDialog(BiConsumer<Class, Operation> itemSaver, Consumer<Class> itemDeleter) {
        super(StringUtils.EMPTY, itemSaver, itemDeleter);
        setItemType(StringUtils.lowerCase(getTranslation(Class_.CLASS)));

        createNameField();
        createEctsField();
        createClassTypeField();
    }

    private void createClassTypeField() {
        classType.setRequired(true);
        classType.setItems(ClassType.values());
        classType.setAllowCustomValue(false);
        classType.setPreventInvalidInput(true);
        getFormLayout().add(classType);

        getBinder().forField(classType)
                .withValidator(Objects::nonNull, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Class::getClassType, Class::setClassType);
    }

    private void createEctsField() {
        classEcts.setRequired(true);
        getFormLayout().add(classEcts);

        getBinder().forField(classEcts)
                .withValidator(StringUtils::isNotBlank, getTranslation(App_.MSG_NOT_BLANK))
                .withConverter(new StringToLongConverter(getTranslation(App_.MSG_NUMERIC)))
                .bind(Class::getEcts, Class::setEcts);
    }

    private void createNameField() {
        className.setRequired(true);
        getFormLayout().add(className);

        getBinder().forField(className)
                .withValidator(StringUtils::isNotBlank, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Class::getName, Class::setName);
    }

    @Override
    protected void afterDialogOpen(Operation operation) {

    }

    @Override
    protected void confirmDelete() {

    }
}
