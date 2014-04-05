package com.baidu.asf.model;

/**
 * {@link InclusiveGateway} is the combination of {@link com.baidu.asf.model.ParallelGateway} and {@link com.baidu
 * .asf.model.ExclusiveGateway},you can define the conditions on the inclusive gateway,
 * but the inclusive gateway will can take more than one sequence flow by the condition evaluation. Like the parallel
 * gateway, inclusive gateway has <tt>fork</tt> and <tt>join</tt> behaviours.
 * <pre>
 *     <li><b>fork</b>: inclusive gateway will fork multiple sequence flows by condition evaluation</li>
 *     <li><b>join</b>: inclusive gateway will only wait for incoming sequence flows that can be executed.(if some of
 *     sequence flows are ignored from fork,they are no-execution.)</li>
 * </pre>
 * The process definition may be very complicated,the <b>join</b> behaviour is difficult to decide which incoming
 * flows are "no-execution",so the inclusive gateway should be used only as the for/join pairs.
 */
public class InclusiveGateway extends AbstractNode implements Gateway {
    public InclusiveGateway() {
        setActType(NodeType.InclusiveGateway);
    }
}
