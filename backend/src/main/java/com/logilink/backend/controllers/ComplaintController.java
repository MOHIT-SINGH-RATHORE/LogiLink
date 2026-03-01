package com.logilink.backend.controllers;

import com.logilink.backend.entity.Complaint;
import com.logilink.backend.repository.ComplaintRepository;
import com.logilink.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintController {

    @Autowired
    ComplaintRepository complaintRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('TRANSPORTER')")
    public ResponseEntity<?> submitComplaint(@RequestBody Complaint complaint, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        complaint.setUserId(userDetails.getId());
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setStatus(Complaint.ComplaintStatus.OPEN);

        complaintRepository.save(complaint);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping("/my-complaints")
    public ResponseEntity<?> getMyComplaints(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(complaintRepository.findByUserIdOrderByCreatedAtDesc(userDetails.getId()));
    }
}
