package com.baidu.asf.engine;

/**
 * Created by chenguoqing01 on 14-3-24.
 */
public class InclusiveTestListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("%s:from: %s to: %s\n", event.to.getId(), event.from.getId(), event.to.getId());
    }
}
