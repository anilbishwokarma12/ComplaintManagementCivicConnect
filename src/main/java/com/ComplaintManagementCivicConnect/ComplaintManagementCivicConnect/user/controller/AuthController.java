package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.controller;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.response.ApiResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.*;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServices authServices;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authServices.register(request)));
    }
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(ApiResponse.success(authServices.verifyEmail(token)));
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(
            @Valid @RequestBody ForgotPasswordRequest request) {
        // reuses ForgotPasswordRequest — just needs an email field
        return ResponseEntity.ok(
                ApiResponse.success(authServices.resendVerification(request.getEmail())));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", authServices.login(request)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        String response = authServices.forgotPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authServices.resetPassword(request)));
    }
}
