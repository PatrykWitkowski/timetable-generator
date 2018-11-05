package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag("class-manager")
public class ClassManager extends Component implements HasComponents {

    private ComboBox<Class> classComboBox = new ComboBox<>();
    private Grid<Class> selectedClasses = new Grid<>();
    private Button addClass = new Button("Add");
    private Button deleteClass = new Button("Delete");
    @Getter
    private EnrollmentGroup currentEnrollmentGroup;

    public ClassManager(EnrollmentGroup currentItem){
        currentEnrollmentGroup = currentItem;

        createClassComboBox();
        createButtons();
        createClassTable();

        HorizontalLayout buttonBar = new HorizontalLayout(addClass, deleteClass);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);

        selectedClasses.setWidth("40em");
        VerticalLayout leftSideBar = new VerticalLayout(classComboBox, buttonBar);
        leftSideBar.setWidth("40%");
        HorizontalLayout productsLayout = new HorizontalLayout(leftSideBar, selectedClasses);

        add(productsLayout);
    }

    private void createClassTable() {
        selectedClasses.addColumn(Class::getName).setHeader("Name");
        selectedClasses.addColumn(Class::getClassType).setHeader("Type");
        selectedClasses.addColumn(Class::getEcts).setHeader("ECTS");

        selectedClasses.addSelectionListener(e -> deleteClass.setEnabled(!e.getAllSelectedItems().isEmpty()));
    }

    private void createButtons() {
        addClass.getElement().setAttribute("theme", "primary");
        addClass.setEnabled(false);
        addClass.addClickListener(e -> {
            if(!classComboBox.isEmpty()){
                if(getCurrentEnrollmentGroup().getClasses().stream()
                        .noneMatch(c -> Objects.equals(c , classComboBox.getValue()))){
                    // check if ects sum <
                    getCurrentEnrollmentGroup().getClasses().add(classComboBox.getValue());
                    selectedClasses.setItems(getCurrentEnrollmentGroup().getClasses());
                } else {
                    Notification.show("Already added!", 3000, Notification.Position.MIDDLE);
                }
            }
        });

        deleteClass.getElement().setAttribute("theme", "error");
        deleteClass.setEnabled(false);
        deleteClass.addClickListener(e -> {
            final List<Class> classes = getCurrentEnrollmentGroup().getClasses().stream()
                    .filter(item -> !selectedClasses.getSelectedItems().contains(item))
                    .collect(Collectors.toList());
            getCurrentEnrollmentGroup().setClasses(classes);
            selectedClasses.setItems(classes);
        });
    }

    private void createClassComboBox() {
        classComboBox.setLabel(getTranslation(Class_.CLASS));
        final List<Class> ownerClasses = currentEnrollmentGroup.getOwner().getOwnerClasses();
        classComboBox.setItems(ownerClasses != null ? ownerClasses : Collections.emptyList());
        classComboBox.addValueChangeListener(e -> addClass.setEnabled(e.getValue() != null));
    }
}
