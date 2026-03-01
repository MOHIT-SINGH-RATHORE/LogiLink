package com.logilink.backend.repository;

import com.logilink.backend.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByTransportRequestIdOrderByTotalAmountAsc(Long transportRequestId);

    List<Bid> findByTransporterIdOrderByCreatedAtDesc(Long transporterId);

    Optional<Bid> findByTransportRequestIdAndTransporterId(Long transportRequestId, Long transporterId);
}
