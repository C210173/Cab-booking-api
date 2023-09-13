package com.sky.booking.controller;

import com.sky.booking.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<MessageResponse> homeController(){
        MessageResponse msg = new MessageResponse("Welcome Booking cab Backend System");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
