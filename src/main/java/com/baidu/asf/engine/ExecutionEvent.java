package com.baidu.asf.engine;

import com.baidu.asf.model.Flow;
import com.baidu.asf.model.Node;

/**
 * Created by chenguoqing01 on 14-3-14.
 */
public class ExecutionEvent {
    public final ASFInstance instance;
    public final Node from;
    public final Flow flow;
    public final Node to;

    public ExecutionEvent(ASFInstance instance, Node from, Flow flow, Node to) {
        this.instance = instance;
        this.from = from;
        this.flow = flow;
        this.to = to;
    }
}
