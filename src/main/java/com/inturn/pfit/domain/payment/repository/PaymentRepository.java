package com.inturn.pfit.domain.payment.repository;

import com.inturn.pfit.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
