package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ComplaintResponse {
    private UUID id;
    private String complaintNumber;
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;
    private boolean emergency;
    private ComplaintStatus status;


    private String address;
    private Double latitude;
    private Double longitude;


    private List<ComplaintImageDTO> images;


    private String aiRawCategory;
    private Double aiConfidence;
    private Boolean aiValidated;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
