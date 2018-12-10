package com.pw.timetablegenerator.backend.utils.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
public class RomanNumberTest {

    @Test
    public void shouldReturnRomanBelow10(){
        List<String> result = IntStream.range(1, 10).mapToObj(RomanNumber::toRoman).collect(Collectors.toList());

        assertThat(result, hasItems("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"));
    }

    @Test
    public void shouldReturnRomanBelow100(){
        List<String> result = IntStream.range(50, 60).mapToObj(RomanNumber::toRoman).collect(Collectors.toList());

        assertThat(result, hasItems("L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX"));
    }

    @Test
    public void shouldReturnRomanBelow1000(){
        List<String> result = IntStream.range(100, 110).mapToObj(RomanNumber::toRoman).collect(Collectors.toList());

        assertThat(result, hasItems("C", "CI", "CII", "CIII", "CIV", "CV", "CVI", "CVIII", "CVIII",
                "CIX"));
    }

    @Test
    public void shouldReturnRomanAbove1000(){
        List<String> result = IntStream.range(1000, 1010).mapToObj(RomanNumber::toRoman).collect(Collectors.toList());

        assertThat(result, hasItems("M", "MI", "MII", "MIII", "MIV", "MV", "MVI", "MVII", "MVIII", "MIX"));
    }

    @Test
    public void shouldReturnNumber(){
        final Long result = RomanNumber.toNumber("MDXLIII");

        assertThat(result, is(1543L));
    }
}
