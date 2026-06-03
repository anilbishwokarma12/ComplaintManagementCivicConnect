package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

    @Size(min =2, max =100, message = "Name must be 2-100 characters")
    String fullName,

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    String phoneNumber,

    String profileImageUrl,

    @Size(max = 300, message = "Bio must be under 300 characters")
    String bio,

    String address

)
{

}