package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatDto {

    private int year;
    private int month;
    private long count;
}
