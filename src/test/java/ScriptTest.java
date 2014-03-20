import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;

/**
 * Created by chenguoqing01 on 14-3-19.
 */
public class ScriptTest {

    //    @Test
    public void testJs() throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("javascript");

        engine.put("a", 234);

        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            engine.eval("a>123");
        }

        System.out.printf("Used time:%d\n", (System.currentTimeMillis() - t));
    }

    @Test
    public void testJEXL() throws Exception {
        // Create or retrieve a JexlEngine
        JexlEngine jexl = new JexlEngine();
        // Create an expression object
        String jexlExp = "list.isEmpty()";
        Expression e = jexl.createExpression(jexlExp);

        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set("a", 1234);
        jc.set("list", new ArrayList<Object>());

        long t = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            // Now evaluate the expression, getting the result
            Object o = e.evaluate(jc);
            System.out.println(o);
        }
        System.out.printf("Used time:%d\n", (System.currentTimeMillis() - t));
    }
}
