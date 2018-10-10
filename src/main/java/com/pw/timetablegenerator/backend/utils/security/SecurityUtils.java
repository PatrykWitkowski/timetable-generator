package com.pw.timetablegenerator.backend.utils.security;

import com.pw.timetablegenerator.backend.dts.UserDts;
import com.pw.timetablegenerator.backend.entity.User;
import com.vaadin.flow.component.UI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SecurityUtils {

    private static Map<String, UserDts> authenticatedUsers = new HashMap<>();

    private SecurityUtils() { }

    public static boolean isAccessGranted(String sessionToken){
        return Optional.ofNullable(authenticatedUsers.get(sessionToken))
                .map(UserDts::isAuthorized)
                .orElse(false);
    }

    public static void addAuthenticatedUser(String token, UserDts user){
        final Optional<UserDts> userAlreadyLogedIn
                = authenticatedUsers.values().stream()
                .filter(Objects::nonNull)
                .filter(u -> Objects.equals(user.getUser(), u.getUser()))
                .findFirst();

        if(noOtherUserLoged(userAlreadyLogedIn) || userNotAuthorized(userAlreadyLogedIn)){
            authenticatedUsers.put(token, user);
        }
    }

    private static boolean noOtherUserLoged(Optional<UserDts> userAlreadyLogedIn) {
        return !userAlreadyLogedIn.isPresent();
    }

    private static boolean userNotAuthorized(Optional<UserDts> userAlreadyLogedIn){
        return userAlreadyLogedIn.isPresent() && !userAlreadyLogedIn.get().isAuthorized();
    }

    public static void revokeAccess(String token){
        authenticatedUsers.remove(token);
    }

    public static UserDts getCurrentUser(){
        String token = UI.getCurrent().getSession().getCsrfToken();
        return Optional.ofNullable(authenticatedUsers.get(token)).orElse(null);
    }

    public static void updateCurrentUser(User refreshedUser){
        String token = UI.getCurrent().getSession().getCsrfToken();
        Optional.ofNullable(authenticatedUsers.get(token))
                .ifPresent(currentUser -> currentUser.setUser(refreshedUser));
    }
}
