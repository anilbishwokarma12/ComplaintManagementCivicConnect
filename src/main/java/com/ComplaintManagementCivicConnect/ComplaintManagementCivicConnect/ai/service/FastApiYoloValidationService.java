package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto.AiImageAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
public class FastApiYoloValidationService implements AiImageValidationService {

    private static final double MIN_CONFIDENCE = 0.4;

    private final RestClient restClient;
    private final RestClient downloadClient;

    public FastApiYoloValidationService(
            @Value("${ai.fastapi.base-url:http://localhost:8000}") String fastApiBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(fastApiBaseUrl)
                .build();
        this.downloadClient = RestClient.builder().build();
    }

    @Override
    public AiImageAnalysisResult analyzeImage(String imageUrl) {
        try {
            log.info("[YOLO] Downloading image from: {}", imageUrl);

            byte[] imageBytes = downloadClient.get()
                    .uri(URI.create(imageUrl))
                    .retrieve()
                    .body(byte[].class);

            if (imageBytes == null || imageBytes.length == 0) {
                throw new RuntimeException("Failed to download image from Cloudinary");
            }

            log.info("[YOLO] Downloaded {} bytes, sending to FastAPI...", imageBytes.length);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return "image.jpg";
                }
            };
            body.add("file", imageResource);

            Map<String, Object> response = restClient.post()
                    .uri("/predict")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            // ✅ ADD THIS: Log the full response
            log.info("[YOLO] FastAPI response: {}", response);

            // Step 4: Parse detections
            List<Map<String, Object>> rawDetections =
                    (List<Map<String, Object>>) response.get("detections");

            if (rawDetections == null || rawDetections.isEmpty()) {
                log.info("[YOLO] NO detections - invalid image");
                return invalidResult();
            }

            // ✅ FIX: Change "class" to "className"
            List<AiImageAnalysisResult.Detection> detections =
                    rawDetections.stream()
                            .map(d -> AiImageAnalysisResult.Detection.builder()
                                    .className((String) d.get("className"))  // ← CHANGED HERE
                                    .confidence(((Number) d.get("confidence")).doubleValue())
                                    .build())
                            .sorted(Comparator.comparingDouble(
                                            AiImageAnalysisResult.Detection::getConfidence)
                                    .reversed())
                            .collect(Collectors.toList());

            AiImageAnalysisResult.Detection top = detections.get(0);

            log.info("[YOLO] Top detection: class={} confidence={} | all detections={}",
                    top.getClassName(),
                    top.getConfidence(),
                    detections.stream()
                            .map(d -> d.getClassName() + ":" + d.getConfidence())
                            .collect(Collectors.joining(", ")));

            if (top.getConfidence() < MIN_CONFIDENCE) {
                log.info("[YOLO] Top confidence {} below threshold {} — invalid",
                        top.getConfidence(), MIN_CONFIDENCE);
                return invalidResult();
            }

            return AiImageAnalysisResult.builder()
                    .category(top.getClassName())
                    .confidence(top.getConfidence())
                    .valid(true)
                    .detections(detections)
                    .build();

        } catch (Exception e) {
            log.error("[YOLO] FastAPI call failed: {}. Using fallback", e.getMessage());
            return AiImageAnalysisResult.builder()
                    .category("OTHER")
                    .confidence(0.0)
                    .valid(true)
                    .detections(Collections.emptyList())
                    .build();
        }
    }

    private AiImageAnalysisResult invalidResult() {
        return AiImageAnalysisResult.builder()
                .category("OTHER")
                .confidence(0.0)
                .valid(false)
                .detections(Collections.emptyList())
                .build();
    }
}