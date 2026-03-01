package com.logilink.backend.repository;

import com.logilink.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTransporterIdOrderByCreatedAtDesc(Long transporterId);
}
