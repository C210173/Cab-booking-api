package com.sky.booking.service;

import com.sky.booking.config.JwtUtil;
import com.sky.booking.exception.UserException;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import com.sky.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public User getReqUserProfile(String token) throws UserException {
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        if (user!=null){
            return user;
        }
        throw new UserException("invalid token ...");
    }

    @Override
    public User findUserById(Integer Id) throws UserException {
        Optional<User> opt = userRepository.findById(Id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new UserException("User not found with id "+Id);
    }

    @Override
    public List<Ride> completedRides(Integer userId) throws UserException {
        List<Ride> completedRides = userRepository.getCompletedRides(userId);
        if (completedRides!=null){
            return completedRides;
        }
        throw new UserException("completedRides is null");
    }
}
