package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.common.TimetableType;
import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimetableServiceTest {

    private static final String TEST_1 = "test_1";
    private static final String GOOD = "good";
    private static boolean setUpIsDone = false;
    private static final String TEST_USER = "test_user_4";
    private static final String TIMETABLE_1 = "timetable_1";
    private static final String TIMETABLE_2 = "timetable_2";
    private static User currentUser;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup(){
        if (setUpIsDone) {
            return;
        }
        currentUser = User.builder().username(TEST_USER).password(TEST_USER).build();
        final Timetable timetable1 = Timetable.builder().name(TIMETABLE_1).timetableType(TimetableType.BAD)
                .owner(currentUser).build();
        final Timetable timetable2 = Timetable.builder().name(TIMETABLE_2).timetableType(TimetableType.GOOD)
                .owner(currentUser).build();
        save(currentUser, timetable1, timetable2);
        setUpIsDone = true;
    }

    @Test
    public void shouldFindAllTimetablesForUser(){
        // when
        final List<Timetable> result = timetableService.findTimetables(currentUser, null);

        //then
        assertThat(result, hasSize(2));
        assertThat(result.stream().map(Timetable::getName).collect(Collectors.toList()),
                containsInAnyOrder(TIMETABLE_1, TIMETABLE_2));
    }

    @Test
    public void shouldFindTimetableByName(){
        // when
        final List<Timetable> result = timetableService.findTimetables(currentUser, TIMETABLE_1);

        //then
        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Timetable::getName).collect(Collectors.toList()),
                containsInAnyOrder(TIMETABLE_1));
    }

    @Test
    public void shouldFindTimetableByType(){
        // when
        final List<Timetable> result = timetableService.findTimetables(currentUser, GOOD);

        //then
        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Timetable::getName).collect(Collectors.toList()),
                containsInAnyOrder(TIMETABLE_2));
    }

    @Test
    public void shouldFindTimetableById(){
        // when
        final Timetable result = timetableService.findByTimetableId(1);

        //then
        assertThat(result.getName(), is(TEST_1));
    }

    private void save(User currentUser, Timetable timetable1, Timetable timetable2) {
        userRepository.save(currentUser);
        timetableService.save(timetable1);
        timetableService.save(timetable2);
    }
}
