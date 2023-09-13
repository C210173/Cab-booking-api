package com.sky.booking.controller;

import com.sky.booking.dto.RideDTO;
import com.sky.booking.dto.mapper.DtoMapper;
import com.sky.booking.exception.DriverException;
import com.sky.booking.exception.RideException;
import com.sky.booking.exception.UserException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import com.sky.booking.request.RideRequest;
import com.sky.booking.request.StartRideRequest;
import com.sky.booking.response.MessageResponse;
import com.sky.booking.service.DriverService;
import com.sky.booking.service.RideService;
import com.sky.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final UserService userService;
    private final DriverService driverService;

    @PostMapping("/request")
    public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization")String jwt) throws UserException, DriverException {
        User user = userService.getReqUserProfile(jwt);
        Ride ride = rideService.requestRide(rideRequest, user);

        RideDTO rideDTO = DtoMapper.toRideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/accept")
    public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId) throws  RideException{
        rideService.acceptRide(rideId);
        MessageResponse res = new MessageResponse("Ride Accepted By Driver");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/decline")
    public ResponseEntity<MessageResponse> declineRideHandler(@PathVariable Integer rideId, @RequestHeader("Authorization")String jwt) throws RideException, DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        rideService.declineRide(rideId, driver.getId());

        MessageResponse res = new MessageResponse("Ride decline By Driver");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/start")
    public ResponseEntity<MessageResponse> rideStartHandler(@PathVariable Integer rideId, @RequestBody StartRideRequest req) throws RideException {
        rideService.startRide(rideId, req.getOtp());
        MessageResponse res = new MessageResponse("Ride is started");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<MessageResponse> rideCompleteHandler(@PathVariable Integer rideId) throws RideException {
        rideService.completeRide(rideId);
        MessageResponse res = new MessageResponse("Ride is Completed Thank you For Booking Cab");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideDTO> findRideByIdHandler(@PathVariable Integer rideId, @RequestHeader("Authorization")String jwt) throws UserException, RideException {
        User user = userService.getReqUserProfile(jwt);
        Ride ride = rideService.findRideById(rideId);

        RideDTO rideDTO = DtoMapper.toRideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
    }
}
