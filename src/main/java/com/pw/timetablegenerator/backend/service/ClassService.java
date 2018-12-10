package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.User;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public interface ClassService extends Serializable {

    List<Class> findClasses(@NonNull User user, String value);

    Class saveClass(@NonNull Class c);

    void deleteClass(@NonNull Class c);

}
