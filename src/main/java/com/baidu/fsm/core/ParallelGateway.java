package com.baidu.fsm.core;

/**
 * The functionality of the parallel gateway is based on the incoming and outgoing sequence flow:
 * <p>
 * <li><strong>fork:</strong> all outgoing sequence flow are followed in parallel, creating one concurrent execution for
 * each sequence flow
 * <li><strong>join:</strong> all concurrent executions arriving at the parallel gateway wait in the gateway until
 * an execution has arrived for each of the incoming sequence flow. Then the doOutgoing continues past the joining gateway.
 * <li><strong>all:</strong> a parallel gateway can have both fork and join behavior,
 * if there are multiple incoming and outgoing sequence flow for the same parallel gateway. In that case,
 * the gateway will first join all incoming sequence flow, before splitting into multiple concurrent paths of
 * executions.
 * </p>
 */
public class ParallelGateway extends Gateway {

    public final ParallelGatewayType gatewayType;

    /**
     * Parallel gateway stateType
     */
    public static enum ParallelGatewayType {
        /**
         * Executes all outgoing events with parallel
         */
        FORK,
        /**
         * Waits all incoming events reaching
         */
        JOIN,
        /**
         * If existing multiple incoming/outgoing events,the stateType can be both <tt>FORK</tt> and <tt>JOIN</tt>
         */
        ALL
    }

    public ParallelGateway(String name, ParallelGatewayType gatewayType) {
        super(name);
        this.gatewayType = gatewayType;
    }

    public ParallelGatewayType getGatewayType() {
        return gatewayType;
    }

    @Override
    public StateType getType() {
        return StateType.ParallelGateway;
    }

    @Override
    public void onState(State from, String event, StateMachine stateMachine) {
        super.onState(from, event, stateMachine);
    }
}
