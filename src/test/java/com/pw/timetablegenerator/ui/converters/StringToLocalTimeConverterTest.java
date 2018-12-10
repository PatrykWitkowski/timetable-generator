package com.pw.timetablegenerator.ui.converters;

import com.vaadin.flow.data.binder.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
public class StringToLocalTimeConverterTest {

    private static final String ERROR = "error";
    private StringToLocalTimeConverter stringToLocalTimeConverter = new StringToLocalTimeConverter(ERROR);

    @Test
    public void shouldConvertToLocalTimeWhenFormatHMM(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("9:30", null);

        assertThat(result.isError(), is(false));
        result.ifOk(r -> assertThat(r, is(LocalTime.of(9, 30))));
    }

    @Test
    public void shouldConvertToLocalTimeWhenFormatHHMM(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("09:30", null);

        assertThat(result.isError(), is(false));
        result.ifOk(r -> assertThat(r, is(LocalTime.of(9, 30))));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatHH(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("09", null);

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatH(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("9", null);

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatHWithoutMinutes(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("9:", null);

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatHHMM(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("0930", null);

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatHMM(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("930", null);

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotConvertToLocalTimeWhenFormatHM(){
        final Result<LocalTime> result = stringToLocalTimeConverter.convertToModel("9:3", null);

        assertThat(result.isError(), is(true));
    }
}
