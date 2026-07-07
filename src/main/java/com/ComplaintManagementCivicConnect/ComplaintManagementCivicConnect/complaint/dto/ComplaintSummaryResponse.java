package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.location.Location;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ComplaintSummaryResponse {
    private UUID id;
    private String complaintNumber;
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;
    private ComplaintStatus status;
    private boolean emergency;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer progressPercent;

    private String thumbnailUrl;






}
