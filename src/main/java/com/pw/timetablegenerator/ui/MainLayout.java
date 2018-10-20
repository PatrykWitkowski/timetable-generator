package com.pw.timetablegenerator.ui;

import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Student_;
import com.pw.timetablegenerator.backend.entity.properties.Timetable_;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.components.ClickableRouterLink;
import com.pw.timetablegenerator.ui.views.LoginView;
import com.pw.timetablegenerator.ui.views.classeslist.ClassesList;
import com.pw.timetablegenerator.ui.views.studentlist.StudentList;
import com.pw.timetablegenerator.ui.views.timetableslist.TimetablesList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.VaadinService;

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends Div
        implements RouterLayout, PageConfigurator, BeforeEnterObserver, LocaleChangeObserver {

    public static final String MAIN_LAYOUT_NAV_ITEM = "main-layout__nav-item";

    private Text timetableText = new Text(getTranslation(Timetable_.TIMETABLE_TITLE));
    private Text classText = new Text(getTranslation(Class_.CLASS_TITLE));
    private Text studentText = new Text(getTranslation(Student_.STUDENT_TITLE));
    private Text logoutText = new Text(getTranslation(App_.LOGOUT));
    private H2 title = new H2();

    public MainLayout() {
        title.addClassName("main-layout__title");

        RouterLink timetables = new RouterLink(null, TimetablesList.class);
        timetables.add(new Icon(VaadinIcon.CALENDAR_CLOCK), timetableText);
        timetables.addClassName(MAIN_LAYOUT_NAV_ITEM);
        // Only show as active for the exact URL, but not for sub paths
        timetables.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink classes = new RouterLink(null, ClassesList.class);
        classes.add(new Icon(VaadinIcon.ARCHIVES), classText);
        classes.addClassName(MAIN_LAYOUT_NAV_ITEM);
        classes.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink student = new RouterLink(null, StudentList.class);
        student.add(new Icon(VaadinIcon.ACADEMY_CAP), studentText);
        student.addClassName(MAIN_LAYOUT_NAV_ITEM);
        student.setHighlightCondition(HighlightConditions.sameLocation());

        ClickableRouterLink logout = new ClickableRouterLink(null, LoginView.class);
        logout.add(new Icon(VaadinIcon.POWER_OFF), logoutText);
        logout.addClassName(MAIN_LAYOUT_NAV_ITEM);
        logout.addClickListener(e -> onLogout());

        Div navigation = new Div(timetables, classes, student, logout);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");

        finishSession();
    }

    private void finishSession() {
        VaadinService.getCurrent().addSessionDestroyListener((SessionDestroyEvent sessionDestroyEvent) ->
                SecurityUtils.revokeAccess(sessionDestroyEvent.getSession().getCsrfToken()));
    }

    private void onLogout() {
        getUI().get().getSession().close();
        getUI().get().getPage().reload();
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!SecurityUtils.isAccessGranted(beforeEnterEvent.getUI().getSession().getCsrfToken())){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        title.setText(getTranslation(App_.APP_TITLE));
        timetableText.setText(getTranslation(Timetable_.TIMETABLE_TITLE));
        classText.setText(getTranslation(Class_.CLASS_TITLE));
        studentText.setText(getTranslation(Student_.STUDENT_TITLE));
        logoutText.setText(getTranslation(App_.LOGOUT));
    }
}
