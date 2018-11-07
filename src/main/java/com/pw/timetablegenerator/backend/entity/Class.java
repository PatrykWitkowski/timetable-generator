package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.ClassType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@EqualsAndHashCode(exclude = {"owner", "enrollmentGroups", "courses"})
public class Class implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @NotBlank
    private String name;

    private Long ects;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ClassType classType;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "classes")
    private List<EnrollmentGroup> enrollmentGroups;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="classOwner")
    private List<Course> courses;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @Override
    public String toString(){
        return String.format("%s (%s)", name, classType);
    }

    public Class(){
        this.enrollmentGroups = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public Class(User owner){
        this();
        this.owner = owner;
        this.name = StringUtils.EMPTY;
        this.classType = ClassType.LECTURE;
        this.ects = 5L;
    }

    public void dismissChild(Course child) {
        this.courses.remove(child);
    }
}
