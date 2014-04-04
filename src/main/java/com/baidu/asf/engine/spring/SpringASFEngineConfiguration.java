package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ASFEngine;
import com.baidu.asf.engine.ASFEngineConfiguration;
import com.baidu.asf.engine.ASFEngineImpl;
import com.baidu.asf.engine.command.ASFEntranceInterceptor;
import com.baidu.asf.engine.command.CacheCommandInterceptor;
import com.baidu.asf.engine.command.CommandInterceptor;
import com.baidu.asf.engine.command.CommandSink;
import com.baidu.asf.persistence.spring.SpringJdbcEntityManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


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
        return engine;
    }

    @Override
    public CommandInterceptor buildCommandInterceptor() {
        List<CommandInterceptor> interceptors = new ArrayList<CommandInterceptor>();
        CommandInterceptor localResourceInterceptor = new ASFEntranceInterceptor();
        CommandInterceptor transactionInterceptor = new SpringTransactionInterceptor(transactionManager);
        CommandInterceptor cacheInterceptor = new CacheCommandInterceptor();
        CommandInterceptor sink = new CommandSink();

        interceptors.add(localResourceInterceptor);
        interceptors.add(transactionInterceptor);
        interceptors.add(sink);

        for (int i = 1; i < interceptors.size(); i++) {
            CommandInterceptor prev = interceptors.get(i - 1);
            CommandInterceptor curr = interceptors.get(i);
            prev.setNext(curr);
        }
        return transactionInterceptor;
    }
}
