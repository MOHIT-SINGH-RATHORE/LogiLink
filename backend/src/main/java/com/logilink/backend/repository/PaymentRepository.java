package com.logilink.backend.repository;

import com.logilink.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserIdOrderByPaymentDateDesc(Long userId);

    List<Payment> findAllByOrderByPaymentDateDesc();
}
