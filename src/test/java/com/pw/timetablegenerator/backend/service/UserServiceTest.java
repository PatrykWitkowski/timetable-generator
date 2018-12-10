package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.common.UserType;
import com.pw.timetablegenerator.backend.dts.UserDts;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.UserRepository;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    public static final String UNKNOWN_USERNAME = "unknown";
    public static final String CORRECT_PASSWORD = "correct";
    public static final String ADMIN_USERNAME = "admin";
    public static final String WRONG_PASSWORD = "wrong";
    public static final String TOKEN = "12ds-fsdf-4fds-ge9s";

    @Autowired
    private UserService testedService;

    @MockBean
    private UserRepository userRepositoryMock;

    private User admin;

    @Before
    public void init(){
        when(userRepositoryMock.findByUsername(UNKNOWN_USERNAME)).thenReturn(null);
        final String correctPass = BCrypt.hashpw(CORRECT_PASSWORD, BCrypt.gensalt());
        admin = User.builder().username(ADMIN_USERNAME).password(correctPass).type(UserType.ADMIN).build();
        when(userRepositoryMock.findByUsername(ADMIN_USERNAME)).thenReturn(admin);
    }

    @Test
    public void shouldNotAuthenticateWhenNoFoundUser(){
        final UserDts result = testedService.authenticate(UNKNOWN_USERNAME, UNKNOWN_USERNAME, TOKEN);

        assertThat(result.getUser(), is(nullValue()));
        assertThat(result.isAuthorized(), is(false));
        assertThat(SecurityUtils.isAccessGranted(TOKEN), is(false));
    }

    @Test
    public void shouldNotAuthenticateWhenBadPassword(){
        final UserDts result = testedService.authenticate(ADMIN_USERNAME, WRONG_PASSWORD, TOKEN);

        assertThat(result.getUser(), is(admin));
        assertThat(result.isAuthorized(), is(false));
        assertThat(SecurityUtils.isAccessGranted(TOKEN), is(false));
    }

    @Test
    public void shouldAuthenticateWhenCorrectPassword(){
        final UserDts result = testedService.authenticate(ADMIN_USERNAME, CORRECT_PASSWORD, TOKEN);

        assertThat(result.getUser(), is(admin));
        assertThat(result.isAuthorized(), is(true));
        assertThat(SecurityUtils.isAccessGranted(TOKEN), is(true));
    }

    @Test
    public void shouldChangePassword(){
        final String newPassword = "newPassword";
        testedService.authenticate(ADMIN_USERNAME, CORRECT_PASSWORD, TOKEN);
        mockUI(TOKEN);

        testedService.changePassword(newPassword);

        assertThat(BCrypt.checkpw(newPassword, admin.getPassword()), is(true));
    }

    private void mockUI(String csrfToken) {
        UI ui = mock(UI.class);
        VaadinSession session = mock(VaadinSession.class);
        when(ui.getSession()).thenReturn(session);
        when(session.getCsrfToken()).thenReturn(csrfToken);
        UI.setCurrent(ui);
    }
}
