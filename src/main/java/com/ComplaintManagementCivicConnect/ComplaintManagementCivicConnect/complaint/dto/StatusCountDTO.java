package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusCountDTO {

    private long all;
    private long pendingReview;
    private long approved;
    private long assigned;
    private long inProgress;
    private long completed;
    private long rejected;

}


