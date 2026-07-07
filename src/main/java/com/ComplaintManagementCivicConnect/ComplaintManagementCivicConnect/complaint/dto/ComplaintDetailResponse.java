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
public class ComplaintDetailResponse {

    private UUID id;
    private String complaintNumber;
    private String  title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;
    private ComplaintStatus status;
    private boolean emergency;
    private String emergencyReason;

    private String address;
    private Double latitude;
    private Double longitude;


    private List<ComplaintImageDTO> images;

    private Double aiConfidence;
    private Boolean aiValidate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String qrTrackingUrl;

    private List<StatusHistoryDTO> statusTimeLine;
}
