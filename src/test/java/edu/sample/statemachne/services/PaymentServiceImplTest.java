package edu.sample.statemachne.services;

import edu.sample.statemachne.domain.Payment;
import edu.sample.statemachne.domain.PaymentEvent;
import edu.sample.statemachne.domain.PaymentState;
import edu.sample.statemachne.repository.PaymentRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Test
    @Transactional
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());
        assertThat(preAuthedPayment, Matchers.is(savedPayment));
        assertThat(sm.getState().getId(), Matchers.is(PaymentState.PRE_AUTH));

    }
}