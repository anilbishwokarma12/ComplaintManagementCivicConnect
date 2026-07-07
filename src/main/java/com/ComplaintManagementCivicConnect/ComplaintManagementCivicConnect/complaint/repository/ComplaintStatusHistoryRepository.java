package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.ComplaintStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplaintStatusHistoryRepository extends JpaRepository<ComplaintStatusHistory, UUID> {

    List<ComplaintStatusHistory> findAllByComplaintIdOrderByChangedAtAsc(UUID complaintId);

}
