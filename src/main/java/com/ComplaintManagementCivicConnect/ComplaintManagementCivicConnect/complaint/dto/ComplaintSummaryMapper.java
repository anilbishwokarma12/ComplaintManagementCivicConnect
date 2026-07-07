package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import org.springframework.stereotype.Component;

@Component
public class ComplaintSummaryMapper {

    private static final int DESC_PREVIEW_LENGTH = 120;
    public ComplaintSummaryResponse toSummary(Complaint complaint){

        return ComplaintSummaryResponse.builder()
                .id(complaint.getId())
                .complaintNumber(complaint.getComplaintNumber())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .category(complaint.getComplaintCategory())
                .priority(complaint.getPriority())
                .status(complaint.getStatus())
                .emergency(complaint.isEmergency())
                .address(complaint.getLocation() != null
                ?complaint.getLocation().getAddress() : null)
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .progressPercent(calcProgress(complaint.getStatus()))
                .thumbnailUrl(complaint.getImages().isEmpty()? null: complaint.getImages().get(0).getImageUrl())
                .build();

    }

    private Integer calcProgress(ComplaintStatus status){

        if (status == null){
            return null;
        }
        return switch (status) {
            case PENDING_REVIEW -> 10;
            case APPROVED -> 25;
            case ASSIGNED -> 40;
            case IN_PROGRESS -> 65;
            case COMPLETED -> 100;
            default -> null;
        };


    }

    private String truncated(String text, int max){

        if(text == null) return  null;

        if (text.length() <= max) return text;
        return text.substring(0, max) +"...";
    }
}
