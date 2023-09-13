package com.sky.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.booking.domain.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fullName;
    private String email;
    @Column(unique = true)
    private String mobile;
    private String password;
    private String profilePicture;
    private UserRole role;


}
