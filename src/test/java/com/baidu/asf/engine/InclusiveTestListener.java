package com.baidu.asf.engine;

import com.baidu.asf.model.InclusiveGateway;

/**
 * Created by chenguoqing01 on 14-3-24.
 */
public class InclusiveTestListener implements ExecutionListener {
    @Override
    public void onNode(ExecutionEvent event) {
        System.out.printf("%s:from: %s to: %s\n", event.to.getId(), event.from.getId(), event.to.getId());

        if (event.to instanceof InclusiveGateway && event.to.getId().equals("joinGateway")) {

            if (event.from.getId().equals("selfTest")) {
                event.instance.setVariable("selfTestPass", LocalVariables.localVariables.get().selfTest);
            } else if (event.from.getId().equals("qaTest")) {
                event.instance.setVariable("qaTestPass", LocalVariables.localVariables.get().qaTest);
            } else if (event.from.getId().equals("opTest")) {
                event.instance.setVariable("opTestPass", LocalVariables.localVariables.get().opTest);
            } else if (event.from.getId().equals("scmTest")) {
                event.instance.setVariable("scmTestPass", LocalVariables.localVariables.get().scmTest);
            }
        }
    }
}
