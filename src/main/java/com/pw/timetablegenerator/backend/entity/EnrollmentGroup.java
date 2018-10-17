package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.utils.converter.RomanNumber;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "enrollment_groups")
@Data
@EqualsAndHashCode(exclude = {"owner", "classes"})
public class EnrollmentGroup {

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
    @OneToMany(mappedBy="enrollmentGroup")
    private List<Class> classes;

    @Override
    public String toString(){
        return String.format("%s (semester: %s)", name, RomanNumber.toRoman(semester.intValue()));
    }
}
