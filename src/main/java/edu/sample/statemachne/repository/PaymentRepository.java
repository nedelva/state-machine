package edu.sample.statemachne.repository;

import edu.sample.statemachne.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
