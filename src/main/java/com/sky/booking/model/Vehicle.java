package com.sky.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String make;
    private String model;
    private int year;
    private String color;
    @Column(name = "license_plate")
    private String licensePlate;
    private int capacity;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;

}
