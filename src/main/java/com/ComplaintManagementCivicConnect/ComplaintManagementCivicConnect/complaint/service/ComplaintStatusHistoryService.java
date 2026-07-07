package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.ComplaintStatusHistory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplaintStatusHistoryService {

    private final ComplaintStatusHistoryRepository repository;

    public void record(Complaint  complaint,
                       ComplaintStatus status,
                       String note,
                       String changedByName,
                       String changeByRole
                       ){


       ComplaintStatusHistory history =  ComplaintStatusHistory.builder()
                .complaint(complaint)
                .status(status)
                .note(note)
                .changedByName(changedByName)
                .changedByRole(changeByRole)
                .build();

       repository.save(history);


        log.info("[TIMELINE] {} → {} | {} | by {}",
                complaint.getComplaintNumber(),
                status, note, changedByName);



    }

    public void recordSubmitted(Complaint complaint, String citizenName){

        record(complaint, ComplaintStatus.PENDING_REVIEW,
                "ComplaintSubmitted",
                citizenName,
                "CITIZEN");
    }

    public void recordApproved(Complaint complaint,
                               String adminName) {
        record(complaint,
                ComplaintStatus.APPROVED,
                "Approved by admin",
                adminName, "ADMIN");
    }

    public void recordAssigned(Complaint complaint,
                               String adminName,
                               String workerName) {
        record(complaint,
                ComplaintStatus.ASSIGNED,
                "Assigned to " + workerName,
                adminName, "ADMIN");
    }

    public void recordCompleted(Complaint complaint,
                                String workerName) {
        record(complaint,
                ComplaintStatus.COMPLETED,
                "Issue resolved",
                workerName, "WORKER");
    }

    public void recordRejected(Complaint complaint,
                               String adminName,
                               String reason) {
        record(complaint,
                ComplaintStatus.REJECTED,
                "Rejected: " + reason,
                adminName, "ADMIN");
    }
}
