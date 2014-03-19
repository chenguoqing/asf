package com.baidu.fsm.core;

import com.baidu.fsm.core.graph.AbstractDSLGraphDef;

/**
 * Created by chenguoqing01 on 14-2-12.
 */
public class TestDef2 extends AbstractDSLGraphDef<TestDef2> {

    @Override
    public String getName() {
        return "test2";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    protected void defineStateGraph() {

        fromStart().to("a").toExclusiveGateway("e1", "b");
        from("b").to("e3", "e").to("e6", "g").toEnd("e9");
        from("b").to("e4", "f").to("e7", "g");
        from("a").to("e2", "c").to("e5", "d").toEnd("e8");

        state("b").addStateListener(new StateListener() {
            @Override
            public void stateChange(StateEvent event) {
//                System.out.printf("Current state:%s,nodeId state:%s,event:%s\n", event.to.getName(),
//                        event.from.getName(), event.event);
            }
        });

//        setEventDecision("b", new EventDecision() {
//            @Override
//            public String decideEvent(StateMachine stateMachine) {
//                return "e3";
//            }
//        });
    }
}
