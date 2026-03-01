package com.logilink.backend.controllers;

import com.logilink.backend.entity.TransportRequest;
import com.logilink.backend.repository.TransportRequestRepository;
import com.logilink.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:5173")
public class TransportRequestController {

    @Autowired
    TransportRequestRepository requestRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRequest(@RequestBody TransportRequest request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        request.setUserId(userDetails.getId());
        requestRepository.save(request);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRequests(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<TransportRequest> requests = requestRepository.findByUserIdOrderByCreatedAtDesc(userDetails.getId());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/market")
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> getMarketRequests() {
        List<TransportRequest> requests = requestRepository
                .findByStatusOrderByCreatedAtDesc(TransportRequest.RequestStatus.PENDING);
        return ResponseEntity.ok(requests);
    }
}
