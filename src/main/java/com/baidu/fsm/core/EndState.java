package com.baidu.fsm.core;

/**
 * End state
 */
public final class EndState extends AbstractState {
    public static final String name = "__end__";

    public EndState() {
        super(name);
    }

    @Override
    public boolean hasSuccessor(String stateName) {
        return false;
    }

    @Override
    public StateType getType() {
        return StateType.End;
    }
}
