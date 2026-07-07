package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ComplaintImageDTO {

    private UUID id;
    private String imageUrl;
    private LocalDateTime uploadedAT;
}
