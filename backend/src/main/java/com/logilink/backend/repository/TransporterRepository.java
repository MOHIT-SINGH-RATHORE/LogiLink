package com.logilink.backend.repository;

import com.logilink.backend.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransporterRepository extends JpaRepository<Transporter, Long> {
    Optional<Transporter> findByUsernameIgnoreCase(String username);
}
