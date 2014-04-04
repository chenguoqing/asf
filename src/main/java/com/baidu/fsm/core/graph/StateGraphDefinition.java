package com.baidu.fsm.core.graph;

import com.baidu.fsm.core.State;

/**
 * <p>
 * The state graph definition interface, the valid state graph can be traversed from <tt>startState</tt> until to <tt>endState</tt>, and
 * the following constraints should be guaranteed:
 * </p>
 * Validate the state graph, the completed graph should contain a fromStart fromNodeId and terminate fromNodeId, and,from any state fromNodeId,
 * it can transmit to toEnd fromNodeId. No any state fromNodeId can be isolate. The following rules will guarantee the graph is valid:
 * <pre>
 *     <li> The start state and toEnd state muse be exists</li>
 *     <li> Start state can reach any state by graph</li>
 *     <li> All states should have successors expect for the end state</li>
 *     <li> Start state can not serve as the successor</li>
 *     <li> Any state's successor can not be itself(circular path)</li>
 * <pre>
 */
public interface StateGraphDefinition {
    /**
     * Return the state machine name
     */
    String getName();

    /**
     * Return the state graph definition version
     */
    int getVersion();

    /**
     * Return the state machine description
     */
    String getDescription();

    /**
     * Retrieve the unique start state
     */
    State getStartState();

    /**
     * Retrieve the unique terminal state
     */
    State getEndState();

    /**
     * Retrieve the state by stateMachineName
     */
    State getState(String name);

    /**
     * Builds and validates the state graph
     */
    void buildStateGraph();
}
