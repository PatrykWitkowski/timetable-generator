package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollment_groups")
@Data
@EqualsAndHashCode(exclude = {"owner", "classes"})
@Builder
@AllArgsConstructor
public class EnrollmentGroup implements Group, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Long ectsSum;

    private Long semester;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "Enrollment_Class",
            joinColumns = { @JoinColumn(name = "enrollment_id") },
            inverseJoinColumns = { @JoinColumn(name = "class_id") }
    )
    private List<Class> classes;

    public EnrollmentGroup(){
        this.classes = new ArrayList<>();
        this.name = "";
        this.ectsSum = 30L;
        this.semester = 1L;
    }

    public EnrollmentGroup(User user) {
        this();
        this.owner = user;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public GroupType getType() {
        return GroupType.ENROLLMENT_GROUP;
    }
}
