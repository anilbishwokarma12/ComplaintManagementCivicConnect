package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintNumberGenerator {

    private final ComplaintRepository complaintRepository;  // ✅ Changed from DashboardComplaintRepository

    @Transactional
    public synchronized String generate() {
        // ✅ Get the maximum complaint number from database
        Optional<String> maxNumberOpt = complaintRepository.findMaxComplaintNumber();

        int nextNumber = 1;

        if (maxNumberOpt.isPresent() && maxNumberOpt.get() != null) {
            String maxNumber = maxNumberOpt.get();
            try {
                // Extract numeric part: "cc-000005" -> 5
                String numericPart = maxNumber.replace("cc-", "");
                nextNumber = Integer.parseInt(numericPart) + 1;
                log.debug("Next number from max: {}", nextNumber);
            } catch (NumberFormatException e) {
                log.error("Failed to parse complaint number: {}", maxNumber, e);
                nextNumber = 1;
            }
        }

        String newNumber = String.format("cc-%06d", nextNumber);

        // ✅ Safety check - ensure uniqueness
        int attempts = 0;
        int maxAttempts = 10;

        while (complaintRepository.existsByComplaintNumber(newNumber) && attempts < maxAttempts) {
            nextNumber++;
            newNumber = String.format("cc-%06d", nextNumber);
            attempts++;
            log.warn("Complaint number {} already exists, trying {}", newNumber, nextNumber);
        }

        if (attempts >= maxAttempts) {
            throw new RuntimeException("Unable to generate unique complaint number after " + attempts + " attempts");
        }

        log.info("✅ Generated new complaint number: {}", newNumber);
        return newNumber;
    }
}