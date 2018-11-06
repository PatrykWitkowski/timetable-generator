package com.pw.timetablegenerator.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "lecturers")
@Data
@NoArgsConstructor
public class Lecturer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lecturerId;

    @NotBlank
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="lecturer")
    private List<Course> courses;

    @Override
    public String toString(){
        return this.name;
    }

    public Lecturer(String name){
        this.name = name;
    }
}
