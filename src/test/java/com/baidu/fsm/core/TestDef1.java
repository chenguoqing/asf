package com.baidu.fsm.core;

import com.baidu.fsm.core.graph.AbstractDSLGraphDef;

/**
 * Created by chenguoqing01 on 14-2-12.
 */
public class TestDef1 extends AbstractDSLGraphDef<TestDef1> {

    @Override
    public String getName() {
        return "test1";
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
        fromStart().to("a").to("e1", "b");
        from("b").to("e3", "e").to("e6", "g").toEnd("e9");
        from("b").to("e4", "f").to("e7", "g");
        from("a").to("e2", "c").to("e5", "d").toEnd("e8");
    }
}
