package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintDraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ComplaintDraftCleanupJob {
    private final ComplaintDraftRepository draftRepository;

    @Transactional
    @Scheduled(fixedRate = 3_600_000)
    public void cleanStableDrafts(){

       LocalDateTime cutoff =  LocalDateTime.now().minusHours(24);

       int deleted = draftRepository.deleteStaleDrafts(cutoff);

       if(deleted > 0){

           log.info("[CLEANUP] Deleted {} stable drafts", deleted);
       }

    }
}
