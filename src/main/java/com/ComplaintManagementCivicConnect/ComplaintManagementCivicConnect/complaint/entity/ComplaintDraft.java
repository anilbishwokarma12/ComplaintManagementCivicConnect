package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "complaint_drafts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ComplaintDraft {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(nullable = false)
    private String imageUrl;

    private String imagePublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintCategory aiCategory;


    @Column(nullable = false)
    private Double aiConfidence;


    @Column(nullable = false)
    private Boolean aiValid;

    @Column(nullable = false)
    private String suggestedTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintPriority suggestedPriority;


    @Column(nullable = false)
    @Builder.Default
    private boolean consumed = false;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
