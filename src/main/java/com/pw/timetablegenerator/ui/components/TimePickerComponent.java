package com.pw.timetablegenerator.ui.components;

import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Tag("time-picker-component")
public class TimePickerComponent extends Component implements HasComponents {

    private static final String PATTERN = "([0-1]?[0-9]|2[0-3]):?[0-5][0-9]";
    private TextField timePicker = new TextField();

    public TimePickerComponent(String timePickerLabel){
        timePicker.setLabel(timePickerLabel);
        timePicker.setRequired(true);
        timePicker.setPattern(PATTERN);
        timePicker.addBlurListener(e -> {
            if(e.getSource().getValue().matches(PATTERN)){
                if(!StringUtils.contains(e.getSource().getValue(), ":")){
                    if(e.getSource().getValue().length() == 3){
                        final String time = String.format("%s:%s",
                                e.getSource().getValue().substring(0, 1),
                                e.getSource().getValue().substring(1, 3));
                        timePicker.setValue(time);
                    } else {
                        final String time = String.format("%s:%s",
                                e.getSource().getValue().substring(0, 2),
                                e.getSource().getValue().substring(2, 4));
                        timePicker.setValue(time);
                    }
                }
            }
        });

        timePicker.setErrorMessage(getTranslation(Course_.MSG_INVALID_TIME_FORMAT));
        timePicker.setSizeFull();
        add(timePicker);
    }

    public TextField getTextField(){
        return timePicker;
    }

    public LocalTime getTime(){
        String time = timePicker.getValue();
        if(time.matches("[0-9]:[0-5][0-9]")){
            time = "0" + time;
        }
        return LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
    }

}
