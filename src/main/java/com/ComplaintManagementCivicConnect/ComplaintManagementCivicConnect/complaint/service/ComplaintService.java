package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.dto.AiImageAnalysisResult;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.ai.service.AiImageValidationService;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.PagedResponse;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.*;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.ComplaintDraft;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.ComplaintImage;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintDraftRepository;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintRepository;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository.ComplaintSpecification;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.location.Location;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.User;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {



    private final UserService userService;
    private final CloudinaryUploadService uploadService;

    private final AiImageValidationService aiService;


    private final CategorySuggestionMapper suggestionMapper;
    private final ComplaintDraftRepository draftRepository;
    private final ComplaintNumberGenerator numberGenerator;
    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final ComplaintSummaryMapper complaintSummaryMapper;
    private final ComplaintDetailMapper complaintDetailMapper;



    //  STEP 1 — ANALYZE IMAGE
    //  Matches: Screen 1 (Upload) + Screen 2 (AI Analysis)

    public AnalyzeImageResponse analyzeImage(
            String email, MultipartFile image){


       User user =  userService.findByEmailOrThrow(email);

       validateImage(image);

      UploadResult upload =  uploadService.uploadImage(image, "complaints");

      log.info("[STEP1]  Image uploaded for {} : {}", email, upload.getImageUrl());

      //call ml service
       AiImageAnalysisResult aiResult =  aiService.analyzeImage(upload.getImageUrl());

       //Map String->enum

        ComplaintCategory category = suggestionMapper.toCategory(
                aiResult.getCategory()
        );

        // Derive suggestions (Spring Boot logic, not ML)

      String suggestedTitle =  suggestionMapper.suggestTitle(category);

     var suggestedPriority =  suggestionMapper.suggestedPriority(category);

     //save draft
        ComplaintDraft draft = ComplaintDraft.builder()
                .user(user)
                .imageUrl(upload.getImageUrl())
                .imagePublicId(upload.getPublicId())
                .aiCategory(category)
                .aiConfidence(aiResult.getConfidence())
                .aiValid(aiResult.isValid())
                .suggestedTitle(suggestedTitle)
                .suggestedPriority(suggestedPriority)
                .consumed(false)
                .build();

        ComplaintDraft saved = draftRepository.save(draft);

        log.info("[STEP1] {} saved -category ={} " +
                "confidence = {} valid ={}",
                saved.getId(),category,
                aiResult.getConfidence(),
                aiResult.isValid());

        return AnalyzeImageResponse.builder()
                .draftId(saved.getId())
                .imageUrl(saved.getImageUrl())
                .category(category)
                .confidence(aiResult.getConfidence())
                .validImage(aiResult.isValid())
                .suggestedTitle(suggestedTitle)
                .complaintPriority(suggestedPriority)
                .build();
    }


    //STEP 2 — SUBMIT FINAL COMPLAINT
    //  Matches: Screen 3 (Add Details) + Screen 4 (Location)
    //           + Screen 5 (Review → Submit)

    public ComplaintResponse submitComplaint(String email, SubmitComplaintRequest request){

       User user = userService.findByEmailOrThrow(email);

       //load draft with ownership check

       ComplaintDraft draft =  draftRepository.findByIdAndUserId( request.getDraftId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Draft not found. Please upload " + "Your image again"));


        if ((draft.isConsumed())){

            throw  new RuntimeException("The draft was already submitted"+
                    "Please start a new complaint");

        }

       Location location =  Location.builder()
                .address(request.getLocation().getAddress())
                .latitude(request.getLocation().getLatitude())
                .longitude(request.getLocation().getLongitude())
                .build();

        // Generate unique complaint number
        String number = numberGenerator.generate();

        // Build complaint
        // NOTE: category comes from AI (draft) — not from request

     Complaint complaint =    Complaint.builder()
                .complaintNumber(number)
                .title(request.getTitle())
                .description(request.getDescription())
                .complaintCategory(draft.getAiCategory())
                .priority(request.getPriority())
                .emergency(request.isEmergency())
                .user(user)
                .location(location)
                .aiRawCategory(draft.getAiCategory().name())
                .aiConfidence(draft.getAiConfidence())
                .aiValidate(draft.getAiValid())
                .build();


    Complaint saved =  complaintRepository.save(complaint);


        // Attach the already-uploaded image from draft

       ComplaintImage image = ComplaintImage.builder()
                .imageUrl(draft.getImageUrl())
                .publicID(draft.getImagePublicId())
                .build();

       saved.addImage(image);
       complaintRepository.save(saved);

       // Mark draft consumed — prevents double submission
        draft.setConsumed(true);
        draftRepository.save(draft);

        log.info("[STEP2] Complaint {} created by {} " +
                        "from draft {} | category={} priority={}",
                number, email, draft.getId(),
                draft.getAiCategory(),
                request.getPriority());


        return complaintMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public StatusCountDTO getMyComplaintStats(String email) {
        User user = userService.findByEmailOrThrow(email);
        return buildSatusCounts(user.getId());
    }

    //module 3

    @Transactional(readOnly = true)
    public MyComplaintsResponse getMyComplaint(String email, ComplaintFilterRequest filter){

       User user =  userService.findByEmailOrThrow(email);
      UUID  userId =  user.getId();


       StatusCountDTO statusCounts =  buildSatusCounts(userId);

       Specification<Complaint> specification= ComplaintSpecification.build(userId, filter);

        Sort sort = "oldest"
                .equalsIgnoreCase(filter.getSort())
                ?Sort.by(Sort.Direction.ASC, "createdAt")
                :Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

       Page<Complaint>  page = complaintRepository.findAll(specification, pageable);

        Page<ComplaintSummaryResponse> mapped = page.map(complaintSummaryMapper::toSummary);

        return MyComplaintsResponse.builder()
                .statusCounts(statusCounts)
                .complaints(PagedResponse.of(mapped))
                .build();


    }



    @Transactional(readOnly = true)
    public ComplaintDetailResponse getComplaintById(
            String email, UUID complaintId) {

        User user = userService.findByEmailOrThrow(email);

        Complaint complaint = complaintRepository
                .findById(complaintId)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found"));

        if (!complaint.getUser().getId()
                .equals(user.getId())) {
            throw new RuntimeException(
                    "Access denied");
        }

        return complaintDetailMapper.toDetail(complaint);
    }




    //private helpers
    private void validateImage(MultipartFile image){

        if(image == null || image.isEmpty()){

            throw new RuntimeException("Please select an image to upload");
        }

        String type = image.getContentType();

        if (type == null || !type.startsWith("image/")){
            throw new RuntimeException(
                    "Only image files are allowed"+
                            "(jpg, png, webp)");
        }

        if (image.getSize()>5*1024*1024){
            throw new RuntimeException(
                    "Image must be under 5MB");
        }

    }

    private StatusCountDTO buildSatusCounts(UUID userId){

       List<Object[]> rows =  complaintRepository.countGroupedByStatus(userId);

       long all =0, pendingReview = 0, approved =0, assigned= 0,
               inProgress = 0, completed =0, rejected =0;

       for (Object[] row : rows){

           ComplaintStatus status = (ComplaintStatus) row[0];
           long count = (long) row[1];
           all += count;
           switch (status) {
               case PENDING_REVIEW -> pendingReview = count;
               case APPROVED       -> approved     = count;
               case ASSIGNED       -> assigned     = count;
               case IN_PROGRESS    -> inProgress   = count;
               case COMPLETED      -> completed    = count;
               case REJECTED       -> rejected     = count;
           }


       }



        return StatusCountDTO.builder()
                .all(all)
                .pendingReview(pendingReview)
                .approved(approved)
                .assigned(assigned)
                .inProgress(inProgress)
                .completed(completed)
                .rejected(rejected)
                .build();
    }
}
