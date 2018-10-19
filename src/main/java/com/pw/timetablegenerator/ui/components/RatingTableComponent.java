package com.pw.timetablegenerator.ui.components;

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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Tag("rating-table-component")
public abstract class RatingTableComponent<T extends Serializable> extends Component
        implements Serializable, HasComponents {

    private ComboBox<T> searchComboBox = new ComboBox<>();
    private Button addButton = new Button(VaadinIcon.PLUS.create());
    private Button deleteButton = new Button(VaadinIcon.MINUS.create());
    private Grid<T> ratingTable = new Grid<>();

    public RatingTableComponent(String componentName){
        initSearchComboBox(componentName);
        initRatingTable();
    }

    private void initRatingTable() {
        addColumns();
        ratingTable.addComponentColumn(l -> new RatingStarsComponent()).setHeader("Priority");
        add(ratingTable);
    }

    protected abstract void addColumns();

    protected Grid<T> getRatingTable(){
        return ratingTable;
    }

    private void initSearchComboBox(String componentName){
        searchComboBox.setLabel(componentName);
        searchComboBox.setAllowCustomValue(false);

        ratingTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        addButton.addClickListener(e -> {
           if(searchComboBox.getValue() != null){
               ListDataProvider<T> all = (ListDataProvider<T>) ratingTable.getDataProvider();
               final boolean alreadyAdded = all.getItems().stream()
                       .anyMatch(lecture -> Objects.equals(lecture, searchComboBox.getValue()));
               if(!alreadyAdded){
                   List<T> allItems = all.getItems().stream().collect(Collectors.toList());
                   allItems.add(searchComboBox.getValue());
                   ratingTable.setItems(allItems);
               } else {
                   Notification.show(componentName + " already added to table!", 3000, Notification.Position.MIDDLE);
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
                ListDataProvider<T> all = (ListDataProvider<T>) ratingTable.getDataProvider();
                final List<T> afterDeletion = all.getItems().stream()
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

    public void updateSearchComboBox(Set<T> values){
        searchComboBox.setItems(values);
    }

}
