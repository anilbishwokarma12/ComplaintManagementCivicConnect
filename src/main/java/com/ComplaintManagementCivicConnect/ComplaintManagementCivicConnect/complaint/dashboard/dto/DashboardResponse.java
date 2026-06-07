package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

    private ComplaintCountsDto counts;

    private List<RecentComplaintDto> recentComplaint;

    private List<MonthlyStatDto> monthlyStats;
}
