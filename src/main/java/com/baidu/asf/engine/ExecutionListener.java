package com.baidu.asf.engine;

/**
 * When doOutgoing flows to one nodeId, user can config some listeners for some logic,{@link ExecutionListener} is the
 * interface.
 */
public interface ExecutionListener {
    /**
     * Notify method
     */
    void onNode(ExecutionEvent event);
}
