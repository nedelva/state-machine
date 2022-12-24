package edu.sample.statemachne.config;

import edu.sample.statemachne.domain.PaymentEvent;
import edu.sample.statemachne.domain.PaymentState;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class StateMachineConfigTest {
    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testNewStateMachine() {
        StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(UUID.randomUUID());
        sm.start();
        assertThat("should be in initial state, NEW state", sm.getState().getId(), Matchers.is(PaymentState.NEW));

        sm.sendEvent(PaymentEvent.PRE_AUTHORIZE); // triggers PreAuthAction
        assertThat("PRE_AUTHORIZE event should keep the state machine in NEW state",
                sm.getState().getId(), Matchers.oneOf(PaymentState.NEW));

        sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

        if (PaymentState.PRE_AUTH.equals(sm.getState().getId())) {
            sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
            assertThat("should remain in PRE_AUTH state", sm.getState().getId(), Matchers.is(PaymentState.PRE_AUTH));
            sm.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
            assertThat("should remain in PRE_AUTH state", sm.getState().getId(), Matchers.is(PaymentState.PRE_AUTH));
        }

        System.out.println("sm.getState() = " + sm.getState());

        if (PaymentState.PRE_AUTH.equals(sm.getState().getId())) {
            sm.sendEvent(PaymentEvent.AUTHORIZE); //triggers StateMachineConfig.authAction() action
            assertThat("should transition to either AUTH or AUTH_ERROR states",
                    sm.getState().getId(), Matchers.oneOf(PaymentState.AUTH, PaymentState.AUTH_ERROR));
        }


        if (PaymentState.AUTH.equals(sm.getState().getId())) {
            sm.sendEvent(PaymentEvent.AUTH_DECLINED);
            assertThat("should remain in AUTH state", sm.getState().getId(), Matchers.is(PaymentState.AUTH));
            sm.sendEvent(PaymentEvent.AUTHORIZE);
            assertThat("should remain in AUTH state", sm.getState().getId(), Matchers.is(PaymentState.AUTH));
        } else if (PaymentState.AUTH_ERROR.equals(sm.getState().getId())) {
            sm.sendEvent(PaymentEvent.AUTH_DECLINED);
            assertThat("should remain in AUTH_ERROR state", sm.getState().getId(), Matchers.is(PaymentState.AUTH_ERROR));
            sm.sendEvent(PaymentEvent.AUTHORIZE);
            assertThat("should remain in AUTH_ERROR state", sm.getState().getId(), Matchers.is(PaymentState.AUTH_ERROR));

        }
    }
}