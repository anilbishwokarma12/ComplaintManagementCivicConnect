package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitComplaintRequest {


    @NotNull(message = "draftId is required." + "Upload image via / analyze-image first.")
    private UUID draftId;


    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 120, message = "Title must be 5 to 120 characters")
    private String title;

    @NotNull(message = "Priority is required")
    private ComplaintPriority priority;

    private  boolean emergency = false;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 1000, message = "Description must be 5 to 1000 characters")
    private String description;


    @NotNull(message = "location is required")
    @Valid
    private LocationDto location;



}
