package com.pw.timetablegenerator.backend.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String groupCode;

    private LocalTime courseStartTime;

    private LocalTime courseEndTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 9)
    private DayOfWeek courseDay;

    private String coursesPlace;

    private String lecturer;

    private Boolean evenWeek;

    private Long freePlaces;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="class_id")
    private Class classOwner;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="timetable_id")
    private Class timetable;
}
