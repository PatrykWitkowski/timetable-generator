package com.pw.timetablegenerator.ui.views.studentlist;

import com.pw.timetablegenerator.backend.entity.properties.Student_;
import com.pw.timetablegenerator.backend.localization.TranslationProvider;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Locale;

@Route(value = "student", layout = MainLayout.class)
@PageTitle("Student")
public class StudentList extends VerticalLayout implements LocaleChangeObserver, BeforeEnterObserver {

    private H2 changeLanguageTitle = new H2();
    private ComboBox<Locale> locales = new ComboBox<>();

    public StudentList(){
        createChangeLanguage();
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
            locales.setValue(getLocale());
            locales.setItemLabelGenerator(g -> g.getDisplayLanguage(getLocale()));
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        changeLanguageTitle.setText(getTranslation(Student_.CHANGE_LANGUAGE_TITLE));
        locales.setLabel(getTranslation(Student_.CHANGE_LANGUAGE));
    }
}
