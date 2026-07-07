package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least one Character")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must have uppercase, number, and special character"
    )
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;


    @Size(max = 300, message = "Bio must be under 300 characters")
    private String bio;

    private String address;


}
