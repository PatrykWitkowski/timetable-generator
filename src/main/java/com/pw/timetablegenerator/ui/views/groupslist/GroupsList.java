package com.pw.timetablegenerator.ui.views.groupslist;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.*;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.pw.timetablegenerator.backend.entity.properties.Group_;
import com.pw.timetablegenerator.backend.service.*;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.common.AbstractList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Classes")
public class GroupsList extends AbstractList implements BeforeEnterObserver {

    @Autowired
    private EnrollmentGroupService enrollmentGroupService;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private UserService userService;

    private Grid<Group> grid;

    private final GroupSelectorDialog selectorDialog
            = new GroupSelectorDialog(this::saveEnrollmentGroup, this::deleteEnrollmentGroup,
            this::saveCourse, this::deleteCourse, this::saveClass, this::deleteClass);

    protected GroupsList() {
        super(Group_.GROUPS, Group_.SEARCH, Group_.NEW);
    }

    @Override
    protected void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
        grid = new Grid<>();

        grid.addColumn(Group::getName, "name")
                .setHeader(getTranslation(Group_.NAME))
                .setWidth("50%");
        grid.addColumn(g -> getTranslation(g.getType().getProperty()), "type")
                .setHeader(getTranslation(Group_.TYPE))
                .setWidth("35%");
        grid.addColumn(new ComponentRenderer<>(this::createEditButton));
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        addSorting();

        container.add(getHeader(), grid);
        add(container);
    }

    private void addSorting() {
        grid.addSortListener(e -> {
            grid.getDataCommunicator()
                    .getBackEndSorting().stream()
                    .map(querySortOrder -> String.format(
                            "{sort property: %s, direction: %s}",
                            querySortOrder.getSorted(),
                            querySortOrder.getDirection()))
                    .collect(Collectors.joining(", "));
        });
    }

    private Button createEditButton(Group group) {
        Button edit = new Button(getTranslation(App_.EDIT),
                event -> selectorDialog.edit(group));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }

    @Override
    protected void openEditorDialog() {
        selectorDialog.open();
    }

    @Override
    protected void updateView() {
        final User currentUser = SecurityUtils.getCurrentUser().getUser();
        final List<EnrollmentGroup> enrollmentGroups
                = enrollmentGroupService.findEnrollmentGroups(currentUser, getSearchField().getValue());
        final List<Course> courses = courseService.findCourses(currentUser, getSearchField().getValue());
        final List<Class> classes = classService.findClasses(currentUser, getSearchField().getValue());

        final List<Group> groups = enrollmentGroups.stream()
                .map(e -> (Group) e)
                .collect(Collectors.toList());
        groups.addAll(courses);
        groups.addAll(classes);

        grid.setItems(groups);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateView();
        }
    }

    private void saveEnrollmentGroup(Group group,
                                     AbstractEditorDialog.Operation operation){
        selectorDialog.close();
        enrollmentGroupService.saveEnrollmentGroup((EnrollmentGroup) group);
        updateClasses((EnrollmentGroup) group);
        userService.refreshUserData();

        Notification.show(
                getTranslation(Class_.MSG_CLASS_ADDED_EDITED) + (operation == AbstractEditorDialog.Operation.ADD ? getTranslation(App_.ADDED) : getTranslation(App_.EDITED)) + ".",
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

    private void updateClasses(EnrollmentGroup enrollmentGroup) {
        final List<Class> deletedClassForEnrollment = enrollmentGroup.getClasses().stream()
                .filter(c -> !enrollmentGroup.getClasses().contains(c))
                .collect(Collectors.toList());
        enrollmentGroup.getClasses().forEach(c -> {
            c.getEnrollmentGroups().removeAll(deletedClassForEnrollment);
            c.getEnrollmentGroups().removeAll(deletedClassForEnrollment);
        });
        deletedClassForEnrollment.forEach(c -> classService.deleteClass(c));
        enrollmentGroup.getClasses().forEach(c -> classService.saveClass(c));
    }

    private void deleteEnrollmentGroup(Group group) {
        enrollmentGroupService.deleteEnrollmentGroup((EnrollmentGroup) group);
        userService.refreshUserData();

        Notification.show(getTranslation(Class_.MSG_SUCCESS) + getTranslation(Class_.MSG_CLASS_DELETED),
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

    private void saveCourse(Group group,
                            AbstractEditorDialog.Operation operation){
        selectorDialog.close();
        final Course course = (Course) group;
        final Lecturer lecturer = course.getLecturer();
        if(!lecturer.getCourses().contains(course)){
            lecturer.getCourses().add(course);
            lecturerService.saveLecturer(lecturer);
        }
        courseService.saveCourse(course);
        userService.refreshUserData();
        selectorDialog.refreshClassEditorDialog(course);

        Notification.show(
                getTranslation(Course_.MSG_COURSE_ADDED_EDITED) + (operation == AbstractEditorDialog.Operation.ADD ? getTranslation(App_.ADDED) : getTranslation(App_.EDITED)) + ".",
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteCourse(Group group){
        final Course course = (Course) group;
        courseService.deleteCourse(course);
        deleteLecturer(course);
        userService.refreshUserData();

        Notification.show(getTranslation(Course_.MSG_COURSE_SUCCESS) + getTranslation(Course_.MSG_COURSE_DELETED),
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteLecturer(Course course) {
        if(course.getLecturer().getCourses().isEmpty()){
            lecturerService.deleteLecturer(course.getLecturer());
        }
    }

    private void saveClass(Class c, AbstractEditorDialog.Operation operation){
        selectorDialog.close();
        classService.saveClass(c);
        userService.refreshUserData();

        Notification.show(
                getTranslation(Class_.MSG_CLASS_ADDED_EDITED) + (operation == AbstractEditorDialog.Operation.ADD ? getTranslation(App_.ADDED) : getTranslation(App_.EDITED)) + ".",
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteClass(Class c){
        classService.deleteClass(c);
        userService.refreshUserData();

        Notification.show(getTranslation(Class_.MSG_SUCCESS) + getTranslation(Class_.MSG_CLASS_DELETED),
                3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }

}
