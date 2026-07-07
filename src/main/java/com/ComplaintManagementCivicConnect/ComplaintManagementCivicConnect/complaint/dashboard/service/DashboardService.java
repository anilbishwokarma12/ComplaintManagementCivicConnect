package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.ComplaintCountsDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.DashboardResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.MonthlyStatDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.RecentComplaintDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.repository.DashboardComplaintRepository;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardComplaintRepository dashboardComplaintRepository;

    private final UserService userService;

public DashboardResponse getDashboard(UserPrinciple userPrinciple){
   User user =  userPrinciple.getUser();
   UUID userId = user.getId();

   ComplaintCountsDto counts = buildCounts(userId);

  List<RecentComplaintDto> recent =  dashboardComplaintRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);

    LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

    // FIXED: Handle Object[] returned from repository
    List<Object[]> monthlyStatsRaw = dashboardComplaintRepository.findMonthlyStats(userId, sixMonthsAgo);
    List<MonthlyStatDto> monthly = monthlyStatsRaw.stream()
            .map(row -> MonthlyStatDto.builder()
                    .year(((Number) row[0]).intValue())
                    .month(((Number) row[1]).intValue())
                    .count(((Number) row[2]).longValue())
                    .build())
            .collect(Collectors.toList());

    return DashboardResponse.builder()
            .counts(counts)
            .recentComplaint(recent)
            .monthlyStats(monthly)
            .build();

}

    private ComplaintCountsDto buildCounts(UUID userId){

    return  ComplaintCountsDto.builder()
            .total(dashboardComplaintRepository.countByUserId(userId))
            .pending(dashboardComplaintRepository.countByUserIdAndStatus(userId, ComplaintStatus.PENDING_REVIEW))
            .inProgress(dashboardComplaintRepository.countByUserIdAndStatus(userId, ComplaintStatus.IN_PROGRESS))
            .completed(dashboardComplaintRepository.countByUserIdAndStatus(userId, ComplaintStatus.COMPLETED))
            .rejected(dashboardComplaintRepository.countByUserIdAndStatus(userId, ComplaintStatus.REJECTED))
            .build();

    }
}
