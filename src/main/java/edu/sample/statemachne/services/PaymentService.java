package edu.sample.statemachne.services;

import edu.sample.statemachne.domain.Payment;
import edu.sample.statemachne.domain.PaymentEvent;
import edu.sample.statemachne.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
}
