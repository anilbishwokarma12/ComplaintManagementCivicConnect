package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplaintDetailMapper {

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public ComplaintDetailResponse toDetail(Complaint complaint){

       List<ComplaintImageDTO> images =  complaint.getImages().stream()
                .map(img -> ComplaintImageDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .uploadedAT(img.getUploadedAt())
                        .build())
                .collect(Collectors.toList());

       List<StatusHistoryDTO> timeline = complaint.getStatusHistory().stream()
                .map(this::statusHistoryDTO)
                .collect((Collectors.toList()));

      String qrTackingUrl =  frontendUrl+ "/track" + complaint.getComplaintNumber();

      return ComplaintDetailResponse.builder()
              .id(complaint.getId())
              .complaintNumber(complaint.getComplaintNumber())
              .title(complaint.getTitle())
              .description(complaint.getDescription())
              .category(complaint.getComplaintCategory())
              .priority(complaint.getPriority())
              .status(complaint.getStatus())
              .emergency(complaint.isEmergency())
              .emergencyReason(complaint.getEmergencyReason())
              .address(complaint.getLocation()!= null? complaint.getLocation().getAddress()
                      : null)
              .latitude(complaint.getLocation() != null
              ? complaint.getLocation().getLatitude()
                      : null)
              .longitude(complaint.getLocation() != null
              ? complaint.getLocation().getLongitude()
                      : null)
              .images(images)
              .aiConfidence(complaint.getAiConfidence())
              .aiValidate(complaint.getAiValidate())
              .createdAt(complaint.getCreatedAt())
              .updatedAt(complaint.getUpdatedAt())
              .qrTrackingUrl(qrTackingUrl)
              .statusTimeLine(timeline)
              .build();



    }

    private StatusHistoryDTO statusHistoryDTO(ComplaintStatusHistory history){

        return StatusHistoryDTO.builder()
                .id((history.getId()))
                .status(history.getComplaint().getStatus())
                .note(history.getNote())
                .changedByName(history.getChangedByName())
                .changedByRole(history.getChangedByRole())
                .changedAt(history.getChangedAt())
                .build();
    }
}
