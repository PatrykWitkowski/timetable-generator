package com.pw.timetablegenerator.backend.entity;

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

//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy="owner")
//    private List<Order> orders;
//
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy="owner")
//    private List<Product> products;
//
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy="owner")
//    private List<Seller> sellers;

    public User(String username, String password, UserType type){
        this.username = username;
        this.password = password;
        this.type = type;
    }
}
