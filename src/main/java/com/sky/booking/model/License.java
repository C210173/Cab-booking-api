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
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String licenseNumber;
    private String licenseExpirationDate;
    private String licenseState;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;
}
