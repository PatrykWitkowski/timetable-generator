package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Group_;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag("class-manager")
public class ClassManager extends Component implements HasComponents {

    public static final String ECTS_SUM = "ectsSum";
    private ComboBox<Class> classComboBox = new ComboBox<>();
    private Grid<Class> selectedClasses = new Grid<>();
    private Button addClass = new Button(getTranslation(App_.ADD));
    private Button deleteClass = new Button(getTranslation(App_.DELETE));
    @Getter
    private EnrollmentGroup currentEnrollmentGroup;
    private Grid.Column<Class> ectsColumn;
    @Getter
    private long ectsSum;

    public ClassManager(EnrollmentGroup currentItem){
        currentEnrollmentGroup = currentItem;

        createClassComboBox();
        createButtons();
        createClassTable();
        calculateTotalPoints(ectsColumn);

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
        selectedClasses.addColumn(Class::getName).setHeader(getTranslation(Group_.NAME)).setWidth("40%");
        selectedClasses.addColumn(Class::getClassType).setHeader(getTranslation(Group_.TYPE)).setWidth("30%");
        ectsColumn = selectedClasses.addColumn(Class::getEcts).setHeader(getTranslation(Group_.ECTS)).setKey(ECTS_SUM);

        selectedClasses.addSelectionListener(e -> deleteClass.setEnabled(!e.getAllSelectedItems().isEmpty()));
        selectedClasses.setItems(getCurrentEnrollmentGroup().getClasses());
    }

    private void createButtons() {
        addClass.getElement().setAttribute("theme", "primary");
        addClass.setEnabled(false);
        addClass.addClickListener(e -> {
            if(!classComboBox.isEmpty()){
                if(getCurrentEnrollmentGroup().getClasses().stream()
                        .noneMatch(c -> Objects.equals(c , classComboBox.getValue()))){
                    getCurrentEnrollmentGroup().getClasses().add(classComboBox.getValue());
                    selectedClasses.setItems(getCurrentEnrollmentGroup().getClasses());
                    calculateTotalPoints(ectsColumn);
                } else {
                    Notification.show(getTranslation(Class_.MSG_CLASS_ALREADY_ADDED),
                            3000,
                            Notification.Position.MIDDLE);
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
            calculateTotalPoints(ectsColumn);
        });
    }

    private void createClassComboBox() {
        classComboBox.setLabel(getTranslation(Class_.CLASS));
        final List<Class> ownerClasses = currentEnrollmentGroup.getOwner().getOwnerClasses();
        classComboBox.setItems(ownerClasses != null ? ownerClasses : Collections.emptyList());
        classComboBox.addValueChangeListener(e -> addClass.setEnabled(e.getValue() != null));
    }

    private void calculateTotalPoints(Grid.Column<Class> enrollmentGroupColumn) {
        ListDataProvider<Class> dataProvider
                = (ListDataProvider<Class>)selectedClasses.getDataProvider();
        ectsSum = dataProvider.getItems().stream()
                .mapToLong(Class::getEcts)
                .sum();
        String totalEctsPoints = Long.toString(ectsSum);
        if(selectedClasses.getFooterRows().isEmpty()){
            selectedClasses.appendFooterRow().getCell(enrollmentGroupColumn).setText(getTranslation(Group_.ECTS_TOTAL) + totalEctsPoints);
        } else {
            selectedClasses.getFooterRows().stream().findFirst().ifPresent(footer ->
                    footer.getCell(selectedClasses.getColumnByKey("ectsSum"))
                            .setText(getTranslation(Group_.ECTS_TOTAL) + totalEctsPoints));
        }
    }
}
