package com.pw.timetablegenerator.ui.views;

import com.pw.timetablegenerator.backend.dts.UserDts;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.User_;
import com.pw.timetablegenerator.backend.service.UserService;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.views.timetableslist.TimetablesList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver, LocaleChangeObserver {

    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private Button login = new Button();
    private String correctPassMsg;
    private String wrongPassMsg;
    private String noUserFoundMsg;

    @Autowired
    private UserService userService;

    public LoginView() {
        username.setValueChangeMode(ValueChangeMode.EAGER);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        login.addClickListener(e -> onLogin(username, password));
        login.setEnabled(false);
        username.addValueChangeListener(e -> {
            if (e.getValue().isEmpty() || password.getValue().isEmpty()){
                login.setEnabled(false);
        } else {
                login.setEnabled(true);
            }

        });
        password.addValueChangeListener(e -> {
            if (e.getValue().isEmpty() || username.getValue().isEmpty()){
                login.setEnabled(false);
            } else {
                login.setEnabled(true);
            }

        });

        HorizontalLayout loginData = new HorizontalLayout(username, password, login);
        loginData.setVerticalComponentAlignment(Alignment.END, login);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        add(loginData);
    }

    private void onLogin(TextField username, PasswordField password) {
        final UserDts authenticatedUser
                = userService.authenticate(username.getValue(), password.getValue(),
                getUI().get().getSession().getCsrfToken());
        if(authenticatedUser.getUser() != null){
            if(authenticatedUser.isAuthorized()){
                Notification.show(correctPassMsg);
                getUI().ifPresent(ui -> ui.navigate(TimetablesList.class));
            } else{
                Notification.show(wrongPassMsg);
            }
        } else{
            Notification.show(noUserFoundMsg);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.isAccessGranted(beforeEnterEvent.getUI().getSession().getCsrfToken())){
            beforeEnterEvent.rerouteTo(TimetablesList.class);
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        username.setLabel(getTranslation(User_.USERNAME));
        password.setLabel(getTranslation(User_.USER_PASSWORD));
        login.setText(getTranslation(App_.LOGIN));
        correctPassMsg = getTranslation(User_.MSG_CORRECT_PASSWORD);
        wrongPassMsg = getTranslation(User_.MSG_WRONG_PASSWORD);
        noUserFoundMsg = getTranslation(User_.MSG_NO_USER_FOUND);
    }
}
