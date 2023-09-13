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
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private double rating;
    private double latitude;
    private double longitude;
    private UserRole role;
    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private License license;
    @JsonIgnore
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ride> rides;
    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vehicle vehicle;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Ride currentRide;
    private Integer totalRevenue = 0;
}
