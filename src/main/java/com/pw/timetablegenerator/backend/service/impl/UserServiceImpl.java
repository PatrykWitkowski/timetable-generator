package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.dts.UserDts;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.UserRepository;
import com.pw.timetablegenerator.backend.service.UserService;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDts authenticate(String userName, String password, String sessionToken) {
        final User user = userRepository.findByUsername(userName);
        UserDts userDts = new UserDts();
        userDts.setUser(user);

        Optional.ofNullable(user).ifPresent(u -> userDts.setAuthorized(BCrypt.checkpw(password, u.getPassword())));
        SecurityUtils.addAuthenticatedUser(sessionToken, userDts);

        return userDts;
    }

    @Override
    public void refreshUserData(){
        final UserDts currentUser = SecurityUtils.getCurrentUser();

        final User refreshedUser = userRepository.findByUsername(currentUser.getUser().getUsername());

        SecurityUtils.updateCurrentUser(refreshedUser);
    }

    @Override
    public void changePassword(String newPassword) {
        final User currentUser = SecurityUtils.getCurrentUser().getUser();
        currentUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepository.save(currentUser);
    }

}
