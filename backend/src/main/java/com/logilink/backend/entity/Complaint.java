package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long transporterId; // Can be null if against platform

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = ComplaintStatus.OPEN;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ComplaintStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED
    }
}
