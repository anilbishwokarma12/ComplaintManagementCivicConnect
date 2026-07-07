package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.PagedResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyComplaintsResponse {

    private StatusCountDTO statusCounts;
    private PagedResponse<ComplaintSummaryResponse> complaints;

}
