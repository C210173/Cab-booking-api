package com.sky.booking.service;


import com.sky.booking.exception.UserException;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;

import java.util.List;

public interface UserService {
    User getReqUserProfile(String token) throws UserException;
    User findUserById(Integer Id) throws UserException;
    List<Ride> completedRides(Integer userId) throws UserException;
}
