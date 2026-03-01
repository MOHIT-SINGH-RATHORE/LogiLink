package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TransportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fromLocation;
    private String toLocation;
    private Double distance;

    private Boolean isFragile;

    private Double pricingRangeStart;
    private Double pricingRangeEnd;

    // e.g., "Ground Floor Loading", "3rd Floor Unloading - No Elevator"
    private String loadUnloadOption;

    private Integer requiredDays;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        COMPLETED,
        CANCELLED
    }
}
