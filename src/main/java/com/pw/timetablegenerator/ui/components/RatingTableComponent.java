package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Tag("rating-table-component")
public class RatingTableComponent extends Component
        implements Serializable, HasComponents {

    private ComboBox<Lecturer> searchComboBox = new ComboBox<>();
    private Button addButton = new Button(VaadinIcon.PLUS.create());
    private Button deleteButton = new Button(VaadinIcon.MINUS.create());
    private Grid<Lecturer> ratingTable = new Grid<>();

    public RatingTableComponent(String componentName){
        initSearchComboBox(componentName);
        initRatingTable();
    }

    private void initRatingTable() {
        ratingTable.addColumn(Lecturer::getName).setHeader("Lecturer");
        ratingTable.addComponentColumn(l -> new RatingStarsComponent()).setHeader("Priority");
        add(ratingTable);
    }

    private void initSearchComboBox(String componentName){
        searchComboBox.setLabel(componentName);
        searchComboBox.setAllowCustomValue(false);

        ratingTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        addButton.addClickListener(e -> {
           if(searchComboBox.getValue() != null){
               ListDataProvider<Lecturer> all = (ListDataProvider<Lecturer>) ratingTable.getDataProvider();
               final boolean alreadyAdded = all.getItems().stream()
                       .anyMatch(lecture -> Objects.equals(lecture, searchComboBox.getValue()));
               if(!alreadyAdded){
                   List<Lecturer> allItems = all.getItems().stream().collect(Collectors.toList());
                   allItems.add(searchComboBox.getValue());
                   ratingTable.setItems(allItems);
               } else {
                   Notification.show("Lecturer already added to table!", 3000, Notification.Position.MIDDLE);
               }
           }
        });

        deleteButton.setEnabled(false);
        ratingTable.addSelectionListener(e -> {
            if(e.getFirstSelectedItem().isPresent()){
                deleteButton.setEnabled(true);
            } else {
                deleteButton.setEnabled(false);
            }
        });
        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> {
            if(!ratingTable.getSelectedItems().isEmpty()){
                ListDataProvider<Lecturer> all = (ListDataProvider<Lecturer>) ratingTable.getDataProvider();
                final List<Lecturer> afterDeletion = all.getItems().stream()
                        .filter(lecture -> !Objects.equals(lecture, ratingTable.getSelectedItems().stream().findFirst().get()))
                        .collect(Collectors.toList());
                ratingTable.setItems(afterDeletion);
            }
        });


        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        searchPanel.setWidth("100%");
        searchPanel.expand(searchComboBox);
        searchPanel.setFlexGrow(0.5, addButton);
        searchPanel.setFlexGrow(0.5, deleteButton);
        searchPanel.add(searchComboBox, addButton, deleteButton);
        add(searchPanel);
    }

    public void updateSearchComboBox(Set<Lecturer> values){
        searchComboBox.setItems(values);
    }

}
