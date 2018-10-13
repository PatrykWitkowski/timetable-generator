package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.TimetableType;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "timetables")
@Data
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableId;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Long semester;

    private Long quality;

    private LocalDate generationDate;

    private TimetableType timetableType;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="timetable")
    private List<Course> courses;
}
