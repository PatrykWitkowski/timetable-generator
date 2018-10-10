package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserRepository extends JpaRepository<User,Long>, Serializable {

    User findByUsername(String userName);
}
