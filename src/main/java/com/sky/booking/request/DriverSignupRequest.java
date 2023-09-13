package com.sky.booking.request;

import com.sky.booking.model.License;
import com.sky.booking.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverSignupRequest {
    private String name;
    private String email;
    private String password;
    private String mobile;
    private double latitude;
    private double longitude;
    private License license;
    private Vehicle vehicle;
}
