package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.service;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.UploadResult;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryUploadService {

    private final Cloudinary  cloudinary;

    public UploadResult uploadImage(MultipartFile file, String folder){

        try {

            log.info("[CLOUDINARY] Uploading file to folder: {} | " +
            "file : {} |  size :{} bytes",
                    folder,
                    file.getOriginalFilename(),
                    file.getSize());

           Map<?,?> result =  cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image",
                            "overwrite", false
                    ));

         String imageUrl =    (String) result.get("secure_url");
          String publicId =  (String) result.get("public_id");

          log.info("[CLOUDINARY] upload success -> {}", imageUrl);

          return UploadResult.builder()
                  .imageUrl(imageUrl)
                  .publicId(publicId)
                  .build();

        }

        catch (IOException e){

            log.error("[CLOUDINARY] Uploaded failed :{}", e.getMessage());

            throw new RuntimeException(
                    "Image upload failed. Please try again.");


        }

    }

    public void deleteImage(String publicId){

        try {

            if (publicId == null || publicId.isBlank()){
                return;
            }

            log.info("[CLOUDINARY] Deleting image: {}", publicId);

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            log.info("[CLOUDINARY] Deleted: {}", publicId);

        }

        catch (IOException e){

            log.error("[CLOUDINARY] Deleted failed for {}: {}", publicId, e.getMessage());

        }
    }
}
