package
com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.controller;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.response.ApiResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.ChangePasswordRequest;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.UpdateProfileRequest;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.UserResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(
            @AuthenticationPrincipal UserPrinciple principal) {
        return ResponseEntity.ok(ApiResponse.success(
                userService.getProfile(principal)));
    }


    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserPrinciple principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Profile updated", userService.updateProfile(principal, request)));
    }
    @PostMapping("/profile-image")
    public ResponseEntity<UserResponse> uploadProfileImage(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestParam("file") MultipartFile file) {

        UserResponse response =
                userService.UploadProfileImage(userPrinciple, file);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<UserResponse>> removeProfileImage(
            @AuthenticationPrincipal UserPrinciple principal) {
        return ResponseEntity.ok(
                ApiResponse.success("Profile image removed",
                        userService.removeProfileImage(principal)));
    }
    // PUT /api/user/me/password
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @AuthenticationPrincipal UserPrinciple principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(userService.changePassword(principal, request)));
    }






}
