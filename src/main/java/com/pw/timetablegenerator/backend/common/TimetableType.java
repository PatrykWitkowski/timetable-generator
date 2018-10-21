package com.pw.timetablegenerator.backend.common;

import com.vaadin.flow.component.UI;
import org.apache.commons.lang3.StringUtils;

public enum TimetableType {
    INVALID, BAD, GOOD, EXCELLENT;

    private static final String INVALID_PL = "NIEPOPRAWNY";
    private static final String BAD_PL = "ZŁY";
    private static final String GOOD_PL = "DOBRY";
    private static final String EXCELLENT_PL = "DOSKONAŁY";

    @Override
    public String toString() {
        final String language = UI.getCurrent().getLocale().getLanguage();
        if(StringUtils.equalsIgnoreCase(language, "en")){
            return name();
        } else if(StringUtils.equalsIgnoreCase(language, "pl")){
            switch (this){
                case INVALID:
                    return INVALID_PL;
                case BAD:
                    return BAD_PL;
                case GOOD:
                    return GOOD_PL;
                case EXCELLENT:
                    return EXCELLENT_PL;
            }
        }
        return name();
    }
}
