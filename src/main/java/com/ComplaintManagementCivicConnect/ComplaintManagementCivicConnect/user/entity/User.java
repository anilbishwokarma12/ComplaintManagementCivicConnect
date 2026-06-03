package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImageUrl;

//    @Pattern(
//            regexp = "^[0-9]{10}$",
//            message = "Phone number must be 10 digits"
//    )

    private String phoneNumber;

    private String bio;

    private String address;


    private boolean enabled = false;

    //-------Account Status--------

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean accountNonLocked = false;

    // ---- Email Verification----


    private String verificationToken;
     private LocalDateTime tokenExpiresAt;

     // ------  password reset

    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    // ---- TimeStamps----------

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;
    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void  onCreate(){
        createdAt = LocalDateTime.now();
        updatedAT = LocalDateTime.now();
    }

    @PreUpdate
    protected void  onUpdate(){
        updatedAT = LocalDateTime.now();
    }





}
