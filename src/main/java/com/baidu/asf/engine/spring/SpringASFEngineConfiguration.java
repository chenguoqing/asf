package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFEngine;
import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.engine.ASFEngineImpl;
import com.baidu.asf.persistence.spring.SpringJdbcEntityManager;

import javax.sql.DataSource;


/**
 * The spring configuration implementation of {@link ASFEngineConfiguration}
 */
public class SpringASFEngineConfiguration implements ASFEngineConfiguration {

    private DataSource dataSource;

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
}
