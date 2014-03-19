package com.baidu.fsm.core;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Default implementation of {@link com.baidu.fsm.core.Transitions}
 */
public class DefaultTransitions implements Transitions {

    /**
     * All {@link com.baidu.fsm.core.Transition} will be pushed to stack by order, the top element is current state
     */
    private Stack<Transition> transitionEntries = new Stack<Transition>();

    /**
     * Construct a empty instance
     */
    public DefaultTransitions() {
    }

    /**
     * Deserialize the instance from byte array
     */
    public DefaultTransitions(byte[] data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("data is null.");
        }

        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Map.class);

        List<Map<String, Object>> transList = mapper.readValue(data, javaType);

        if (transList != null) {
            for (Map<String, Object> tranMap : transList) {
                transitionEntries.add(newTransition(tranMap));
            }
        }
    }

    private Transition newTransition(Map<String, Object> tranMap) {
        String toState = (String) tranMap.get("toState");
        String fromState = (String) tranMap.get("fromState");
        String event = (String) tranMap.get("event");
        Long date = (Long) tranMap.get("date");

        if (toState == null || fromState == null || event == null || date == null) {
            throw new StateMachineException("Can't construct Transition from data." + tranMap);
        }

        return new Transition(toState, fromState, event, date);
    }

    @Override
    public Stack<Transition> getTransitionStack() {
        return (Stack) transitionEntries.clone();
    }

    /**
     * Add transition entry to transition table
     */
    @Override
    public void addTransition(Transition transition) {
        transitionEntries.push(transition);
    }

    @Override
    public byte[] serialize() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsBytes(transitionEntries);
        } catch (IOException e) {
            //ignore
        }
        return null;
    }
}
