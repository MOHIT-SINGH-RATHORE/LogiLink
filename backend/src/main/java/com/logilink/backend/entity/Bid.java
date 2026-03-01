package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transportRequestId;
    private Long transporterId;

    private Double serviceCharge;
    private Double vehicleCost;
    private Double fuelCost;
    private Double tollTaxFee;

    private Double totalAmount; // Will be the sum of above

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BidStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
