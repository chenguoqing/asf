package com.baidu.model;

import com.baidu.asf.engine.ExecutionEvent;
import com.baidu.asf.engine.ExecutionListener;

/**
 * Test listener
 */
public class LogIssueListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("Log Issue:from: %s to: %s\n", event.from.getId(), event.to.getId());
    }
}
