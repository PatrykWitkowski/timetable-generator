package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.User;
import lombok.Data;

@Data
public class UserDts {

    private User user;

    private boolean isAuthorized;
}
