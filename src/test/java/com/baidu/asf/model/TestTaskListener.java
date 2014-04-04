package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionEvent;
import com.baidu.asf.engine.ExecutionListener;

/**
 * Created by chenguoqing01 on 14-3-26.
 */
public class TestTaskListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("%s:from: %s to: %s\n", event.to.getId(), event.from.getId(), event.to.getId());
        event.instance.setVariable("solutionApproved", true);
    }
}
