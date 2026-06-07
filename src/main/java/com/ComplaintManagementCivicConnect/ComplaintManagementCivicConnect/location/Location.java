package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.location;


import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String address;

    private Double latitude;

    private Double longitude;



}
