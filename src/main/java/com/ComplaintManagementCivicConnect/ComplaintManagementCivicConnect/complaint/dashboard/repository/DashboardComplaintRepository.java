package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.repository;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.RecentComplaintDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardComplaintRepository extends JpaRepository<Complaint, UUID> {

    long countByUserId(UUID userID);
    long countByUserIdAndStatus(UUID userId, ComplaintStatus status);

    // FIXED: Changed 'RecentComplaintDTO' to 'RecentComplaintDto' and 'c.category' to 'c.complaintCategory'
    @Query("""
        SELECT new com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.RecentComplaintDto(
            c.id, c.complaintNumber, c.title,
            c.complaintCategory, c.priority, c.status, c.createdAt)
        FROM Complaint c
        WHERE c.user.id = :userId
        ORDER BY c.createdAt DESC
        """)
    List<RecentComplaintDto> findTop5ByUserIdOrderByCreatedAtDesc(
            @Param("userId") UUID userId
    );

    // FIXED: Return Object[] instead of MonthlyStatDto
    @Query("SELECT YEAR(c.createdAt), MONTH(c.createdAt), COUNT(c) FROM Complaint c WHERE c.user.id = :userId AND c.createdAt >= :fromDate GROUP BY YEAR(c.createdAt), MONTH(c.createdAt) ORDER BY YEAR(c.createdAt) DESC, MONTH(c.createdAt) DESC")
    List<Object[]> findMonthlyStats(
            @Param("userId") UUID userId,
            @Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT COUNT(c) FROM Complaint c")
    long countAllComplaints();

    Optional<Complaint> findByComplaintNumber(String complaintNumber);
    List<Complaint> findAllByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}