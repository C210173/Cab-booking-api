package com.sky.booking.service;

import com.sky.booking.model.Driver;
import com.sky.booking.model.User;
import com.sky.booking.repository.DriverRepository;
import com.sky.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> authorities = new ArrayList<>();

        User user = userRepository.findByEmail(username);
        if (user!=null){
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }

        Driver driver = driverRepository.findByEmail(username);
        if (driver!=null){
            return new org.springframework.security.core.userdetails.User(driver.getEmail(), driver.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("user not found with email : " + username);
    }
}
