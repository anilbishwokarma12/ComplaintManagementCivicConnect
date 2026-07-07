package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StatusHistoryDTO {
    private UUID id;
    private ComplaintStatus status;
    private String note;
    private String changedByName;
    private String changedByRole;
    private LocalDateTime changedAt;

}
