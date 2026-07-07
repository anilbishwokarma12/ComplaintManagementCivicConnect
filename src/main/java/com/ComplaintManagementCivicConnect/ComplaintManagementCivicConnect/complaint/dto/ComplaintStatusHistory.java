package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "complaint_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;


    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private String changedByName;


    @Column(nullable = false)
    private String changedByRole;


    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime changedAt = LocalDateTime.now();


}
