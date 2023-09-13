package com.sky.booking.controller;

import com.sky.booking.exception.UserException;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import com.sky.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer userId)throws UserException{
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization")String jwt) throws UserException {
        User user = userService.getReqUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRidesHandler(@RequestHeader("Authorization")String jwt)throws UserException{
        User user = userService.getReqUserProfile(jwt);
        List<Ride> rides = userService.completedRides(user.getId());
        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }
}
