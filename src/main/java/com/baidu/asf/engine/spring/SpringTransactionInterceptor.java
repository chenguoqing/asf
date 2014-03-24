package com.baidu.asf.engine.spring;

import com.baidu.asf.engine.ProcessorContext;
import com.baidu.asf.engine.command.AbstractCommandInterceptor;
import com.baidu.asf.engine.command.Command;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Spring transaction interceptor
 */
public class SpringTransactionInterceptor extends AbstractCommandInterceptor {

    private final PlatformTransactionManager transactionManager;

    public SpringTransactionInterceptor(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Start transaction by propagation
     */
    @Override
    public <T> T execute(final ProcessorContext context, final Command<T> command) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);

        Object level = context.getParam(ProcessorContext.ParamKeys.TransactionPropagation.paramName);

        if (level instanceof Integer) {
            template.setPropagationBehavior((Integer) level);
        }

        return template.execute(new TransactionCallback<T>() {
            @Override
            public T doInTransaction(TransactionStatus status) {
                return next.execute(context, command);
            }
        });
    }
}
