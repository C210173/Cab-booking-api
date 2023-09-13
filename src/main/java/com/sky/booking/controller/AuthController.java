package com.sky.booking.controller;

import com.sky.booking.config.JwtUtil;
import com.sky.booking.domain.UserRole;
import com.sky.booking.exception.UserException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.User;
import com.sky.booking.repository.DriverRepository;
import com.sky.booking.repository.UserRepository;
import com.sky.booking.request.DriverSignupRequest;
import com.sky.booking.request.LoginRequest;
import com.sky.booking.request.SignupRequest;
import com.sky.booking.response.JwtResponse;
import com.sky.booking.service.CustomerUserDetailsService;
import com.sky.booking.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final DriverService driverService;

    @PostMapping("/user/signup")
    public ResponseEntity<JwtResponse> signUpHandler(@RequestBody SignupRequest req) throws UserException {
        User user = userRepository.findByEmail(req.getEmail());
        if (user!=null){
            throw new UserException("User Already Exist with email "+req.getEmail());
        }
        User createUser = new User();
        createUser.setEmail(req.getEmail());
        createUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createUser.setFullName(req.getFullName());
        createUser.setMobile(req.getMobile());
        createUser.setRole(UserRole.USER);

        User savedUser = userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        JwtResponse res = new JwtResponse();
        res.setJwt(jwt);
        res.setAuthenticated(true);
        res.setError(false);
        res.setErrorDetails(null);
        res.setType(UserRole.USER);
        res.setMessage("Account Created Successfully: " + savedUser.getFullName());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signInHandler(@RequestBody LoginRequest req){
        Authentication authentication = authenticate(req.getPassword(), req.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        JwtResponse res = new JwtResponse();
        res.setJwt(jwt);
        res.setAuthenticated(true);
        res.setError(false);
        res.setErrorDetails(null);
        res.setType(UserRole.USER);
        res.setMessage("Account Login Successfully ");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/driver/signup")
    public ResponseEntity<JwtResponse> driverSignUpHandler(@RequestBody DriverSignupRequest req){
        Driver driver = driverRepository.findByEmail(req.getEmail());
        JwtResponse jwtResponse = new JwtResponse();
        if (driver!=null){
            jwtResponse.setAuthenticated(false);
            jwtResponse.setErrorDetails("Email already used another account");
            jwtResponse.setError(true);

            return new ResponseEntity<>(jwtResponse, HttpStatus.BAD_REQUEST);
        }

        Driver createDriver = driverService.registerDriver(req);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createDriver.getEmail(), createDriver.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Account Created Successfully: " + createDriver.getName());

        return new ResponseEntity<>(jwtResponse, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String password, String username){
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("invalid username or password from authenticated method");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }
}
