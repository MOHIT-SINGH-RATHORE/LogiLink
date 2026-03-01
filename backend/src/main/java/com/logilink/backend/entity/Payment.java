package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long bidId;

    private String paymentMethod;

    private Double bidAmount;
    private Double platformFee;
    private Double totalPaid;

    private LocalDateTime paymentDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.COMPLETED;

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}
