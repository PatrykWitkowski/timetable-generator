package com.pw.timetablegenerator.ui.views.groupslist;

import com.pw.timetablegenerator.backend.common.ClassType;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.properties.*;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
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
    private CoursesManager coursesManager;
    private boolean coursesManagerAlreadyAdded = false;
    private Tab tabCourses;
    private BiConsumer<Course, Operation> itemSaver;
    private Consumer<Course> itemDeleter;
    private BiConsumer<Class, Operation> classSaver;
    private Consumer<Class> classDeleter;
    private Operation currentOperation;

    protected ClassEditorDialog(BiConsumer<Class, Operation> itemSaver, Consumer<Class> itemDeleter,
                                BiConsumer<Course, Operation> courseSaver, Consumer<Course> courseDeleter) {
        super(StringUtils.EMPTY, itemSaver, itemDeleter);
        this.itemSaver = courseSaver;
        this.itemDeleter = courseDeleter;
        this.classSaver = itemSaver;
        this.classDeleter = itemDeleter;
        setItemType(Class_.NEW_CLASS, Class_.EDIT_CLASS);

        createNameField();
        createEctsField();
        createClassTypeField();
    }

    private void createClassTypeField() {
        classType.setRequired(true);
        classType.setItems(ClassType.values());
        classType.setItemLabelGenerator((ItemLabelGenerator<ClassType>) classType -> getTranslation(classType.getProperty()));
        classType.setAllowCustomValue(false);
        classType.setPreventInvalidInput(true);
        getFormLayout().add(classType);

        getBinder().forField(classType)
                .withValidator(Objects::nonNull, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Class::getClassType, Class::setClassType);
    }

    private void createEctsField() {
        classEcts.setRequired(true);
        classEcts.addValueChangeListener(e -> getCurrentItem().setEcts(Long.parseLong(e.getValue())));
        getFormLayout().add(classEcts);

        getBinder().forField(classEcts)
                .withValidator(StringUtils::isNotBlank, getTranslation(App_.MSG_NOT_BLANK))
                .withConverter(new StringToLongConverter(getTranslation(App_.MSG_NUMERIC)))
                .bind(Class::getEcts, Class::setEcts);
    }

    private void createNameField() {
        className.setRequired(true);
        className.addValueChangeListener(e -> getCurrentItem().setName(e.getValue()));
        getFormLayout().add(className);

        className.addValueChangeListener(e -> coursesManager.addCourseButtonEnabled(StringUtils.isNotBlank(e.getValue())));

        getBinder().forField(className)
                .withValidator(StringUtils::isNotBlank, getTranslation(App_.MSG_NOT_BLANK))
                .bind(Class::getName, Class::setName);
    }

    @Override
    protected void afterDialogOpen(Operation operation) {
        classType.clear();
        classType.setValue(getCurrentItem().getClassType());
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog(getTranslation(Group_.MSG_DELETE_CLASS_GROUP_TITLE),
                getTranslation(Group_.MSG_DELETE_CLASS_GROUP_CONFIRMATION) + "”"+ getCurrentItem().getName() + "”?",
                "");
    }

    @Override
    public void open(Class item, Operation operation) {
        this.currentOperation = operation;
        createCoursesManager(item);
        coursesManager.addCourseButtonEnabled(StringUtils.isNotBlank(className.getValue()));
        super.open(item, operation);
    }

    private void createCoursesManager(Class item) {
        if(!coursesManagerAlreadyAdded){
            coursesManager = new CoursesManager(item, itemSaver, itemDeleter, classSaver);
            tabCourses = addNewTab(getTranslation(Course_.COURSES), new Div(coursesManager));
            coursesManagerAlreadyAdded = true;
        } else{
            coursesManager = new CoursesManager(item, itemSaver, itemDeleter, classSaver);
            updateTab(tabCourses, new Div(coursesManager));
        }
    }

    public void refreshCourseManager(Course course){
        if(coursesManager != null){
            coursesManager.refreshCoursesTable(course);
        }
    }

    @Override
    protected void cancelClicked() {
        if(getCurrentItem().getClassId() != null && currentOperation == Operation.ADD){
            classDeleter.accept(getCurrentItem());
        }
        super.cancelClicked();
    }
}
