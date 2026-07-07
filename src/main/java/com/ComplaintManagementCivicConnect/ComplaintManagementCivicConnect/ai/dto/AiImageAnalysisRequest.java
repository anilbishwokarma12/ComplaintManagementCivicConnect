package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiImageAnalysisRequest {

    private String imageUrl;
}
