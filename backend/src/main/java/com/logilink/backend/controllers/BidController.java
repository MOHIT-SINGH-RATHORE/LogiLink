package com.logilink.backend.controllers;

import com.logilink.backend.entity.Bid;
import com.logilink.backend.entity.TransportRequest;
import com.logilink.backend.repository.BidRepository;
import com.logilink.backend.repository.TransportRequestRepository;
import com.logilink.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "http://localhost:5173")
public class BidController {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    TransportRequestRepository requestRepository;

    @PostMapping("/{requestId}")
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> placeBid(@PathVariable Long requestId, @RequestBody Bid bid,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<TransportRequest> reqOpt = requestRepository.findById(requestId);
        if (!reqOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Request not found");
        }

        if (bidRepository.findByTransportRequestIdAndTransporterId(requestId, userDetails.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("You have already placed a bid on this request.");
        }

        bid.setTransporterId(userDetails.getId());
        bid.setTransportRequestId(requestId);

        Double total = (bid.getServiceCharge() != null ? bid.getServiceCharge() : 0.0) +
                (bid.getVehicleCost() != null ? bid.getVehicleCost() : 0.0) +
                (bid.getFuelCost() != null ? bid.getFuelCost() : 0.0) +
                (bid.getTollTaxFee() != null ? bid.getTollTaxFee() : 0.0);
        bid.setTotalAmount(total);

        bidRepository.save(bid);
        return ResponseEntity.ok(bid);
    }

    @GetMapping("/request/{requestId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBidsForRequest(@PathVariable Long requestId) {
        List<Bid> bids = bidRepository.findByTransportRequestIdOrderByTotalAmountAsc(requestId);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/my-bids")
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> getMyBids(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Bid> bids = bidRepository.findByTransporterIdOrderByCreatedAtDesc(userDetails.getId());
        return ResponseEntity.ok(bids);
    }

    @PostMapping("/{bidId}/accept")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> acceptBid(@PathVariable Long bidId) {
        Optional<Bid> bidOpt = bidRepository.findById(bidId);
        if (!bidOpt.isPresent())
            return ResponseEntity.badRequest().body("Bid not found");

        Bid bid = bidOpt.get();
        if (bid.getStatus() != Bid.BidStatus.PENDING) {
            return ResponseEntity.badRequest().body("Bid is already " + bid.getStatus());
        }

        Optional<TransportRequest> reqOpt = requestRepository.findById(bid.getTransportRequestId());
        if (!reqOpt.isPresent())
            return ResponseEntity.badRequest().body("Request not found");

        TransportRequest request = reqOpt.get();
        request.setStatus(TransportRequest.RequestStatus.ACCEPTED);
        requestRepository.save(request);

        bid.setStatus(Bid.BidStatus.ACCEPTED);
        bidRepository.save(bid);

        return ResponseEntity.ok("Bid accepted successfully.");
    }
}
