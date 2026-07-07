package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResult {

    private String imageUrl;
    private String publicId;

}
