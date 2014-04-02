package com.baidu.asf.engine;

/**
 * Test helper
 */
public class LocalVariables {

    public static class TestVariable {
        public TestVariable() {
            this(false, false, false, false);
        }

        public TestVariable(boolean selfTest, boolean qaTest, boolean opTest, boolean scmTest) {
            this.selfTest = selfTest;
            this.qaTest = qaTest;
            this.opTest = opTest;
            this.scmTest = scmTest;
        }

        public final boolean selfTest;
        public final boolean qaTest;
        public final boolean opTest;
        public final boolean scmTest;
    }

    public static final ThreadLocal<TestVariable> localVariables = new ThreadLocal<TestVariable>() {
        @Override
        protected TestVariable initialValue() {
            return new TestVariable();
        }
    };
}
