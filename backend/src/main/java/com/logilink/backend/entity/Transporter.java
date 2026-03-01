package com.logilink.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String password; // Will be hashed
    private String address;
    private String serviceArea;
    private String mobileNumber;

    private Double rating = 0.0;
    private Integer reviewCount = 0;

    @Enumerated(EnumType.STRING)
    private Role role = Role.TRANSPORTER;
}
