package com.sky.booking.service;

import com.sky.booking.exception.DriverException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.request.DriverSignupRequest;

import java.util.List;

public interface DriverService {
    Driver registerDriver(DriverSignupRequest driverSignupRequest);
    List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride);
    Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);
    Driver getReqDriverProfile(String jwt) throws DriverException;
    Ride getDriversCurrentRide(Integer driverId) throws DriverException;
    List<Ride> getAllocatedRides(Integer driverId) throws DriverException;
    Driver findDriverById(Integer driverId) throws DriverException;
    List<Ride> completedRides(Integer driverId) throws DriverException;
}
