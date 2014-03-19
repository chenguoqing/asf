package com.baidu.fsm.core;

import com.baidu.fsm.spring.StateMachineBuilder;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

/**
 * Created by chenguoqing01 on 14-2-3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestMachine1 {

    @Resource(name = "sm1")
    private StateMachineBuilder builder;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${jdbc.driverClassName}")
    private String jdbcDriverName;

    private long id;

//    @Before
    public void init() throws Exception {
        InputStream in = new DefaultResourceLoader().getResource("schema.sql").getInputStream();
        byte[] b = new byte[512];
        int count = in.read(b);

        final String schemaSQL = new String(b, 0, count);

        jdbcTemplate.execute(new StatementCallback<Object>() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
                stmt.execute(schemaSQL);
                return null;
            }
        });

        StateMachine sm = builder.createStateMachine();
        sm.start();
        id = sm.getId();
    }

//    @After
    public void destory() {
        if (jdbcDriverName.equals("org.h2.Driver")) {
            return;
        }
        StateMachine sm = builder.getStateMachine(id);
        sm.destroy();
    }

//    @Test
    public void testNext1() {
        StateMachine sm = builder.getStateMachine(id);
        assertStateMachine("a", 1, null);
        sm.transit("e1");
        assertStateMachine("b", 2, new Transition("b", "a", "e1"));
        sm.transit("e4");
        sm.transit("e7");
        sm.transit("e9");
        assertStateMachine("__end__", 5, new Transition("__end__", "g", "e9"));
    }

    private void assertStateMachine(String state, int transitionCount, Transition transition) {
        StateMachine sm = builder.getStateMachine(id);
        Assert.assertNotNull(sm);
        Assert.assertNotNull(sm.getTransitions());
        Assert.assertEquals(sm.getState().getName(), state);
        Transitions transitions = sm.getTransitions();
        Assert.assertNotNull(transitions);
        Assert.assertEquals(transitions.getTransitionStack().size(), transitionCount);
        Stack<Transition> stack = transitions.getTransitionStack();
        Transition transition1 = stack.pop();

        if (transition != null) {
            Assert.assertEquals(transition1.toState, transition.toState);
            Assert.assertEquals(transition1.fromState, transition.fromState);
            Assert.assertEquals(transition1.event, transition.event);
        }
    }
}
