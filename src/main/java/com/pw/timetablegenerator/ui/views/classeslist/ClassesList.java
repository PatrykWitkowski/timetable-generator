package com.pw.timetablegenerator.ui.views.classeslist;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.Group;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Group_;
import com.pw.timetablegenerator.backend.service.EnrollmentGroupService;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.common.AbstractList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Classes")
public class ClassesList extends AbstractList implements BeforeEnterObserver {

    @Autowired
    private EnrollmentGroupService enrollmentGroupService;

    private Grid<Group> grid;

    private final GroupEditorDialog form = new GroupEditorDialog(
            this::saveGroup, this::deleteGroup);

    protected ClassesList() {
        super(Class_.CLASS);
    }

    @Override
    protected void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
        grid = new Grid<>();

        grid.addColumn(Group::getName).setHeader(getTranslation(Group_.NAME));
        grid.addColumn(Group::getType).setHeader(getTranslation(Group_.TYPE));
        grid.addColumn(new ComponentRenderer<>(this::createEditButton))
                .setFlexGrow(0);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        container.add(getHeader(), grid);
        add(container);
    }

    private Button createEditButton(Group group) {
        Button edit = new Button(getTranslation(App_.EDIT), event -> form.open(group,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }

    @Override
    protected void openEditorDialog() {

    }

    @Override
    protected void updateView() {
        final List<EnrollmentGroup> enrollmentGroups = enrollmentGroupService.findEnrollmentGroups(SecurityUtils.getCurrentUser().getUser(), getSearchField().getValue());
        final List<Group> groups = enrollmentGroups.stream()
                .map(e -> (Group) e)
                .collect(Collectors.toList());
        grid.setItems(groups);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateView();
        }
    }

    private void saveGroup(Group group,
                            AbstractEditorDialog.Operation operation){

    }

    private void deleteGroup(Group group) {

    }
}
