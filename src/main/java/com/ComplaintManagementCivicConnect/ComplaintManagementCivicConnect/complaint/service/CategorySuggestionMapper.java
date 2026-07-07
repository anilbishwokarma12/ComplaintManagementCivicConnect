package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class CategorySuggestionMapper {

    private static final Map<ComplaintCategory, String> TITLES =
            Map.of(
                    ComplaintCategory.ANIMAL, "Stray Animal Detected",
                    ComplaintCategory.CRACKS, "Roads Cracks Detected",
                    ComplaintCategory.OPEN_MANHOLE, "Open Manhole Detetcted",
                    ComplaintCategory.POTHOLE, "Pothole Detected",
                    ComplaintCategory.OTHER, "Civic Issue Detected"

    );

    public String suggestTitle(ComplaintCategory category){

        return TITLES.getOrDefault(category, "Civic Issue Detected");
    }

    private static final Map<ComplaintCategory, ComplaintPriority>
            PRIORITIES = Map.of(
            ComplaintCategory.ANIMAL,         ComplaintPriority.MEDIUM,
            ComplaintCategory.CRACKS,      ComplaintPriority.LOW,
            ComplaintCategory.OPEN_MANHOLE,        ComplaintPriority.HIGH,
            ComplaintCategory.POTHOLE,  ComplaintPriority.HIGH,
            ComplaintCategory.OTHER,        ComplaintPriority.LOW

    );

    public ComplaintPriority suggestedPriority(ComplaintCategory category){

        return PRIORITIES.getOrDefault(category, ComplaintPriority.LOW);
    }

    public ComplaintCategory toCategory(String raw){

        if (raw ==null ||  raw.isBlank())
            return ComplaintCategory.OTHER;

        try{

            return ComplaintCategory.valueOf(
                    raw.trim().toUpperCase()
                            .replace(" ", "_"));
        }

        catch (IllegalArgumentException e){
            return ComplaintCategory.OTHER;

        }
    }
}
