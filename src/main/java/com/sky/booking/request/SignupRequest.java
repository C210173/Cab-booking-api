package com.sky.booking.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Email is Required")
    @Email(message = "Email should be valid")
    private String email;
    private String fullName;
    private String password;
    private String mobile;
}
