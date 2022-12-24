package edu.sample.statemachne.config.actions;

import edu.sample.statemachne.domain.PaymentEvent;
import edu.sample.statemachne.domain.PaymentState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class PreAuthApprovedAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> stateContext) {
        System.out.println("Sending Notification of Pre-Auth APPROVED");
    }
}
