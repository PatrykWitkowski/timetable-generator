package com.pw.timetablegenerator.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractList extends VerticalLayout {

    private final TextField searchField;
    private final H2 header;
    private String searchLabel;
    private String newLabel;

    protected AbstractList(String listName, String searchLabel, String newLabel){
        this.searchLabel = getTranslation(searchLabel);
        this.newLabel = getTranslation(newLabel);
        searchField = new TextField("", this.searchLabel);
        header = new H2(getTranslation(listName));

        initView();

        addSearchBar();
        addContent();
    }

    protected abstract void addContent();

    private void initView() {
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    protected void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        final IronIcon icon = new IronIcon("lumo", "plus");
        icon.getElement().setAttribute("slot", "prefix");
        Button newButton = new Button(newLabel, icon);
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> openEditorDialog());

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    protected abstract void openEditorDialog();

    protected abstract void updateView();
}
