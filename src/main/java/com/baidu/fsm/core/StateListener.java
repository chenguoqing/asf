package com.baidu.fsm.core;

/**
 * The state machine will fire the {@link StateListener} when state transition
 */
public interface StateListener {

    void stateChange(StateEvent event);
}
