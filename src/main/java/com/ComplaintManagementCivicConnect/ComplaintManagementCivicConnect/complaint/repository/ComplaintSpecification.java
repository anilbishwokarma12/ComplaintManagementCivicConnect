package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.repository;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.dto.ComplaintFilterRequest;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.entity.Complaint;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintCategory;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintPriority;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.complaint.enums.ComplaintStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;

public class ComplaintSpecification {

    private ComplaintSpecification() {}
    public static Specification<Complaint> build(UUID userId, ComplaintFilterRequest filter){

        return (root, query, cb) ->{

            List<Predicate> predicates = new ArrayList<>();

           predicates.add(cb.equal(
                   root.get("user").get("id"), userId));


           if(filter.getComplaintStatus() != null) {
               predicates.add(cb.equal(
                       root.get("status"),
                       filter.getComplaintStatus()
               ));
           }

               // Search: title OR complaintNumber OR category
               if (filter.getSearch() != null && !filter.getSearch().isBlank()) {

                   String kw = "%" + filter.getSearch().toLowerCase().trim() + "%";

                   predicates.add(cb.or(
                           cb.like(cb.lower(root.get("title")), kw),
                           cb.like(cb.lower(root.get("complaintNumber")), kw),
                           cb.like(cb.lower(root.get("category")), kw)
                   ));

               }

                  //category
                  if (filter.getCategory() != null){
                      predicates.add(cb.equal(
                              root.get("category"),
                              filter.getCategory()
                      ));
                  }

                  if (filter.getPriority() != null) {
                      predicates.add(cb.equal(
                              root.get("priority"),
                              filter.getPriority()
                      ));
                  }

                      return cb.and(
                              predicates.toArray(new Predicate[0]));



               };

    }

    public static Specification<Complaint> belongsToUser(UUID userId){
        return(root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);


    }

    public static Specification<Complaint> hasStatus(ComplaintStatus status){

        return (root, query, cb) ->
        cb.equal(root.get("status"), status);
    }

    public static Specification<Complaint> hasCategory(ComplaintCategory category){
        return (root, query, cb) ->
        cb.equal(root.get("category"), category);
    }

    public static Specification<Complaint> hasPriority(ComplaintPriority priority){

        return (root, query, cb) ->
        cb.equal(root.get("priority"), priority);
    }

}




