package com.sky.booking.service;

import com.sky.booking.exception.DriverException;
import com.sky.booking.exception.RideException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import com.sky.booking.request.RideRequest;

public interface RideService {

    Ride requestRide(RideRequest rideRequest, User user) throws DriverException;
    Ride createRideRequest(User user, Driver nearesDriver, double pickupLatitude,
                           double pickupLongitude, double destinationLatitude, double destinationLongitude,
                           String pickupArea, String destinationArea);
    void acceptRide(Integer rideId) throws RideException;
    void declineRide(Integer rideId, Integer driverId) throws RideException;
    void startRide(Integer rideId, int otp) throws RideException;
    void completeRide(Integer rideId) throws RideException;
    void cancelRide(Integer rideId) throws RideException;
    Ride findRideById(Integer rideId) throws RideException;

}
