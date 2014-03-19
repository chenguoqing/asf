package com.baidu.asf.engine.command;

import com.baidu.asf.engine.ProcessorContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Variable operation command
 */
public class UpdateVariableCommand implements Command<Integer> {
    /**
     * Variable name
     */
    private final Map<String, Object> variables = new HashMap<String, Object>();
    /**
     * Operation type
     */
    private final int type;

    public UpdateVariableCommand(Map<String, Object> variables, int type) {
        if (variables != null) {
            this.variables.putAll(variables);
        }
        this.type = type;
    }

    @Override
    public Integer execute(ProcessorContext context) {
        return null;
    }
}
