package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
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
public class CourseServiceTest {

    private static final String CLASS_1 = "class1";
    private static final String GROUP_CODE_1 = "group_code_1";
    private static final String GROUP_CODE_2 = "group_code_2";
    private static boolean setUpIsDone = false;
    private static final String TEST_USER = "test_user_2";
    private static User currentUser;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Before
    public void setup(){
        if (setUpIsDone) {
            return;
        }
        currentUser = User.builder().username(TEST_USER).password(TEST_USER).build();
        final Class class1 = Class.builder().name(CLASS_1).owner(currentUser).build();
        final Course course1 = Course.builder().groupCode(GROUP_CODE_1).classOwner(class1).build();
        final Course course2 = Course.builder().groupCode(GROUP_CODE_2).classOwner(class1).build();
        save(currentUser, class1, course1, course2);
        setUpIsDone = true;
    }

    @Test
    public void shouldFindAllCoursesForUser(){
        // when
        final List<Course> result = courseService.findCourses(currentUser, null);

        //then
        assertThat(result, hasSize(2));
        assertThat(result.stream().map(Course::getName).collect(Collectors.toList()),
                containsInAnyOrder(GROUP_CODE_1, GROUP_CODE_2));
    }

    @Test
    public void shouldFindCoursesByGroupCode(){
        // when
        final List<Course> result = courseService.findCourses(currentUser, GROUP_CODE_1);

        //then
        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Course::getName).collect(Collectors.toList()),
                containsInAnyOrder(GROUP_CODE_1));
    }

    private void save(User currentUser, Class class1, Course course1, Course course2) {
        userRepository.save(currentUser);
        classRepository.save(class1);
        courseService.saveCourse(course1);
        courseService.saveCourse(course2);
    }
}
