package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiImageAnalysisResult {

    private String category;
    private double confidence;
    private boolean valid;
    private List<Detection> detections;

    @Data
    @Builder
    public static  class  Detection{

        private String className;
        private double confidence;
    }
}
