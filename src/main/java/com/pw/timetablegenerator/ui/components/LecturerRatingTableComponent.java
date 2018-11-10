package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.dts.LecturerPreferenceDts;
import com.pw.timetablegenerator.backend.dts.PreferenceDts;
import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.entity.properties.Lecturer_;
import com.vaadin.flow.component.ItemLabelGenerator;

import java.util.ArrayList;
import java.util.List;

public class LecturerRatingTableComponent extends RatingTableComponent<Lecturer> {

    public LecturerRatingTableComponent() {
        super();
        setComponentName(getTranslation(Lecturer_.LECTURER));
    }

    @Override
    protected void addColumns() {
        getRatingTable().addColumn(Lecturer::getName).setHeader(getTranslation(Lecturer_.LECTURER));
    }


    @Override
    protected ItemLabelGenerator<Lecturer> setItemLabelGenerator() {
        return (ItemLabelGenerator<Lecturer>) lecturer -> lecturer.getName();
    }

    @Override
    public List<PreferenceDts> getPreferences(){
        List<PreferenceDts> lecturerPreferencesDts = new ArrayList<>();
        getRatingStarsComponents().keySet().forEach(lecturer -> {
            LecturerPreferenceDts lecturerPreferenceDts = new LecturerPreferenceDts(lecturer,
                    getRatingStarsComponents().get(lecturer).getStarValue());
            lecturerPreferencesDts.add(lecturerPreferenceDts);
        });

        return lecturerPreferencesDts;
    }
}
