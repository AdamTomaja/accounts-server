package com.cydercode.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterAccountRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    @Size(min = 8, max = 30, message = "Confirm password must be between 8 and 20 characters")
    private String confirmPassword;
}
