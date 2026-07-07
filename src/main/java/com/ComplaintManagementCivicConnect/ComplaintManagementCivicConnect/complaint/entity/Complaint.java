package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.BaseEntity;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.ComplaintStatusHistory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.location.Location;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false, unique = true)
    private String complaintNumber;

    @Column(nullable = false)
    private String  title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintCategory complaintCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintPriority priority;

    @Column(nullable = false)
    @Builder.Default
    private boolean emergency = false;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ComplaintStatus status = ComplaintStatus.PENDING_REVIEW;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;


    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ComplaintImage> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    @OrderBy("changedAt ASC")
    private List<ComplaintStatusHistory> statusHistory =
            new ArrayList<>();





    //ai-data
    private String aiRawCategory;
    private Double aiConfidence;
    private Boolean aiValidate;

    // ── Location Intelligence ─────────────────────────────────────
    private String emergencyReason;

    @Column(columnDefinition = "TEXT")
    private String nearbyPlacesJson;


    public void addImage(ComplaintImage image){

        image.setComplaint(this);
        this.images.add(image);

    }
}
