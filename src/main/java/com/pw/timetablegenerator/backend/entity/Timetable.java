package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.TimetableType;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetables")
@Data
@EqualsAndHashCode(exclude="owner")
@Builder
@AllArgsConstructor
@ToString(exclude = "owner")
public class Timetable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableId;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Long semester;

    private LocalDate semesterStartDate;

    private LocalDate semesterEndDate;

    private double quality;

    private LocalDate generationDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TimetableType timetableType;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Timetable_Course",
            joinColumns = { @JoinColumn(name = "timetable_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    private List<Course> courses;

    public Timetable(){
        this.courses = new ArrayList<>();
    }

    public Timetable(User user) {
        this();
        this.owner = user;
    }
}
