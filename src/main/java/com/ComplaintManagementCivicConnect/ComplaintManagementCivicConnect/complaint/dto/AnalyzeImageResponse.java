package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto.AiImageAnalysisResult;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AnalyzeImageResponse {

    private UUID draftId;

    private String imageUrl;


    private ComplaintCategory category;

    private double confidence;

    private boolean validImage;

    private String suggestedTitle;

    private ComplaintPriority complaintPriority;


    private List<AiImageAnalysisResult.Detection> detections;


}
