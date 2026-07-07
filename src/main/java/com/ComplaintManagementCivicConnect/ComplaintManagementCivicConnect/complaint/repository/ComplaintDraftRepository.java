package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.ComplaintDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ComplaintDraftRepository extends JpaRepository<ComplaintDraft, UUID> {

    Optional<ComplaintDraft> findByIdAndUserId(
      UUID id, UUID userId);

    @Modifying
    @Query("DELETE FROM ComplaintDraft d " +
            "WHERE d.consumed = false " +
            "AND d.createdAt < :cutoff")
    int deleteStaleDrafts(
            @Param("cutoff") LocalDateTime cutoff);

}
