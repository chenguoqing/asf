package com.baidu.asf.model;

import com.baidu.asf.engine.ASFInstance;
import com.baidu.asf.engine.spring.ASFEngineProxy;
import com.baidu.asf.expression.JEXLConditionExpression;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf.xml")
public class VariableTest {
    @Autowired
    private ASFEngineProxy engineProxy;

    @Test
    public void testVariable() {
        ASFInstance instance = engineProxy.findASFInstance(30);

        // assert boolean
        instance.setVariable("b", true);
        Boolean b = (Boolean) instance.getVariable("b");
        Assert.assertTrue(b);

        // assert string
        instance.setVariable("name", "john");
        String name = (String) instance.getVariable("name");
        Assert.assertNotNull(name);
        Assert.assertEquals(name, "john");

        // assert integer
        instance.setVariable("age", 23);
        Long age = (Long) instance.getVariable("age");
        Assert.assertNotNull(age);
        Assert.assertEquals(age.intValue(), 23);

        // assert double
        instance.setVariable("score", 98.5);
        Double score = (Double) instance.getVariable("score");
        Assert.assertNotNull(score);
        Assert.assertTrue(score.compareTo(98.5) == 0);

        // assert object
        TestObject to = new TestObject("dd", 22);
        instance.setVariable("obj", to);
        Object result = instance.getVariable("obj");
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestObject);
        Assert.assertEquals(to.f1, ((TestObject) result).f1);
        Assert.assertEquals(to.f2, ((TestObject) result).f2);

        instance.setVariable("obj", 5.6);
        result = instance.getVariable("obj");
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Double);
        Assert.assertTrue(((Double) result).compareTo(5.6) == 0);
    }

    @Test
    public void testExpression() {
        ASFInstance instance = engineProxy.findASFInstance(30);
        instance.setVariable("b", true);
        instance.setVariable("name", "john");
        instance.setVariable("age", 23);
        instance.setVariable("score", 98.5);
        TestObject to = new TestObject("dd", 22);
        instance.setVariable("obj", to);
        JEXLConditionExpression expression1 = new JEXLConditionExpression("age==23");
        Assert.assertTrue(expression1.evaluate(instance));

        JEXLConditionExpression expression2 = new JEXLConditionExpression("score==98.5");
        Assert.assertTrue(expression2.evaluate(instance));

        JEXLConditionExpression expression3 = new JEXLConditionExpression("name=='john'");
        Assert.assertTrue(expression3.evaluate(instance));

        JEXLConditionExpression expression4 = new JEXLConditionExpression("obj.getF1()=='dd'");
        Assert.assertTrue(expression4.evaluate(instance));

        JEXLConditionExpression expression5 = new JEXLConditionExpression("b");
        Assert.assertTrue(expression5.evaluate(instance));

        JEXLConditionExpression expression6 = new JEXLConditionExpression("!b");
        Assert.assertFalse(expression6.evaluate(instance));
    }

    public static class TestObject implements Serializable {
        public String f1;
        public int f2;

        TestObject(String f1, int f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public String getF1() {
            return f1;
        }

        public int getF2() {
            return f2;
        }
    }
}
