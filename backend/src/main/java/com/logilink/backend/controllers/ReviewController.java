package com.logilink.backend.controllers;

import com.logilink.backend.entity.Review;
import com.logilink.backend.entity.Transporter;
import com.logilink.backend.repository.ReviewRepository;
import com.logilink.backend.repository.TransporterRepository;
import com.logilink.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TransporterRepository transporterRepository;

    @PostMapping("/{transporterId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitReview(@PathVariable Long transporterId, @RequestBody Review review,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Transporter> transporterOpt = transporterRepository.findById(transporterId);
        if (!transporterOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Transporter not found");
        }

        Transporter transporter = transporterOpt.get();

        review.setUserId(userDetails.getId());
        review.setTransporterId(transporterId);
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        // Update transporter rating
        List<Review> allReviews = reviewRepository.findByTransporterIdOrderByCreatedAtDesc(transporterId);
        double avgRating = allReviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);

        transporter.setRating(avgRating);
        transporter.setReviewCount(allReviews.size());
        transporterRepository.save(transporter);

        return ResponseEntity.ok(review);
    }

    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<?> getTransporterReviews(@PathVariable Long transporterId) {
        return ResponseEntity.ok(reviewRepository.findByTransporterIdOrderByCreatedAtDesc(transporterId));
    }
}
