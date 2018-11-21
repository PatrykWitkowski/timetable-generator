package com.pw.timetablegenerator.backend.utils.security;

import com.pw.timetablegenerator.backend.dts.UserDts;
import com.pw.timetablegenerator.backend.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class SecurityUtilsTest {

    private static final String TOKEN = "3244-c34c-43v4-c42c";
    public static final String ADMIN_USERNAME = "admin";
    public static final String USER_NAME = "user";
    private UserDts user;

    @Before
    public void init(){
        user = new UserDts();
        user.setUser(User.builder().username(ADMIN_USERNAME).build());
        user.setAuthorized(true);
        SecurityUtils.addAuthenticatedUser(TOKEN, user);
    }

    @Test
    public void shouldRevokeAccess(){
        SecurityUtils.revokeAccess(TOKEN);

        assertThat(SecurityUtils.isAccessGranted(TOKEN), is(false));
    }

    @Test
    public void shouldNoGainAccessWhenOtherUserLogged(){
        String otherSessionToken = "5345-df34-fd34-dr34";
        SecurityUtils.addAuthenticatedUser(otherSessionToken , user);

        final boolean result = SecurityUtils.isAccessGranted(otherSessionToken);

        assertThat(result, is(false));
    }

    @Test
    public void shouldNoGainAccessWhenUserNotAuthorized(){
        UserDts notAuthorizedUser = new UserDts();
        notAuthorizedUser.setUser(User.builder().username(USER_NAME).build());
        notAuthorizedUser.setAuthorized(false);
        String otherSessionToken = "5345-df66-fd34-6gfd";
        SecurityUtils.addAuthenticatedUser(otherSessionToken, notAuthorizedUser);

        final boolean result = SecurityUtils.isAccessGranted(otherSessionToken);

        assertThat(result, is(false));
    }

    @Test
    public void shouldGainAccessWhenUserAuthorized(){
        final boolean result = SecurityUtils.isAccessGranted(TOKEN);

        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnNullWhenUserNotLoggedIn(){
        mockUI("invalid-token");

        final UserDts result = SecurityUtils.getCurrentUser();

        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldReturnUserWhenUserLoggedIn(){
        mockUI(TOKEN);

        final UserDts result = SecurityUtils.getCurrentUser();

        assertThat(result.isAuthorized(), is(true));
        assertThat(result.getUser(), is(notNullValue()));
        assertThat(result.getUser().getUsername(), is(ADMIN_USERNAME));
    }

    @Test
    public void shouldRefreshUser(){
        final User user = User.builder().username(USER_NAME).build();
        mockUI(TOKEN);

        SecurityUtils.updateCurrentUser(user);

        final UserDts result = SecurityUtils.getCurrentUser();
        assertThat(result, is(notNullValue()));
        assertThat(result.getUser().getUsername(), is(USER_NAME));
    }

    private void mockUI(String csrfToken) {
        UI ui = mock(UI.class);
        VaadinSession session = mock(VaadinSession.class);
        when(ui.getSession()).thenReturn(session);
        when(session.getCsrfToken()).thenReturn(csrfToken);
        UI.setCurrent(ui);
    }
}
