package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import lombok.Data;

@Data
public class ComplaintFilterRequest {

    private ComplaintStatus complaintStatus;

    private String search;

    private ComplaintCategory category;
    private ComplaintPriority priority;


    private String sort = "newest";

    private int page =0;
    private int size =10;


}
