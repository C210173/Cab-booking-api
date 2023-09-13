package com.sky.booking.dto.mapper;

import com.sky.booking.dto.DriverDTO;
import com.sky.booking.dto.RideDTO;
import com.sky.booking.dto.UserDTO;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import org.springframework.stereotype.Service;

@Service
public class DtoMapper {
    public static DriverDTO toDriverDTO(Driver driver){
        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setEmail(driver.getEmail());
        driverDTO.setId(driver.getId());
        driverDTO.setName(driver.getName());
        driverDTO.setMobile(driver.getMobile());
        driverDTO.setLatitude(driver.getLatitude());
        driverDTO.setLongitude(driver.getLongitude());
        driverDTO.setRating(driver.getRating());
        driverDTO.setRole(driver.getRole());
        driverDTO.setVehicle(driver.getVehicle());

        return driverDTO;
    }

    public static UserDTO toUserDTO(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setName(user.getFullName());
        userDTO.setMobile(user.getMobile());

        return userDTO;
    }

    public static RideDTO toRideDTO(Ride ride){
        DriverDTO driverDTO = toDriverDTO(ride.getDriver());
        UserDTO userDTO = toUserDTO(ride.getUser());

        RideDTO rideDTO = new RideDTO();
        rideDTO.setDestinationLatitude(ride.getDestinationLatitude());
        rideDTO.setDestinationLongitude(ride.getDestinationLongitude());
        rideDTO.setDistance(ride.getDistance());
        rideDTO.setDuration(ride.getDuration());
        rideDTO.setEndTime(ride.getEndTime());
        rideDTO.setFare(ride.getFare());
        rideDTO.setId(ride.getId());
        rideDTO.setPickupLatitude(ride.getPickupLatitude());
        rideDTO.setPickupLongitude(ride.getPickupLongitude());
        rideDTO.setStartTime(ride.getStartTime());
        rideDTO.setStatus(ride.getStatus());
        rideDTO.setPickupArea(ride.getPickupArea());
        rideDTO.setDestinationArea(ride.getDestinationArea());
        rideDTO.setPaymentDetails(ride.getPaymentDetails());
        rideDTO.setOtp(ride.getOtp());
        rideDTO.setDriver(driverDTO);
        rideDTO.setUser(userDTO);

        return rideDTO;
    }

}
