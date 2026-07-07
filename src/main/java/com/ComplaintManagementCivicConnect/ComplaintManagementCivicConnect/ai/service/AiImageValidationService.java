package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto.AiImageAnalysisResult;

public interface AiImageValidationService {

    AiImageAnalysisResult analyzeImage(String imageUrl);
}
