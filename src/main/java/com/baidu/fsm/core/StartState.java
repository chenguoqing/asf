package com.baidu.fsm.core;

/**
 * Start state
 */
public final class StartState extends AbstractState {

    public static final String name = "__init__";

    public StartState() {
        super(name);
    }

    @Override
    public StateType getType() {
        return StateType.Start;
    }
}
