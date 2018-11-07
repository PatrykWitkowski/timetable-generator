package com.pw.timetablegenerator.ui.views.groupslist;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Tag("courses-manager")
public class CoursesManager extends Component implements HasComponents {

    private Class currentClass;

    private Grid<Course> courses = new Grid<>();
    private FormLayout detailsForm = new FormLayout();
    private TextField courseCode = new TextField(getTranslation(Course_.GROUP_CODE));
    private TextField courseDay = new TextField(getTranslation(Class_.DAY));
    private TextField lecturer = new TextField(getTranslation(Lecturer_.LECTURER));
    private TextField location = new TextField(getTranslation(Course_.LOCALIZATION));
    private TextField startTime = new TextField(getTranslation(Course_.START_TIME));
    private TextField endTime = new TextField(getTranslation(Course_.END_TIME));
    private TextField freePlaces = new TextField(getTranslation(Course_.MAX_PLACES));
    private TextField numberOfTimetables = new TextField(getTranslation(Course_.USED_IN_TIMETABLES));

    private Button addCourse = new Button(new Icon(VaadinIcon.PLUS));
    private Button deleteCourse = new Button(new Icon(VaadinIcon.MINUS));
    private CourseEditorDialog courseEditorDialog;

    public CoursesManager(Class currentClass,
                          BiConsumer<Course, AbstractEditorDialog.Operation> itemSaver,
                          Consumer<Course> itemDeleter){
        this.currentClass = currentClass;

        createCoursesTable(currentClass);
        createDetails();
        createButtons(itemSaver, itemDeleter);

        HorizontalLayout buttonBar = new HorizontalLayout(addCourse, deleteCourse);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        buttonBar.setWidth("100%");

        courses.setWidth("45em");
        Div details = new Div();
        details.add(new H4(getTranslation(App_.DETAILS)));
        details.add(detailsForm);
        VerticalLayout rightSideBar = new VerticalLayout(details, buttonBar);
        rightSideBar.setWidth("40%");
        HorizontalLayout mainLayout = new HorizontalLayout(courses, rightSideBar);

        add(mainLayout);
    }

    private void createButtons(BiConsumer<Course, AbstractEditorDialog.Operation> itemSaver,
                               Consumer<Course> itemDeleter) {
        courseEditorDialog = new CourseEditorDialog(itemSaver, itemDeleter);
        addCourse.addClickListener(e -> {
            Course newCourse = new Course();
            newCourse.setClassOwner(currentClass);
            courseEditorDialog.open(newCourse, AbstractEditorDialog.Operation.ADD);
        });
        addCourse.setSizeFull();
        addCourse.getElement().setAttribute("theme", "primary");
        deleteCourse.setEnabled(false);
        courses.addSelectionListener(e -> deleteCourse.setEnabled(e.getFirstSelectedItem().isPresent()));
        deleteCourse.addClickListener(e -> {
            final List<Course> courses = currentClass.getCourses().stream()
                    .filter(item -> !this.courses.getSelectedItems().contains(item))
                    .collect(Collectors.toList());
            currentClass.setCourses(courses);
            this.courses.setItems(courses);
        });
        deleteCourse.setSizeFull();
        deleteCourse.getElement().setAttribute("theme", "error");
    }

    private void createDetails() {
        detailsForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("10em", 2));
        courseCode.setEnabled(false);
        detailsForm.add(courseCode);
        courseDay.setEnabled(false);
        detailsForm.add(courseDay);
        lecturer.setEnabled(false);
        detailsForm.add(lecturer);
        location.setEnabled(false);
        detailsForm.add(location);
        startTime.setEnabled(false);
        detailsForm.add(startTime);
        endTime.setEnabled(false);
        detailsForm.add(endTime);
        freePlaces.setEnabled(false);
        detailsForm.add(freePlaces);
        numberOfTimetables.setEnabled(false);
        detailsForm.add(numberOfTimetables);

        courses.addSelectionListener(e -> {
            final Optional<Course> firstSelectedItem = e.getFirstSelectedItem();
            if(firstSelectedItem.isPresent()){
                final Course course = firstSelectedItem.get();
                courseCode.setValue(course.getGroupCode());
                courseDay.setValue(course.getCourseDay()
                        .getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale()));
                lecturer.setValue(course.getLecturer().getName());
                location.setValue(course.getCoursesPlace());
                startTime.setValue(course.getCourseStartTime().toString());
                endTime.setValue(course.getCourseEndTime().toString());
                freePlaces.setValue(course.getFreePlaces().toString());
                numberOfTimetables.setValue(Integer.toString(course.getTimetables().size()));
            } else {
                courseCode.clear();
                courseDay.clear();
                lecturer.clear();
                location.clear();
                startTime.clear();
                endTime.clear();
                freePlaces.clear();
                numberOfTimetables.clear();
            }
        });
    }

    private void createCoursesTable(Class currentClass) {
        courses.addColumn(Course::getName).setHeader(getTranslation(Course_.GROUP_CODE));
        courses.addColumn(c -> c.getCourseDay().getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale()))
                .setHeader(getTranslation(Class_.DAY));
        courses.addColumn(c -> getTranslation(c.getParityOfTheWeek().getProperty())).setHeader(getTranslation(Class_.WEEK_PARITY));
        courses.addColumn(Course::getLecturer).setHeader(getTranslation(Lecturer_.LECTURER));
        courses.setItems(currentClass.getCourses());
        String style="width: 45em;touch-action: none;margin-top: 2em;";
        courses.getElement().setAttribute("style", style);
    }

    public void refreshCoursesTable(Course course){
        currentClass.getCourses().add(course);
        courses.setItems(currentClass.getCourses());
    }
}
