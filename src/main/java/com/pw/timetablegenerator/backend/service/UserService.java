package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.dts.UserDts;
import lombok.NonNull;

import java.io.Serializable;

public interface UserService extends Serializable {

    UserDts authenticate(@NonNull String userName, @NonNull String password, @NonNull String sessionToken);

    void refreshUserData();

    void changePassword(@NonNull String newPassword);

}
