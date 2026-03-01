package com.logilink.backend.controllers;

import com.logilink.backend.UserRepository;
import com.logilink.backend.repository.ComplaintRepository;
import com.logilink.backend.repository.PaymentRepository;
import com.logilink.backend.repository.TransporterRepository;
import com.logilink.backend.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransporterRepository transporterRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ComplaintRepository complaintRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalTransporters", transporterRepository.count());
        stats.put("totalComplaints", complaintRepository.count());

        List<Payment> allPayments = paymentRepository.findAll();
        double totalRevenue = allPayments.stream()
                .mapToDouble(p -> p.getPlatformFee() != null ? p.getPlatformFee() : 0.0).sum();
        stats.put("totalRevenue", totalRevenue);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/transporters")
    public ResponseEntity<?> getAllTransporters() {
        return ResponseEntity.ok(transporterRepository.findAll());
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAllByOrderByPaymentDateDesc());
    }

    @GetMapping("/complaints")
    public ResponseEntity<?> getAllComplaints() {
        return ResponseEntity.ok(complaintRepository.findAllByOrderByCreatedAtDesc());
    }
}
