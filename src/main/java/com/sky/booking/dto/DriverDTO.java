package com.sky.booking.dto;

import com.sky.booking.domain.UserRole;
import com.sky.booking.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Integer id;
    private String name;
    private String email;
    private String mobile;
    private double rating;
    private double latitude;
    private double longitude;
    private UserRole role;
    private Vehicle vehicle;
}
