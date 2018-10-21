package com.pw.timetablegenerator.ui.encoders;

import com.pw.timetablegenerator.backend.common.TimetableType;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class TimetableTypeToStringEncoder implements ModelEncoder<TimetableType, String> {

    @Override
    public String encode(TimetableType timetableType) {
        if(timetableType == null){
            return null;
        }
        return timetableType.toString();
    }

    @Override
    public TimetableType decode(String s) {
        return TimetableType.valueOf(s);
    }
}
