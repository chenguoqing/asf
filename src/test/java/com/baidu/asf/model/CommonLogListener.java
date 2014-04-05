package com.baidu.asf.model;

import com.baidu.asf.engine.ExecutionEvent;
import com.baidu.asf.engine.ExecutionListener;
import org.springframework.stereotype.Component;

@Component("commonLogListener")
public class CommonLogListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("%s:from: %s to: %s\n", event.to.getId(), event.from.getId(), event.to.getId());
    }
}
