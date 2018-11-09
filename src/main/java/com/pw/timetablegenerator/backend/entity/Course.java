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
import java.util.Objects;

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private ParityOfTheWeek parityOfTheWeek;

    private Long freePlaces;

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
        this.freePlaces = 30L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @PreRemove
    public void prepareToRemove(){
        if(this.classOwner != null){
            this.classOwner.dismissChild(this);
        }
        if(this.timetables != null){
            this.timetables.stream().forEach(t -> t.getCourses().remove(this));
        }
        if(this.lecturer != null){
            this.lecturer.getCourses().remove(this);
        }
        this.setClassOwner(null);
        this.setLecturer(null);
        this.setTimetables(null);
    }
}
