package com.baidu.fsm.core;

/**
 * Wait state fromNodeId
 */
public class WaitState extends AbstractState {
    public WaitState(String name) {
        super(name);
    }

    @Override
    public StateType getType() {
        return StateType.Wait;
    }
}
