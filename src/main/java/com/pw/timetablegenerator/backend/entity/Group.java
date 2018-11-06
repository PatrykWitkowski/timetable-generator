package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.GroupType;

import java.io.Serializable;

public interface Group extends Serializable {

    String getName();

    GroupType getType();
}
