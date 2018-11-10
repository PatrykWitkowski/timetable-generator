package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.dts.LecturerPreferenceDts;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Tag("rating-table-component")
public abstract class RatingTableComponent<T extends Serializable> extends Component
        implements Serializable, HasComponents {

    private ComboBox<T> searchComboBox = new ComboBox<>();
    private Button addButton = new Button(VaadinIcon.PLUS.create());
    private Button deleteButton = new Button(VaadinIcon.MINUS.create());
    private Grid<T> ratingTable = new Grid<>();
    private String componentName;
    @Getter
    private Map<T, RatingStarsComponent> ratingStarsComponents = new HashMap<>();

    public RatingTableComponent(){
        initSearchComboBox();
        initRatingTable();
    }

    protected void setComponentName(String componentName){
        this.componentName = componentName;
        searchComboBox.setLabel(this.componentName);
    }

    private void initRatingTable() {
        addColumns();
        ratingTable.addComponentColumn(l -> {
            final RatingStarsComponent ratingStarsComponent = new RatingStarsComponent();
            ratingStarsComponents.put(l, ratingStarsComponent);
            return ratingStarsComponent;
        }).setHeader(getTranslation(App_.PRIORITY));
        add(ratingTable);
    }

    protected abstract void addColumns();

    protected Grid<T> getRatingTable(){
        return ratingTable;
    }

    private void initSearchComboBox(){
        searchComboBox.setLabel(componentName);
        searchComboBox.setAllowCustomValue(false);
        searchComboBox.setItemLabelGenerator(setItemLabelGenerator());

        ratingTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        addButton.addClickListener(e -> {
           if(searchComboBox.getValue() != null){
               ListDataProvider<T> all = (ListDataProvider<T>) ratingTable.getDataProvider();
               final boolean alreadyAdded = all.getItems().stream()
                       .anyMatch(item -> Objects.equals(item, searchComboBox.getValue()));
               if(!alreadyAdded){
                   List<T> allItems = all.getItems().stream().collect(Collectors.toList());
                   allItems.add(searchComboBox.getValue());
                   ratingTable.setItems(allItems);
               } else {
                   Notification.show(componentName + " " + getTranslation(App_.MSG_ALREADY_ADDED), 3000, Notification.Position.MIDDLE);
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

    protected abstract ItemLabelGenerator<T> setItemLabelGenerator();

    public void updateSearchComboBox(Set<T> values){
        searchComboBox.setItems(values);
    }

    public boolean isEmpty(){
        ListDataProvider<T> all = (ListDataProvider<T>) ratingTable.getDataProvider();
        return all.getItems().isEmpty();
    }

    public abstract List<PreferenceDts> getPreferences();

}
