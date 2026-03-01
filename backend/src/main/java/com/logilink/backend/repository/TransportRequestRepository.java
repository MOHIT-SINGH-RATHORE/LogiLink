package com.logilink.backend.repository;

import com.logilink.backend.entity.TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransportRequestRepository extends JpaRepository<TransportRequest, Long> {
    List<TransportRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TransportRequest> findByStatusOrderByCreatedAtDesc(TransportRequest.RequestStatus status);
}
