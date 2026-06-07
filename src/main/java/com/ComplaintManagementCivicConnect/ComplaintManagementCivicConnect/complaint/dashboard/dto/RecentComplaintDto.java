package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentComplaintDto {
    private UUID id;
    private String complaintNumber;
    private String title;
    private ComplaintCategory complaintCategory;
    private ComplaintPriority complaintPriority;
    private ComplaintStatus complaintStatus;
    private LocalDateTime createdAt;
}
