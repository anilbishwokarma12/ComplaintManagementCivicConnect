package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.Role;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;
public record UserResponse (

        UUID id,
        String fullName,
        String email,
        Role role,
        String phoneNumber,
        String profileImageUrl,
        String bio,
        String address,
        boolean enabled,
        boolean isActive,
        boolean accountNonLocked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogIn
){
    public static UserResponse from(User u){
        return new UserResponse(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getRole(),
                u.getPhoneNumber(),
                u.getProfileImageUrl(),
                u.getBio(),
                u.getAddress(),
                u.isEnabled(),
                u.isActive(),
                u.isAccountNonLocked(),
                u.getCreatedAt(),
                u.getUpdatedAT(),
                u.getLastLoginAt()


        );
    }

}
