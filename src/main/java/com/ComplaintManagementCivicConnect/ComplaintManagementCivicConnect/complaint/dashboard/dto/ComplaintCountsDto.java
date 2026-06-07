package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintCountsDto {

    private long total;
    private long pending;
    private long inProgress;
    private long completed;
    private long rejected;
}
