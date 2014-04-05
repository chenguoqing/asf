package com.baidu.asf.engine;

import com.baidu.asf.engine.spring.ASFEngineProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TxServiceImpl implements TxService {

    @Autowired
    private ASFEngineProxy engineProxy;

    @Override
    @Transactional
    public void doLogic() {
        engineProxy.startASFInstance();
    }

    @Override
    @Transactional
    public void doLogicWithException() {
        engineProxy.startASFInstance();
        throw new RuntimeException("test exception!");
    }
}
