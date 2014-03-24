package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFEngine;
import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.engine.ASFEngineImpl;
import com.baidu.asf.engine.command.*;
import com.baidu.asf.persistence.spring.SpringJdbcEntityManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * The spring configuration implementation of {@link ASFEngineConfiguration}
 */
public class SpringASFEngineConfiguration implements ASFEngineConfiguration {

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public ASFEngine buildEngine() {
        ASFEngineImpl engine = new ASFEngineImpl();
        SpringJdbcEntityManager entityManager = new SpringJdbcEntityManager(dataSource);
        engine.setEntityManager(entityManager);
        engine.setExecutor(buildExecutor());
        return engine;
    }

    @Override
    public CommandExecutor buildExecutor() {
        CommandExecutorImpl executor = new CommandExecutorImpl();
        executor.setCommandInterceptor(buildCommandInterceptor());
        return executor;
    }

    @Override
    public CommandInterceptor buildCommandInterceptor() {

        CommandInterceptor transactionInterceptor = new SpringTransactionInterceptor(transactionManager);
        CommandInterceptor cacheInterceptor = new CacheCommandInterceptor();
        CommandInterceptor sink = new CommandSink();
        transactionInterceptor.setNext(cacheInterceptor);
        cacheInterceptor.setNext(sink);

        return transactionInterceptor;
    }
}
