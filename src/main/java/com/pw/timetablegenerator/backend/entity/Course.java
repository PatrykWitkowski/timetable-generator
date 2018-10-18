package com.pw.timetablegenerator.backend.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@ToString(exclude = {"classOwner", "timetables"})
public class Course implements Serializable {

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

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    private Boolean evenWeek;

    private Long freePlaces;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="class_id")
    private Class classOwner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "courses")
    private List<Timetable> timetables;
}
