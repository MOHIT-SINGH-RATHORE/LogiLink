package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long transporterId;
    private Long transportRequestId;

    private Double rating; // 1.0 - 5.0

    @Column(length = 1000)
    private String reviewText;

    private LocalDateTime createdAt = LocalDateTime.now();
}
