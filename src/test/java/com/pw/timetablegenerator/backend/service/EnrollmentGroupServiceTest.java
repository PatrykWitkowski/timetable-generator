package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
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
public class EnrollmentGroupServiceTest {

    private static final String ENROLLMENT_GROUP_1 = "enrollment_group_1";
    private static final String ENROLLMENT_GROUP_2 = "enrollment_group_2";
    private static boolean setUpIsDone = false;
    private static final String TEST_USER = "test_user_3";
    private static User currentUser;

    @Autowired
    private EnrollmentGroupService enrollmentGroupService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup(){
        if (setUpIsDone) {
            return;
        }
        currentUser = User.builder().username(TEST_USER).password(TEST_USER).build();
        final EnrollmentGroup enrollmentGroup1 = EnrollmentGroup.builder().name(ENROLLMENT_GROUP_1)
                .owner(currentUser).build();
        final EnrollmentGroup enrollmentGroup2 = EnrollmentGroup.builder().name(ENROLLMENT_GROUP_2)
                .owner(currentUser).build();
        save(currentUser, enrollmentGroup1, enrollmentGroup2);
        setUpIsDone = true;
    }

    @Test
    public void shouldFindAllEnrollmentGroupsForUser(){
        // when
        final List<EnrollmentGroup> result = enrollmentGroupService.findEnrollmentGroups(currentUser, null);

        //then
        assertThat(result, hasSize(2));
        assertThat(result.stream().map(EnrollmentGroup::getName).collect(Collectors.toList()),
                containsInAnyOrder(ENROLLMENT_GROUP_1, ENROLLMENT_GROUP_2));
    }

    @Test
    public void shouldFindEnrollmentGroupsByName(){
        // when
        final List<EnrollmentGroup> result = enrollmentGroupService.findEnrollmentGroups(currentUser, ENROLLMENT_GROUP_1);

        //then
        assertThat(result, hasSize(1));
        assertThat(result.stream().map(EnrollmentGroup::getName).collect(Collectors.toList()),
                containsInAnyOrder(ENROLLMENT_GROUP_1));
    }

    private void save(User currentUser, EnrollmentGroup enrollmentGroup1, EnrollmentGroup enrollmentGroup2) {
        userRepository.save(currentUser);
        enrollmentGroupService.saveEnrollmentGroup(enrollmentGroup1);
        enrollmentGroupService.saveEnrollmentGroup(enrollmentGroup2);
    }
}
