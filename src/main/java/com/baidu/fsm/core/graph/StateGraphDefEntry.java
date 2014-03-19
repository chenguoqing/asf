package com.baidu.fsm.core.graph;

import com.baidu.fsm.core.EndState;
import com.baidu.fsm.core.StartState;
import com.baidu.fsm.core.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Each of state graph definition will be abstracted with {@link com.baidu.fsm.core.graph.StateGraphDefEntry},
 * any of entry can contains some sub entries.(the implementation of sub state machine)
 */
class StateGraphDefEntry {
    /**
     * Parent state machine
     */
    final StateGraphDefEntry parent;
    /**
     * State machine name
     */
    final String stateMachineName;
    /**
     * Whether the entry is the top one?(top state machine)
     */
    final boolean top;
    /**
     * Default start event instance
     */
    final State startState = new StartState();
    /**
     * Default end event instance
     */
    final State endState = new EndState();
    /**
     * All event and transition state
     */
    final Map<String, State> states = new HashMap<String, State>();
    /**
     * Associated sub entries
     */
    final Map<String, StateGraphDefEntry> subEntries = new HashMap<String, StateGraphDefEntry>();
    /**
     * Current transition state
     */
    State currentState;

    /**
     * Constructor
     *
     * @param parent           parent entry
     * @param stateMachineName state machine name
     */
    StateGraphDefEntry(StateGraphDefEntry parent, String stateMachineName) {
        this.currentState = startState;
        this.stateMachineName = stateMachineName;
        this.top = parent == null;
        this.parent = parent;
        states.put(startState.getName(), startState);
        states.put(endState.getName(), endState);
    }

    boolean hasState(String stateName) {
        return states.containsKey(stateName);
    }

    void addState(State state) {
        states.put(state.getName(), state);
    }

    void clearCurrentState() {
        this.currentState = null;
    }

    void setCurrentState(String stateName) {
        this.currentState = getState(stateName);
    }

    <T extends State> T getState(String stateName) {
        return (T) states.get(stateName);
    }

    boolean hasCurrentState() {
        return currentState != null;
    }

    void addSuccessor(String event, State state) {
        this.currentState.addSuccessor(event, state);
    }

    StateGraphDefEntry getSubEntry(String subStateMachineName) {
        return subEntries.get(subStateMachineName);
    }

    void addSubEntry(StateGraphDefEntry subEntry) {
        subEntries.put(subEntry.stateMachineName, subEntry);
    }

    /**
     * Validate the state graph, the completed graph should contain a fromStart nodeId and terminate nodeId, and,from any state nodeId,
     * it can transmit to toEnd nodeId. No any state nodeId can be isolate. The following rules will guarantee the graph is valid:
     * <p/>
     * <p><strong>For Top State Machine:</strong>
     * <li> The start state and toEnd state muse be exists</li>
     * <li> Start state can reach any state by graph</li>
     * <li> All states should has successors expect for the toEnd state</li>
     * <li> No any paths from any nodeId to start state</li>
     * <li> The successor state should not be itself</li>
     * </p>
     * <p/>
     * <p><strong>For Sub State Machine:</strong>
     * <li> The start state and toEnd state muse be exists</li>
     * <li> Start state can reach any state by graph</li>
     * <li> All states should has successors and end state <strong>must connect to a parent state </strong></li>
     * <li> No any paths from any nodeId to start state except the <strong>SubSMState</strong></li>
     * <li> The successor state should not be itself</li>
     * </p>
     */
    void validate() {

        for (StateGraphDefEntry sub : subEntries.values()) {
            sub.validate();
        }

        // marker collection,if one state has been traversed, it will be removed from this for avoiding dead-loop traversal.
        // when traversal toEnd, the collection should be empty.
        Map<String, State> markers = new HashMap<String, State>(states);

        // validate the predecessor of startState

        validateSuccessors(startState, markers);

        // There are some not reachable state from fromStart state
        if (!markers.isEmpty()) {
            throw new StateGraphDefinitionException("There are some not reachable states: %s ", markers);
        }
    }

    private void validateSuccessors(State state, Map<String, State> markers) {

        // the state has been traversed,ignore
        if (!markers.containsKey(state.getName())) {
            return;
        }

        Map<String, State> successors = state.getSuccessors();

        if (successors.isEmpty() && state != endState) {
            throw new StateGraphDefinitionException("State %s does not contain any successors.", state);
        }

        markers.remove(state.getName());

        for (State successor : successors.values()) {
            // Validate circle reference
            if (successor == state) {
                throw new StateGraphDefinitionException("Circle reference is found for state: %s", state);
            }
            validateSuccessors(successor, markers);
        }
    }
}