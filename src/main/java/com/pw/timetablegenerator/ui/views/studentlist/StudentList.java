package com.pw.timetablegenerator.ui.views.studentlist;

import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.entity.properties.App_;
import com.pw.timetablegenerator.backend.entity.properties.Settings_;
import com.pw.timetablegenerator.backend.entity.properties.Student_;
import com.pw.timetablegenerator.backend.entity.properties.User_;
import com.pw.timetablegenerator.backend.localization.TranslationProvider;
import com.pw.timetablegenerator.backend.service.UserService;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Locale;
import java.util.Objects;

@Route(value = "student", layout = MainLayout.class)
@PageTitle("Student")
public class StudentList extends VerticalLayout implements LocaleChangeObserver, BeforeEnterObserver {

    private final static String NULL_DEFAULT = "------------";
    private H2 changeLanguageTitle = new H2();
    private ComboBox<Locale> locales = new ComboBox<>();
    private TextField studentFirstName = new TextField(getTranslation(User_.FIRST_NAME));
    private TextField studentLastName = new TextField(getTranslation(User_.LAST_NAME));
    private TextField studentPersonalNumber = new TextField(getTranslation(User_.PERSONAL_NUMBER));
    private TextField studentBirthDate = new TextField(getTranslation(User_.BIRTH_DATE));
    private TextField studentGender = new TextField(getTranslation(User_.GENDER));
    private TextField studentAddress = new TextField(getTranslation(User_.ADDRESS));
    private TextField studentEmail = new TextField(getTranslation(User_.EMAIL));
    private TextField studentIndex = new TextField(getTranslation(User_.INDEX));
    private TextField studentActualSemester = new TextField(getTranslation(User_.ACTUAL_SEMESTER));
    private TextField studentEnrollmentAccess = new TextField(getTranslation(User_.ENROLLMENT_ACCESS));
    private H2 studentInfoTitle = new H2();
    private User currentUser;

    public StudentList(){
    }

    private void createStudentInfo() {
        VerticalLayout studentInfoLayout = new VerticalLayout();
        studentInfoLayout.getStyle().set("border", "1px solid #9E9E9E");
        studentInfoLayout.setPadding(true);
        studentInfoLayout.setSizeFull();
        studentInfoLayout.setBoxSizing(BoxSizing.BORDER_BOX);
        studentInfoLayout.setEnabled(false);

        studentFirstName.setValue(Objects.toString(currentUser.getFirstName(), NULL_DEFAULT));
        studentLastName.setValue(Objects.toString(currentUser.getLastName(), NULL_DEFAULT));
        studentPersonalNumber.setValue(Objects.toString(currentUser.getPersonalNumber(), NULL_DEFAULT));

        HorizontalLayout firstRow = new HorizontalLayout(studentFirstName, studentLastName, studentPersonalNumber);

        studentBirthDate.setValue(Objects.toString(currentUser.getBirthDate(), NULL_DEFAULT));
        studentGender.setValue(Objects.toString(getTranslation(currentUser.getGender().getProperty()), NULL_DEFAULT));
        studentAddress.setValue(Objects.toString(currentUser.getAddress(), NULL_DEFAULT));
        studentEmail.setValue(Objects.toString(currentUser.getEmail(), NULL_DEFAULT));

        HorizontalLayout secondRow = new HorizontalLayout(studentBirthDate, studentGender, studentAddress, studentEmail);

        studentIndex.setValue(Objects.toString(currentUser.getIndexNumber(), NULL_DEFAULT));
        studentActualSemester.setValue(Objects.toString(currentUser.getActualSemester(), NULL_DEFAULT));
        studentEnrollmentAccess.setValue(Objects.toString(
                currentUser.getEnrollmentAccess() ? getTranslation(App_.YES) : getTranslation(App_.NO),
                NULL_DEFAULT));

        HorizontalLayout thirdRow = new HorizontalLayout(studentIndex, studentActualSemester, studentEnrollmentAccess);

        studentInfoLayout.add(firstRow, secondRow, thirdRow);
        VerticalLayout layout = new VerticalLayout(studentInfoTitle, studentInfoLayout);
        add(layout);
    }

    private void createChangeLanguage() {
        HorizontalLayout changeLanguage = new HorizontalLayout();
        changeLanguage.getStyle().set("border", "1px solid #9E9E9E");
        changeLanguage.setPadding(true);
        changeLanguage.setSizeFull();
        changeLanguage.setBoxSizing(BoxSizing.BORDER_BOX);

        locales.setItems(TranslationProvider.getLocales());
        locales.setAllowCustomValue(false);
        locales.setRequired(true);
        locales.setPreventInvalidInput(true);

        locales.addValueChangeListener(e -> {
            if(e.getValue() != null){
                if(getUI().isPresent()){
                    getUI().get().setLocale(e.getValue());
                    locales.setItemLabelGenerator(g -> g.getDisplayLanguage(getUI().get().getLocale()));
                }
            } else {
                locales.setValue(getUI().get().getLocale());
            }
        });

        changeLanguage.add(locales);
        VerticalLayout layout = new VerticalLayout(changeLanguageTitle, changeLanguage);
        add(layout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            currentUser = SecurityUtils.getCurrentUser().getUser();
            createStudentInfo();
            createChangeLanguage();
            locales.setValue(getLocale());
            locales.setItemLabelGenerator(g -> g.getDisplayLanguage(getLocale()));
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        changeLanguageTitle.setText(getTranslation(Student_.CHANGE_LANGUAGE_TITLE));
        studentInfoTitle.setText(getTranslation(User_.STUDENT_INFO));
        locales.setLabel(getTranslation(Student_.CHANGE_LANGUAGE));
        studentFirstName.setLabel(getTranslation(User_.FIRST_NAME));
        studentLastName.setLabel(getTranslation(User_.LAST_NAME));
        studentPersonalNumber.setLabel(getTranslation(User_.PERSONAL_NUMBER));
        studentBirthDate.setLabel(getTranslation(User_.BIRTH_DATE));
        studentGender.setLabel(getTranslation(User_.GENDER));
        studentAddress.setLabel(getTranslation(User_.ADDRESS));
        studentEmail.setLabel(getTranslation(User_.EMAIL));
        studentIndex.setLabel(getTranslation(User_.INDEX));
        studentActualSemester.setLabel(getTranslation(User_.ACTUAL_SEMESTER));
        studentEnrollmentAccess.setLabel(getTranslation(User_.ENROLLMENT_ACCESS));
        studentGender.setValue(Objects.toString(getTranslation(currentUser.getGender().getProperty()), NULL_DEFAULT));
        studentEnrollmentAccess.setValue(Objects.toString(
                currentUser.getEnrollmentAccess() ? getTranslation(App_.YES) : getTranslation(App_.NO),
                NULL_DEFAULT));
    }

}
