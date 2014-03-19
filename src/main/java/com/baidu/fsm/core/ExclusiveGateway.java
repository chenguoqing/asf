package com.baidu.fsm.core;

/**
 * <p>
 * An exclusive gateway is used to model a decision in the doOutgoing. When the execution arrives at this gateway,
 * all outgoing sequence flow are evaluated in the order in which they are defined.  The sequence flow which
 * retrieved from {@link com.baidu.fsm.core.EventDecision} is selected for continuing the doOutgoing
 * </p>
 */
public class ExclusiveGateway extends Gateway {

    public ExclusiveGateway(String name) {
        super(name);
    }

    @Override
    public StateType getType() {
        return StateType.ExclusiveGateway;
    }


    @Override
    public void onState(State from, String event, StateMachine stateMachine) {
        super.onState(from, event, stateMachine);

        String decidedEvent;

        // If EventDecision is not set, and only one successor exists, the only one will be take as the nodeId event
        if (getEventDecision() == null) {
            if (this.successorSize() > 1) {
                throw new IllegalStateException("EventDecision instance must be set to ExclusiveGateway.");
            }
            decidedEvent = getSuccessors().keySet().iterator().next();
        } else {
            decidedEvent = getEventDecision().decideEvent(stateMachine);
        }

        if (decidedEvent == null) {
            throw new StateMachineException("Can't resolve the auto event for " + getEventDecision());
        }

        stateMachine.transit(decidedEvent);
    }
}
