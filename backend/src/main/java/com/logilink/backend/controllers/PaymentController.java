package com.logilink.backend.controllers;

import com.logilink.backend.entity.Bid;
import com.logilink.backend.entity.Payment;
import com.logilink.backend.repository.BidRepository;
import com.logilink.backend.repository.PaymentRepository;
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
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BidRepository bidRepository;

    @PostMapping("/checkout/{bidId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> processPayment(@PathVariable Long bidId, @RequestParam String paymentMethod,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Bid> bidOpt = bidRepository.findById(bidId);
        if (!bidOpt.isPresent())
            return ResponseEntity.badRequest().body("Bid not found");

        Bid bid = bidOpt.get();
        if (bid.getStatus() != Bid.BidStatus.ACCEPTED) {
            return ResponseEntity.badRequest().body("Bid must be ACCEPTED before payment.");
        }

        Payment payment = new Payment();
        payment.setUserId(userDetails.getId());
        payment.setBidId(bidId);
        payment.setPaymentMethod(paymentMethod);
        payment.setBidAmount(bid.getTotalAmount());

        // 5% Platform fee
        double platformFee = bid.getTotalAmount() * 0.05;
        payment.setPlatformFee(platformFee);
        payment.setTotalPaid(bid.getTotalAmount() + platformFee);

        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        paymentRepository.save(payment);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/my-payments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyPayments(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Payment> payments = paymentRepository.findByUserIdOrderByPaymentDateDesc(userDetails.getId());
        return ResponseEntity.ok(payments);
    }
}
