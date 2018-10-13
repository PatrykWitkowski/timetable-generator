package com.pw.timetablegenerator.backend.entity;

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
    @Column(unique = true)
    private String name;

    private Integer ects;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="enrollment_id")
    private EnrollmentGroup enrollmentGroup;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="class_owner")
    private List<Course> courses;
}
