package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.common.TimetableType;
import com.pw.timetablegenerator.backend.entity.Class;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassServiceTest {

    private static final String CLASS_TEST_1 = "class_test_1";
    private static final String CLASS_TEST_2 = "class_test_2";
    private static boolean setUpIsDone = false;
    private static final String TEST_USER = "test_user_1";
    private static User currentUser;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup(){
        if (setUpIsDone) {
            return;
        }
        currentUser = User.builder().username(TEST_USER).password(TEST_USER).build();
        final Class class1 = Class.builder().name(CLASS_TEST_1).owner(currentUser).build();
        final Class class2 = Class.builder().name(CLASS_TEST_2).owner(currentUser).build();
        save(currentUser, class1, class2);
        setUpIsDone = true;
    }

    @Test
    public void shouldFindAllClassesForUser(){
        // when
        final List<Class> result = classService.findClasses(currentUser, null);

        //then
        assertThat(result, hasSize(2));
        assertThat(result.stream().map(Class::getName).collect(Collectors.toList()),
                containsInAnyOrder(CLASS_TEST_1, CLASS_TEST_2));
    }

    @Test
    public void shouldFindClassesByName(){
        // when
        final List<Class> result = classService.findClasses(currentUser, CLASS_TEST_1);

        //then
        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Class::getName).collect(Collectors.toList()),
                containsInAnyOrder(CLASS_TEST_1));
    }

    private void save(User currentUser, Class class1, Class class2) {
        userRepository.save(currentUser);
        classService.saveClass(class1);
        classService.saveClass(class2);
    }
}
