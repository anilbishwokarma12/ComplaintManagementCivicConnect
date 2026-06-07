package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.controller;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.DashboardResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.service.DashboardService;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.response.ApiResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citizen/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal UserPrinciple userPrinciple
            ){
        DashboardResponse dashboardResponse = dashboardService.getDashboard(userPrinciple);

        return ResponseEntity.ok(

                ApiResponse.success(
                        "Dashboard loaded successfully",

                           dashboardResponse
                )
                );

    }
}
