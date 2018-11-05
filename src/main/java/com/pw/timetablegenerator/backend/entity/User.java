package com.pw.timetablegenerator.backend.entity;

import com.pw.timetablegenerator.backend.common.Gender;
import com.pw.timetablegenerator.backend.common.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private UserType type;

    private String firstName;

    private String lastName;

    private String indexNumber;

    private String personalNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    private LocalDate birthDate;

    private String email;

    private String address;

    private Long actualSemester;

    private Boolean enrollmentAccess;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="owner")
    private List<Timetable> timetables;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="owner")
    private List<EnrollmentGroup> enrollmentGroups;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="owner")
    private List<Class> ownerClasses;

    public User(String username, String password, UserType type){
        this.username = username;
        this.password = password;
        this.type = type;
    }
}
