package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.GroupType;
import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@ToString(exclude = {"classOwner", "timetables"})
public class Course implements Serializable, Group {

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

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private ParityOfTheWeek parityOfTheWeek;

    private Long freePlaces;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="class_id")
    private Class classOwner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "courses")
    private List<Timetable> timetables;

    @Override
    public String getName() {
        return getGroupCode();
    }

    @Override
    public GroupType getType() {
        return GroupType.COURSE;
    }

    public Course(){
        this.timetables = new ArrayList<>();
    }
}
