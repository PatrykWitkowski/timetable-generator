package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.ClassType;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @NotBlank
    private String name;

    private Long ects;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ClassType classType;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private EnrollmentGroup enrollmentGroup;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="classOwner")
    private List<Course> courses;
}
