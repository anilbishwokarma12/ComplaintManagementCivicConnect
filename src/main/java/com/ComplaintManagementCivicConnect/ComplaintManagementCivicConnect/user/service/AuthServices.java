package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.exception.BadRequestException;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.exception.ResourceNotFoundException;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.email.EmailServices;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.*;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.repository.UserRepository;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServices {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServices emailServices;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    //-----Register
    @Transactional
    public String register(RegisterRequest request){

        if (userRepository.existsByEmail(request.getEmail())){
            throw  new BadRequestException("Email is already registered");
        }

        String token = UUID.randomUUID().toString();
       User user =  User.builder()
                .fullName(request.getFullName().trim())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
               .address(request.getAddress())
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .enabled(false)
                .isActive(false)
               .verificationToken(token)
               .accountNonLocked(true)
               .tokenExpiresAt(LocalDateTime.now().plusHours(24))
                .build();


        userRepository.save(user);
      emailServices.sendVerificationEmail(user.getEmail(),  user.getFullName(), token);

        return "Registration successful ! Please check your email to verify account ";

    }

    //--verify email

    @Transactional
    public String verifyEmail(String token){

       User user =  userRepository.findByVerificationToken(token).orElseThrow(
               ()-> new BadRequestException("Invalid Verification token")
       );

       if (LocalDateTime.now().isAfter(user.getTokenExpiresAt())){
           throw new BadRequestException("Token expired. Please request a new verification email.");
       }
       user.setEnabled(true);
       user.setActive(true);
       user.setVerificationToken(token);
       user.setTokenExpiresAt(null);
       return "Email verified! You can now login in.";
    }

    //Resend Verification
    @Transactional
    public String resendVerification(String email){
       User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No Account with the email"));

        if(user.isEnabled()){
            throw new BadRequestException("Account is already verified");
        }

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiresAt(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailServices.sendVerificationEmail(user.getEmail(), user.getFullName(), token);
        return "Verification email resent. please check your inbox.";
    }


    @Transactional
    public AuthResponse login(LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));

        UserPrinciple principle =(UserPrinciple) authentication.getPrincipal();
       User user = principle.getUser();

       if(!user.isEnabled()){
           throw new DisabledException("Account not varified. Check your email.");
       }
       if(! user.isAccountNonLocked()){
           throw new LockedException("Account is locked. please contact support.");
       }

       user.setLastLoginAt(LocalDateTime.now());
       userRepository.save(user);

      String token =  jwtUtil.generateToken(principle);
      log.info("Login: {}",user.getEmail() );


        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name(),
                user.getFullName(),
                user.getProfileImageUrl()
        );


    }

    //Forgot Password
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request){
        userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .ifPresent(user -> {
                    String token = UUID.randomUUID().toString();
                    user.setResetToken(token);
                    user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
                    userRepository.save(user);

                    emailServices.sendPasswordResetEmail(user.getEmail(), user.getFullName(), token);

                });

        return "If this email is registered ,  a password reset link has been sent";

    }

    //  reset password

    @Transactional
    public String resetPassword(ResetPasswordRequest request){

        User user =  userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("invalid or expired reset link."));

        if(LocalDateTime.now().isAfter(user.getResetTokenExpiry())){
            throw new BadRequestException("Reset token expired. Please request a new one.");
        }
        user.setPassword(passwordEncoder.encode(request
                .getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        emailServices.sendPasswordChangedEmail(user.getEmail(), user.getFullName());
        return "Password reset successfully. You can now log in.";
    }


}
