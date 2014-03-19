package com.baidu.fsm.core.graph;

import com.baidu.fsm.core.*;

import java.util.Stack;
import java.util.UUID;

/**
 * This a convenient base class for implementing a {@link StateGraphDefinition}.
 */
public abstract class AbstractDSLGraphDef<T> implements StateGraphDefinition {
    private static final UUID uuid = UUID.randomUUID();

    private boolean started = false;
    /**
     * Top state machine entry
     */
    private StateGraphDefEntry mainEntry = new StateGraphDefEntry(null, getName());
    /**
     * State machine entry stack for DSL
     */
    private final Stack<StateGraphDefEntry> entryStack = new Stack<StateGraphDefEntry>();


    private StateGraphDefEntry getCurrentEntry() {
        return entryStack.peek();
    }

    /**
     * Change the current state machine entry to main
     */
    protected void switchToMain() {
        this.entryStack.clear();
        this.entryStack.push(mainEntry);
    }

    /**
     * Change the current state machine to sub by name
     */
    protected void switchToSub(String stateMachineName) {
        StateGraphDefEntry subEntry = entryStack.peek().getSubEntry(stateMachineName);
        if (subEntry == null) {
            throw new StateGraphDefinitionException("Not found the sub state machine: %s from %s", stateMachineName,
                    entryStack.peek().stateMachineName);
        }

        entryStack.push(subEntry);
    }

    /**
     * Constructs the start point of state graph
     */
    protected T fromStart() {

        if (started) {
            throw new StateGraphDefinitionException("fromStart has been invoked.");
        }

        started = true;
        entryStack.push(mainEntry);
        return (T) this;
    }

    /**
     * Validate the state is existing by stateMachineName and set it to current state, if the state is not existing,
     * defines a <tt>wait state</tt> instance
     *
     * @throws StateGraphDefinitionException if not found the state by stateMachineName
     */
    protected T from(String name) {
        if (name == null) {
            throw new StateGraphDefinitionException("Not found the state instance by stateMachineName:%s", name);
        }

        if (!getCurrentEntry().hasState(name)) {
            defineState(name);
        }

        getCurrentEntry().setCurrentState(name);

        return (T) this;
    }

    /**
     * Connects current state to target <tt>wait state</tt>, the associated event will be generated automatically.
     *
     * @param state target state stateMachineName
     * @return this
     */
    protected T to(String state) {
        return to(state, false);
    }

    protected T to(String state, boolean toParent) {
        return to(uuid.toString(), state, toParent);
    }

    /**
     * Connects current state to <tt> wait state</tt> by event <tt>event</tt>, creating a {@link com.baidu.fsm.core
     * .WaitState} instance if the state is not existing
     *
     * @param event event stateMachineName
     * @param state target state stateMachineName
     * @return this
     */
    protected T to(String event, String state) {
        return to(event, state, false);
    }

    protected T to(String event, String state, boolean toParent) {
        return (T) to(event, state, new WaitStateFactory(), toParent);
    }

    /**
     * Connects current state to <tt>exclusive gateway</tt> ,the associated event will be generated automatically.
     *
     * @param state target state stateMachineName
     * @return this
     */
    protected T toExclusiveGateway(String state) {
        return toExclusiveGateway(uuid.toString(), state);
    }

    /**
     * Connects current state to <tt>exclusive gateway</tt> by event identified by <tt>event</tt>,
     * creating the state if the state is not existing
     *
     * @param event event name
     * @param state target state stateMachineName
     * @return this
     */
    protected T toExclusiveGateway(String event, String state) {
        return to(event, state, false);
    }

    protected T toExclusiveGateway(String event, String state, boolean toParent) {
        return (T) to(event, state, new ExclusiveStateFactory(), toParent);
    }

    /**
     * Connects current state to <tt>parallel gateway</tt>, the associated event will be generated automatically.
     *
     * @param state       target state stateMachineName
     * @param gatewayType parallel gateway type
     * @return this
     */
    protected T toParallelGateway(String state, ParallelGateway.ParallelGatewayType gatewayType) {
        return toParallelGateway(uuid.toString(), state, gatewayType);
    }

