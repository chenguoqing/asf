package com.baidu.fsm.spring;

import com.baidu.fsm.core.StateMachine;
import com.baidu.fsm.core.StateMachineFactory;
import com.baidu.fsm.core.graph.StateGraphDefinition;
import com.baidu.fsm.storage.StatePersist;
import com.baidu.fsm.storage.db.SpringDataSourcePersist;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;

/**
 * {@link com.baidu.fsm.spring.StateMachineBuilderFactoryBean} should be used in spring context
 */
public class StateMachineBuilderFactoryBean extends AbstractFactoryBean<StateMachineBuilder> {

    /**
     * State graph definition class
     */
    private StateGraphDefinition definition;
    /**
     * State persistence instance
     */
    private StatePersist persist;
    /**
     * Data source
     */
    private DataSource dataSource;

    public void setDefinitionClass(StateGraphDefinition definition) {
        this.definition = definition;
    }

    public void setPersist(StatePersist persist) {
        this.persist = persist;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return StateMachineBuilderImpl.class;
    }

    @Override
    protected StateMachineBuilder createInstance() throws Exception {
        return new StateMachineBuilderImpl(definition, persist);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (definition == null) {
            throw new IllegalArgumentException("StateGraphDefinition class instance must be set.");
        }

        if (persist == null && dataSource == null) {
            throw new IllegalArgumentException("StatePersist instance or dataSource must be set.");
        }

        if (persist == null) {
            this.persist = new SpringDataSourcePersist();
            ((SpringDataSourcePersist) this.persist).setDataSource(dataSource);
        }

        super.afterPropertiesSet();
    }

    static class StateMachineBuilderImpl implements StateMachineBuilder {

        final StateGraphDefinition definition;
        final StatePersist persist;

        StateMachineBuilderImpl(StateGraphDefinition definition, StatePersist persist) {
            this.definition = definition;
            this.persist = persist;
        }

        @Override
        public StateMachine createStateMachine() {
            return StateMachineFactory.createStateMachine(definition, persist);
        }

        @Override
        public StateMachine getStateMachine(long id) {
            return StateMachineFactory.createStateMachine(id, definition, persist);
        }
    }
}
