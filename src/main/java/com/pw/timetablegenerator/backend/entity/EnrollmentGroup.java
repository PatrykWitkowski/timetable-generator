package com.pw.timetablegenerator.backend.entity;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "enrollment_groups")
@Data
public class EnrollmentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Integer ectsSum;

    private Integer semester;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="enrollment_group")
    private List<Class> classes;
}