    /**
     * Connects current state to <tt>parallel gateway</tt> by <tt>event</tt>, creating the state if its not existing.
     *
     * @param event       event stateMachineName
     * @param state       target state stateMachineName
     * @param gatewayType parallel gateway type
     * @return this
     */
    protected T toParallelGateway(String event, String state, ParallelGateway.ParallelGatewayType gatewayType) {
        return toParallelGateway(event, state, gatewayType, false);
    }

    protected T toParallelGateway(String event, String state, ParallelGateway.ParallelGatewayType gatewayType,
                                  boolean toParent) {
        return (T) to(event, state, new ParallelGateWayFactory(gatewayType), toParent);
    }

    /**
     * Connects current state to <tt>sub state machine</tt> by  event
     *
     * @param event event name(sub state machine name)
     * @return this
     */
    protected T toSubStart(String event) {
        StateGraphDefEntry sub = new StateGraphDefEntry(getCurrentEntry(), event);
        getCurrentEntry().addSubEntry(sub);

        // connect current state to SubSMState
        to(event, sub.startState, false);

        // change current entry to sub state machine
        entryStack.push(sub);
        return (T) this;
    }

    /**
     * Inner method for connecting current state to target state, if the target state don't existing,
     * will create it with the <tt>factory</tt>
     */
    private T to(String event, String state, StateFactory factory, boolean toParent) {
        if (state == null) {
            throw new IllegalArgumentException("state must not be null.");
        }

        State toState;

        if (toParent) {
            final StateGraphDefEntry current = getCurrentEntry();
            // Only the end state of sub state machine can be allowed to connect to parent state
            if (current.top || current.currentState != current.endState) {
                throw new StateGraphDefinitionException("Only the sub state machine's end state has been allowed to " +
                        "connect to one parent nodeId.");
            }

            if (entryStack.size() < 2) {
                throw new StateGraphDefinitionException("There are no enough entries in stack!");
            }

            entryStack.pop();
            StateGraphDefEntry parent = entryStack.peek();

            toState = parent.getState(state);

            if (toState == null) {
                toState = factory.createState(state);
                parent.addState(toState);
            }

            entryStack.push(current);
        } else {
            toState = getCurrentEntry().getState(state);

            if (toState == null) {
                toState = factory.createState(state);
                getCurrentEntry().addState(toState);
            }
        }

        return to(event, toState, toParent);
    }

    /**
     * Connects current state to target state and current state must be <tt>start state</tt>
     */
    protected T to(State state) {
        return to(null, state, false);
    }

    /**
     * Connects current state to target state(current---->target),it will validate the current and target state.
     */
    protected T to(String event, State state, boolean toParent) {
        if (state == null || event == null) {
            throw new IllegalArgumentException("event and target state must not be null.");
        }

        if (!getCurrentEntry().hasCurrentState()) {
            throw new StateGraphDefinitionException("Must be invoking the from()  before to(%s)", state);
        }
        final StateGraphDefEntry current = getCurrentEntry();

        // Forbidden to cycle reference
        if (current.currentState == state) {
            throw new StateGraphDefinitionException("Found the cycle reference, state:%s, event:%s", state.getName(), event);
        }

        // validate current state
        if (current.currentState instanceof EndState) {
            // Only the sub state machine's end state can be connected to other state
            if (current.top) {
                throw new StateGraphDefinitionException("Top state machine's endState can't connect to other state",
                        state);
                // The endState of sub state machine must connect to parent's state
            } else {
                StateGraphDefEntry topEntry = entryStack.peek();
                StateGraphDefEntry parent = entryStack.peek();
                entryStack.push(topEntry);

                if ((current.hasState(state.getName()) && current.getState(state.getName()) ==
                        state) || !parent.hasState(state.getName())) {
                    throw new StateGraphDefinitionException("The endState of sub state machine must connect to " +
                            "parent state.");
                }
            }
        }

        if (state instanceof EndState) {
            toEnd(event);
        } else {

            // Only the parent state can connect to sub's StartState
            if (state instanceof StartState) {
                StateGraphDefEntry sub = current.getSubEntry(event);
                if (sub == null || state != sub.startState) {
                    throw new StateGraphDefinitionException("The target state must not be start state.");
                }
            }

            current.addSuccessor(event, state);

            // While reaching the end state of sub state machine,the sub entry should be popped,
            // and the parent get into the current.
            if (toParent) {
                entryStack.pop();
            } else {
                current.setCurrentState(state.getName());
            }
        }

        return (T) this;
    }

