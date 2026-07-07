package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.MonthlyStatDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.RecentComplaintDto;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID>, JpaSpecificationExecutor<Complaint> {

    long countByUserId(UUID userId);

    long countByUserIdAndStatus(UUID userId, ComplaintStatus status);

    // ✅ Add this method - find the highest complaint number
    @Query("SELECT MAX(c.complaintNumber) FROM Complaint c")
    Optional<String> findMaxComplaintNumber();

    // ✅ Add this method - check if complaint number exists
    boolean existsByComplaintNumber(String complaintNumber);

    @Query("""
        SELECT new com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.RecentComplaintDto(
            c.id, c.complaintNumber, c.title,
            c.complaintCategory, c.priority, c.status, c.createdAt)
        FROM Complaint c
        WHERE c.user.id = :userId
        ORDER BY c.createdAt DESC
        LIMIT 5
        """)
    List<RecentComplaintDto> findTop5ByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    @Query("""
        SELECT new com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dashboard.dto.MonthlyStatDto(
            YEAR(c.createdAt), MONTH(c.createdAt), COUNT(c))
        FROM Complaint c
        WHERE c.user.id = :userId
          AND c.createdAt >= :from
        GROUP BY YEAR(c.createdAt), MONTH(c.createdAt)
        ORDER BY YEAR(c.createdAt), MONTH(c.createdAt)
        """)
    List<MonthlyStatDto> findMonthlyStats(@Param("userId") UUID userId, @Param("from") LocalDateTime from);


    @Query("SELECT COUNT(c) FROM Complaint c")
    long countAllComplaints();

    Optional<Complaint> findByComplaintNumber(String complaintNumber);

    boolean existsByIdAndUserId(UUID id, UUID userId);


    // For status tab counts — all in one query
    @Query("""
        SELECT c.status, COUNT(c)
        FROM Complaint c
        WHERE c.user.id = :userId
        GROUP BY c.status
        """)
    List<Object[]> countGroupedByStatus(
            @Param("userId") UUID userId);
}