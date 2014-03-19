package com.baidu.fsm.core;

/**
 * {@link com.baidu.fsm.core.Gateway} is a auto state, when flows arriving at gateway,
 * it will decide the nodeId event by {@link com.baidu.fsm.core.EventDecision}.
 */
public abstract class Gateway extends AbstractState {
    /**
     * Event decision instance
     */
    private EventDecision eventDecision;

    protected Gateway(String name) {
        super(name);
    }

    public void setEventDecision(EventDecision eventDecision) {
        this.eventDecision = eventDecision;
    }

    public EventDecision getEventDecision() {
        return eventDecision;
    }
}
