package com.pw.timetablegenerator.ui.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private String errorMsg;

    public StringToLocalTimeConverter(String errorMsg){
        this.errorMsg = errorMsg;
    }

    @Override
    public Result<LocalTime> convertToModel(String s, ValueContext valueContext) {
        try {
            if(s.matches("[0-9]:[0-5][0-9]")){
                s = "0" + s;
            }
            final LocalTime localTime = LocalTime.parse(s, DateTimeFormatter.ISO_TIME);
            return Result.ok(localTime);
        } catch (DateTimeParseException e) {
            return Result.error(errorMsg);
        }
    }

    @Override
    public String convertToPresentation(LocalTime o, ValueContext valueContext) {
        return o != null ? o.toString() : "";
    }
}
