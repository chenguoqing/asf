package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionEvent;
import com.baidu.asf.engine.ExecutionListener;

/**
 * Created by chenguoqing01 on 14-3-24.
 */
public class DeveloperTaskListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("Developer task:from: %s to: %s \n", event.from.getId(), event.to.getId());
    }
}