    /**
     * Connect current state to end
     */
    protected void toEnd(String event) {
        if (event == null) {
            throw new IllegalArgumentException("state stateMachineName must not be null.");
        }

        if (!getCurrentEntry().hasCurrentState()) {
            throw new StateGraphDefinitionException("Must be invoking the from()  before to(%s)", event);
        }

        final StateGraphDefEntry current = getCurrentEntry();

        current.addSuccessor(event, current.endState);

        // if the state machine is top one, it should clear the current state;otherwise,
        // the sub state machine's end state must be connected with outer state.
        if (current.top) {
            current.clearCurrentState();
        }
    }

    /**
     * Define a wait state instance
     */
    protected State defineState(String stateName) {
        return define(stateName, new WaitStateFactory());
    }

    /**
     * Define a exclusive gateway instance
     */
    protected ExclusiveGateway defineExclusiveGateway(String stateName) {
        return define(stateName, new ExclusiveStateFactory());
    }

    /**
     * Define a parallel gateway instance
     */
    protected ParallelGateway defineParallelGateway(String stateName, ParallelGateway.ParallelGatewayType type) {
        return define(stateName, new ParallelGateWayFactory(type));
    }

    private <T extends State> T define(String stateName, StateFactory<T> stateFactory) {
        if (getCurrentEntry().hasState(stateName)) {
            throw new StateGraphDefinitionException("state %s has existed.", stateName);
        }

        T state = stateFactory.createState(stateName);

        getCurrentEntry().addState(state);
        return state;
    }

    protected void setEventDecision(String name, EventDecision decision) {
        State state = getCurrentEntry().getState(name);

        if (!(state instanceof ExclusiveGateway)) {
            throw new StateGraphDefinitionException("Can't set EventDecision for no-auto state: " + name);
        }

        ((ExclusiveGateway) state).setEventDecision(decision);
    }

    /**
     * Retrieve the state instance form current entry
     */
    protected <T extends State> T state(String name) {
        return getCurrentEntry().getState(name);
    }

    /**
     * Retrieve the current state of main entry(top state machine)
     */
    @Override
    public State getState(String stateName) {
        return mainEntry.getState(stateName);
    }

    @Override
    public State getStartState() {
        return mainEntry.startState;
    }

    @Override
    public State getEndState() {
        return mainEntry.endState;
    }

    /**
     * Build state graph
     */
    @Override
    public void buildStateGraph() {
        // buildStateGraph
        defineStateGraph();

        // validate
        mainEntry.validate();
    }

    private State getAndCreate(String name, State.StateType type) {
        State state = getCurrentEntry().getState(name);

        if (state == null) {
            state = type == State.StateType.ExclusiveGateway ? new ExclusiveGateway(name) : new WaitState(name);
            getCurrentEntry().addState(state);
        }

        return state;
    }

    /**
     * The implementer should provide the graph builder
     */
    protected abstract void defineStateGraph();

    /**
     * State factory for creating state instance by type
     */
    static interface StateFactory<T extends State> {
        T createState(String stateName);
    }

    /**
     * WaitState factory
     */
    class WaitStateFactory implements StateFactory {
        @Override
        public State createState(String stateName) {
            return getAndCreate(stateName, State.StateType.Wait);
        }
    }

    /**
     * ExclusiveGateway state factory
     */
    class ExclusiveStateFactory implements StateFactory<ExclusiveGateway> {
        @Override
        public ExclusiveGateway createState(String stateName) {
            return (ExclusiveGateway) getAndCreate(stateName, State.StateType.ExclusiveGateway);
        }
    }

    /**
     * ParallelGateway state factory
     */
    class ParallelGateWayFactory implements StateFactory<ParallelGateway> {
        final ParallelGateway.ParallelGatewayType gatewayType;

        private ParallelGateWayFactory(ParallelGateway.ParallelGatewayType gatewayType) {
            this.gatewayType = gatewayType;
        }

        @Override
        public ParallelGateway createState(String stateName) {
            State state = getCurrentEntry().getState(stateName);

            if (state == null) {
                state = new ParallelGateway(stateName, gatewayType);
                getCurrentEntry().addState(state);
            }
            return (ParallelGateway) state;
        }
    }
}
