package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto.AiImageAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StubAiImageValidationService implements AiImageValidationService{

    @Override
    public AiImageAnalysisResult analyzeImage(String imageUrl) {
       log.info("[YOLO STUB] Simulating YOLO detection for; {}", imageUrl);


        // Simulate multiple detections like real YOLO would return
        List<AiImageAnalysisResult.Detection> detections =
                List.of(
                        AiImageAnalysisResult.Detection.builder()
                                .className("pothole")
                                .confidence(0.87)
                                .build(),
                        AiImageAnalysisResult.Detection.builder()
                                .className("road_damage")
                                .confidence(0.62)
                                .build()
                );

        return AiImageAnalysisResult.builder()
                .category("POTHOLE")       // top detection
                .confidence(0.87)
                .valid(true)
                .detections(detections)
                .build();
    }
}
