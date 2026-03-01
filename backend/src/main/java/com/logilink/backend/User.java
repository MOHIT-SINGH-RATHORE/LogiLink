package com.logilink.backend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    private String name;
    private String address;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private com.logilink.backend.entity.Role role = com.logilink.backend.entity.Role.USER;
}
