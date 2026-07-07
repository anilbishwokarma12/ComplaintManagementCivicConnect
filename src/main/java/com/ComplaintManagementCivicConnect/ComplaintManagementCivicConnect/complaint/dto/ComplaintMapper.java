package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.ComplaintImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplaintMapper {

    public ComplaintResponse toResponse(Complaint complaint){

       List<ComplaintImageDTO> images =  complaint.getImages().stream()
                .map(this::toImageDto)
                .collect(Collectors.toList());

       return ComplaintResponse.builder()
               .id(complaint.getId())
               .complaintNumber(complaint.getComplaintNumber())
               .title(complaint.getTitle())
               .description(complaint.getDescription())
               .priority(complaint.getPriority())
               .emergency(complaint.isEmergency())
               .status(complaint.getStatus())
               .address(complaint.getLocation() != null
               ? complaint.getLocation().getAddress()
               :null)

               .latitude(complaint.getLocation() !=null
               ? complaint.getLocation().getLatitude()
               :null)

               .longitude(complaint.getLocation() != null
               ? complaint.getLocation().getLatitude()
               : null)

               .images(images)
               .aiRawCategory(complaint.getAiRawCategory())
               .aiConfidence(complaint.getAiConfidence())
               .aiValidated(complaint.getAiValidate())
               .createdAt(complaint.getCreatedAt())
               .updatedAt(complaint.getUpdatedAt())
               .build();
    }

    public ComplaintImageDTO toImageDto(ComplaintImage image){

        return ComplaintImageDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .uploadedAT(image.getUploadedAt())
                .build();
    }
}
