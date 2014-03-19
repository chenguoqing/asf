package com.baidu.asf.persistence.enitity;

/**
 * Variable entity
 */
public class VariableEntity extends Entity {
    /**
     * Variable type
     */
    public static enum VariableType {
        STRING(0),
        LONG(1),
        DOUBLE(2),
        OBJECT(3);

        public final int value;

        VariableType(int value) {
            this.value = value;
        }

        public static VariableType get(int value) {
            if (value < STRING.value || value > OBJECT.value) {
                throw new IllegalArgumentException("Invalidate value:" + value);
            }
            return values()[value];
        }
    }

    public static enum VariableClass {
        USER(0),
        SYSTEM(1);

        public final int value;

        VariableClass(int value) {
            this.value = value;
        }

        static VariableClass get(int value) {
            if (value < USER.value || value > SYSTEM.value) {
                throw new IllegalArgumentException("Invalidate value:" + value);
            }
            return values()[value];
        }
    }

    public VariableEntity() {
    }

    public VariableEntity(long instanceId, String name, Object value) {
        this(instanceId, name, value, VariableClass.USER);
    }

    public VariableEntity(long instanceId, String name, Object value, VariableClass variableClass) {
        this.instanceId = instanceId;
        setVariable(name, value);
        this.variableClass = variableClass;
    }

    /**
     * Associated asf instance id
     */
    private long instanceId;
    /**
     * Variable name
     */
    private String name;
    /**
     * Double value
     */
    private Double dValue;
    /**
     * Long value
     */
    private Long lValue;
    /**
     * String value
     */
    private String sValue;
    /**
     * Object value
     */
    private Object oValue;
    /**
     * Variable type
     */
    private VariableType type;
    /**
     * Variable class
     */
    private VariableClass variableClass;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDouble() {
        return dValue;
    }

    public void setDouble(Double dValue) {
        this.dValue = dValue;
    }

    public Long getLong() {
        return lValue;
    }

    public void setLong(Long lValue) {
        this.lValue = lValue;
    }

    public String getString() {
        return sValue;
    }

    public void setString(String sValue) {
        this.sValue = sValue;
    }

    public Object getObject() {
        return oValue;
    }

    public void setObject(Object oValue) {
        this.oValue = oValue;
    }

    public void setVariable(String name, Object value) {
        if (value instanceof String) {
            this.sValue = (String) value;
            this.type = VariableType.STRING;
        } else if (value instanceof Long) {
            this.lValue = (Long) value;
            this.type = VariableType.LONG;
        } else if (value instanceof Double) {
            this.dValue = (Double) value;
            this.type = VariableType.DOUBLE;
        } else {
            this.oValue = value;
            this.type = VariableType.OBJECT;
        }
        this.name = name;
    }

    public Object getValue() {
        if (type == VariableType.LONG) {
            return lValue;
        }

        if (type == VariableType.DOUBLE) {
            return dValue;
        }

        if (type == VariableType.OBJECT) {
            return oValue;
        }

        return sValue;
    }

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    public VariableClass getVariableClass() {
        return variableClass;
    }

    public void setVariableClass(VariableClass variableClass) {
        this.variableClass = variableClass;
    }
}
