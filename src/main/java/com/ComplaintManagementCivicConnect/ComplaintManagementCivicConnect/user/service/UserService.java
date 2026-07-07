package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.exception.BadRequestException;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.ChangePasswordRequest;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.UpdateProfileRequest;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.dto.UserResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;


    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png", "image/webp");


    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB


    // ── Get my profile ───────────────────────────────────────────
    public UserResponse getProfile(UserPrinciple principle){
        return  UserResponse.from(principle.getUser());

    }

    //------Update profile(name, phone , address)
    @Transactional
    public UserResponse updateProfile(UserPrinciple principle, UpdateProfileRequest request){

        User user = principle.getUser();

        if (request.fullName() !=null && !request.fullName().isBlank())
            user.setFullName(request.fullName().trim());

        if(request.phoneNumber()!=null && !request.phoneNumber().isBlank())
            user.setPhoneNumber(request.phoneNumber().trim());
        if(request.bio() !=null)
            user.setBio(request.bio().trim());
        if (request.address() != null)
            user.setAddress(request.address().trim());

       User saved = userRepository.save(user);

        return UserResponse.from(saved);
    }


/// -upload/replace profile image-----
    public UserResponse UploadProfileImage(UserPrinciple userPrinciple, MultipartFile file){
        validateImage(file);

        User user = findUsers(userPrinciple);

        //Delete old image
        deleteOldImage(user.getProfileImageUrl());

     String imageUrl = storeImage(file, user.getId().toString());
      user.setProfileImageUrl(imageUrl);
      return UserResponse.from(userRepository.save(user));

    }


    //-----remove profile image

    public UserResponse removeProfileImage(UserPrinciple principle){

        User user = findUsers(principle);

        if (user.getProfileImageUrl() == null){
            throw  new BadRequestException("No profile image to remove");
        }

        deleteOldImage(user.getProfileImageUrl());
        user.setProfileImageUrl(null);
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    public String changePassword(UserPrinciple userPrinciple, ChangePasswordRequest req) {

        User user = findUsers(userPrinciple);

        // 1. check current password
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // 2. new vs confirm
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        // 3. new password must be different from OLD password
        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // 4. update
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }



    //------find Users------
    private User findUsers(UserPrinciple userPrinciple){

        return userRepository.findByEmail(userPrinciple.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

    }


    public String storeImage(MultipartFile file, String userId){

        try {
           Path dir =  Paths.get(uploadDir, "Profiles");
           Files.createDirectories(dir);
          String ext =  getExtension(file.getOriginalFilename());
         String filename =  userId+ "_"+ UUID.randomUUID()+"."+ext;
         Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
         return baseUrl+"/uploads/profiles"+filename;

        }
        catch (IOException e){
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }

    }

    public String getExtension(String fileName){
        if(fileName == null || !fileName.contains("."))return  "jpg";
        return  fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase();

    }

    //-------delete old image----

    private void deleteOldImage(String url){

        if(url == null || url.isBlank()) return;
        try {
            String relative = url.replace(baseUrl + "/", "");
            Files.deleteIfExists(Paths.get(uploadDir).resolve(relative));
        }
        catch (IOException ignored){}

    }

    public User findByEmailOrThrow(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    //---validate Image--------
    private void validateImage(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new BadRequestException("Profile image is required");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only JPG, PNG, and WEBP images are allowed");
        }

        if(file.getSize()>MAX_SIZE){
            throw new BadRequestException("File size must not exceed 5MB");
        }
    }

}
