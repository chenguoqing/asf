package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFEngine;
import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.engine.ASFEngineImpl;
import com.baidu.asf.persistence.spring.SpringJdbcEntityManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;


/**
 * The spring configuration implementation of {@link ASFEngineConfiguration}
 */
public class SpringASFEngineConfiguration implements ASFEngineConfiguration, ApplicationContextAware {

    private ApplicationContext context;
    private DataSource dataSource;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ASFEngine buildEngine() {
        ASFEngineImpl engine = new ASFEngineImpl();
        SpringJdbcEntityManager entityManager = new SpringJdbcEntityManager(dataSource);
        engine.setEntityManager(entityManager);
        return engine;
    }

    @Override
    public <T> T getRefObject(String refName) {
        return (T) context.getBean(refName);
    }

    @Override
    public <T> T getRefObject(Class<T> refType) {
        return context.getBean(refType);
    }
}
