package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.controller;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.*;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service.ComplaintService;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/citizen/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping(
            value = "/analyze-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<AnalyzeImageResponse>>
    analyzeImage(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @RequestParam("image")
            MultipartFile image) {

        AnalyzeImageResponse response =
                complaintService.analyzeImage(
                        userDetails.getUsername(), image);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Image analyzed successfully",
                        response));
    }


    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<ComplaintResponse>>
    submitComplaint(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @Valid @RequestBody
            SubmitComplaintRequest request) {

        ComplaintResponse response =
                complaintService.submitComplaint(
                        userDetails.getUsername(), request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Complaint submitted successfully",
                        response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<ComplaintDetailResponse>>
    getComplaintById(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Complaint fetched successfully",
                        complaintService.getComplaintById(
                                userDetails.getUsername(),
                                id)));
    }


    @GetMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<MyComplaintsResponse>>
    getMyComplaints(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @RequestParam(required = false)
            ComplaintStatus status,
            @RequestParam(required = false)
            ComplaintCategory category,
            @RequestParam(required = false)
            ComplaintPriority priority,
            @RequestParam(required = false)
            String search,
            @RequestParam(defaultValue = "newest")
            String sort,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size) {

        ComplaintFilterRequest filter =
                new ComplaintFilterRequest();
        filter.setComplaintStatus(status);
        filter.setCategory(category);
        filter.setPriority(priority);
        filter.setSearch(search);
        filter.setSort(sort);
        filter.setPage(page);
        filter.setSize(size);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Complaints fetched successfully",
                        complaintService.getMyComplaint(
                                userDetails.getUsername(),
                                filter)));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<StatusCountDTO>>
    getMyComplaintStats(
            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Stats fetched successfully",
                        complaintService.getMyComplaintStats(
                                userDetails.getUsername())));
    }
}


