package com.sky.booking.controller;

import com.sky.booking.exception.DriverException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/profile")
    public ResponseEntity<Driver> getReqDriverProfileHandler(@RequestHeader("Authorization")String jwt) throws DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @GetMapping("/{driverId}/current_ride")
    public ResponseEntity<Ride> getDriversCurrentRideHandler(@PathVariable Integer driverId) throws DriverException{
        Ride ride = driverService.getDriversCurrentRide(driverId);
        return new ResponseEntity<>(ride, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{driverId}/allocated")
    public ResponseEntity<List<Ride>> getAllocatedRidesHandler(@PathVariable Integer driverId) throws DriverException {
        List<Ride> rides = driverService.getAllocatedRides(driverId);
        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }

    @GetMapping("rides/completed")
    public ResponseEntity<List<Ride>> getCompleteRidesHandler(@RequestHeader("Authorization")String jwt) throws DriverException{
        Driver driver = driverService.getReqDriverProfile(jwt);
        List<Ride> rides = driverService.completedRides(driver.getId());
        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }
}
