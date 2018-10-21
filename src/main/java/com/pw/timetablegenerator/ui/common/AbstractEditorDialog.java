/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.pw.timetablegenerator.ui.common;

import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract base class for dialogs adding, editing or deleting items.
 *
 * Subclasses are expected to
 * <ul>
 * <li>add, during construction, the needed UI components to
 * {@link #getFormLayout()} and bind them using {@link #getBinder()}, as well
 * as</li>
 * <li>override {@link #confirmDelete()} to open the confirmation dialog with
 * the desired message (by calling
 * {@link #openConfirmationDialog(String, String, String)}.</li>
 * </ul>
 *
 * @param <T>
 *            the type of the item to be added, edited or deleted
 */
public abstract class AbstractEditorDialog<T extends Serializable>
        extends Dialog {

    public static final String HAS_PADDING_CLASS_NAME = "has-padding";

    /**
     * The operations supported by this dialog. Delete is enabled when editing
     * an already existing item.
     */
    public enum Operation {
        ADD(App_.NEW, StringUtils.lowerCase(App_.ADD), false),
        EDIT(App_.EDIT, StringUtils.lowerCase(App_.EDIT), true);

        private final String nameInTitle;
        private final String nameInText;
        private final boolean deleteEnabled;

        Operation(String nameInTitle, String nameInText,
                boolean deleteEnabled) {
            this.nameInTitle = nameInTitle;
            this.nameInText = nameInText;
            this.deleteEnabled = deleteEnabled;
        }

        public String getNameInTitle() {
            return nameInTitle;
        }

        public String getNameInText() {
            return nameInText;
        }

        public boolean isDeleteEnabled() {
            return deleteEnabled;
        }
    }

    private final H3 titleField = new H3();
    private final Button saveButton = new Button(getTranslation(App_.SAVE));
    private final Button cancelButton = new Button(getTranslation(App_.CANCEL));
    private final Button deleteButton = new Button(getTranslation(App_.DELETE));
    private Registration registrationForSave;

    private final FormLayout formLayout = new FormLayout();
    private final HorizontalLayout buttonBar = new HorizontalLayout(saveButton,
            cancelButton);

    private Binder<T> binder = new Binder<>();
    private T currentItem;

    private final ConfirmationDialog<T> confirmationDialog = new ConfirmationDialog<>();

    private String itemType;
    private final BiConsumer<T, Operation> itemSaver;
    private final Consumer<T> itemDeleter;

    private Tabs tabs = new Tabs();
    private Div pages = new Div();
    private Map<Tab, Component> tabsToPages = new HashMap<>();
    private Tab mainTab;

    /**
     * Constructs a new instance.
     *
     * @param itemType
     *            The readable name of the item type
     * @param itemSaver
     *            Callback to save the edited item
     * @param itemDeleter
     *            Callback to delete the edited item
     */
    protected AbstractEditorDialog(String itemType,
                                   BiConsumer<T, Operation> itemSaver, Consumer<T> itemDeleter) {
        this.itemType = itemType;
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;

        initTitle();
        initFormLayout();
        initButtonBar();
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
    }

    protected void setItemType(String itemType){
        this.itemType = itemType;
    }

    private void initTitle() {
        add(titleField);
    }

    private void initFormLayout() {
        mainTab = new Tab(getTranslation(App_.MAIN_TAB));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2));
        Div mainDiv = new Div(formLayout);
        mainDiv.addClassName(HAS_PADDING_CLASS_NAME);
        tabsToPages.put(mainTab, mainDiv);
        Set<Component> pagesShown = Stream.of(mainDiv)
                .collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });
        tabs.add(mainTab);
        pages.add(mainDiv);
        add(tabs);
        add(pages);
    }

    private void initButtonBar() {
        saveButton.setAutofocus(true);
        saveButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> cancelClicked());
        if(itemDeleter != null){
            buttonBar.add(deleteButton);
            deleteButton.addClickListener(e -> deleteClicked());
            deleteButton.getElement().setAttribute("theme", "error");
        }
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        add(buttonBar);
    }

    /**
     * Adds a new tab to an editor dialog.
     *
     * @param tabName The name to display on a new tab
     * @param tabContent The content to display on a new tab
     */
    protected Tab addNewTab(String tabName, Div tabContent){
        Tab newTab = new Tab(tabName);
        tabs.add(newTab);
        tabContent.setVisible(false);
        tabContent.addClassName(HAS_PADDING_CLASS_NAME);
        pages.add(tabContent);
        tabsToPages.put(newTab, tabContent);
        return newTab;
    }

    /**
     * Update a tab.
     *
     * @param tab The tab to update
     * @param tabContent The content to display on a tab
     */
    protected void updateTab(Tab tab, Div tabContent){
        tabs.setSelectedTab(mainTab);
        tabContent.setVisible(false);
        tabContent.addClassName(HAS_PADDING_CLASS_NAME);
        pages.remove(tabsToPages.get(tab));
        pages.add(tabContent);
        tabsToPages.replace(tab, tabContent);
    }

    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected final FormLayout getFormLayout() {
        return formLayout;
    }

    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected final Binder<T> getBinder() {
        return binder;
    }

    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected final T getCurrentItem() {
        return currentItem;
    }

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item
     *            The item to edit; it may be an existing or a newly created
     *            instance
     * @param operation
     *            The operation being performed on the item
     */
    public void open(T item, Operation operation) {
        currentItem = item;
        titleField.setText(getTranslation(operation.getNameInTitle()) + " " + itemType);
        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton
                .addClickListener(e -> saveClicked(operation));
        binder.readBean(currentItem);

        deleteButton.setEnabled(operation.isDeleteEnabled());

        afterDialogOpen();

        open();
    }

    /**
     * Method runs after dialog is opened.
     */
    protected abstract void afterDialogOpen();

    protected void saveClicked(Operation operation) {
        boolean isValid = binder.writeBeanIfValid(currentItem);

        if (isValid) {
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            binder.validate();
        }
    }

    protected void cancelClicked(){
        close();
    }

    protected void deleteClicked() {
        if (confirmationDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(confirmationDialog));
        }
        confirmDelete();
    }

    protected abstract void confirmDelete();

    /**
     * Opens the confirmation dialog before deleting the current item.
     *
     * The dialog will display the given title and message(s), then call
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     *
     * @param title
     *            The title text
     * @param message
     *            Detail message (optional, may be empty)
     * @param additionalMessage
     *            Additional message (optional, may be empty)
     */
    protected final void openConfirmationDialog(String title, String message,
            String additionalMessage) {
        close();
        confirmationDialog.open(title, message, additionalMessage, getTranslation(App_.DELETE),
                true, getCurrentItem(), this::deleteConfirmed, this::open);
    }

    /**
     * Removes the {@code item} from the backend and close the dialog.
     *
     * @param item
     *            the item to delete
     */
    protected void doDelete(T item) {
        itemDeleter.accept(item);
        close();
    }

    private void deleteConfirmed(T item) {
        doDelete(item);
    }
}
