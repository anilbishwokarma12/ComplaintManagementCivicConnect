package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.NotificationType;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;


    @Column(nullable = false)
    private String  title;


    @Column(nullable = false)
    private String  message;


    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean read = false;

    private UUID complaintId;


    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}
