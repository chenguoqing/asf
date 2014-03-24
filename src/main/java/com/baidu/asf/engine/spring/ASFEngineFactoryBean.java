package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFEngine;
import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.engine.ASFInstance;
import com.baidu.asf.model.ASFDefinition;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Map;

/**
 * Helper class for spring context
 */
public class ASFEngineFactoryBean extends AbstractFactoryBean<ASFEngineFactoryBean.SpringASFEngineProxy> {
    private ASFDefinition definition;
    private ASFEngineConfiguration engineConfiguration;

    public void setDefinition(ASFDefinition definition) {
        this.definition = definition;
    }

    public void setEngineConfiguration(ASFEngineConfiguration engineConfiguration) {
        this.engineConfiguration = engineConfiguration;
    }

    @Override
    public Class<?> getObjectType() {
        return SpringASFEngineProxy.class;
    }

    @Override
    protected SpringASFEngineProxy createInstance() throws Exception {
        return new SpringASFEngineProxy(definition, engineConfiguration.buildEngine());
    }

    static class SpringASFEngineProxy implements ASFEngineProxy {
        final ASFDefinition definition;
        final ASFEngine engine;

        SpringASFEngineProxy(ASFDefinition definition, ASFEngine engine) {
            this.definition = definition;
            this.engine = engine;
        }

        @Override
        public ASFInstance startASFInstance() {
            return engine.startASFInstance(definition);
        }

        @Override
        public ASFInstance startASFInstance(Map<String, Object> variables) {
            return engine.startASFInstance(definition, variables);
        }

        @Override
        public ASFInstance findASFInstance(long id) {
            return engine.findASFInstance(definition, id);
        }
    }
}
