package com.baidu.asf.persistence.enitity;

import com.baidu.asf.util.ObjectLazyLoader;

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
        BOOLEAN(3),
        OBJECT(4);

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

        public static VariableClass get(int value) {
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
     * Object stream
     */
    private byte[] objBytes;
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
        this.type = VariableType.DOUBLE;
    }

    public Long getLong() {
        return lValue;
    }

    public void setLong(Long lValue) {
        this.lValue = lValue;
        this.type = VariableType.LONG;
    }

    public String getString() {
        return sValue;
    }

    public void setString(String sValue) {
        this.sValue = sValue;
        this.type = VariableType.STRING;
    }

    public void setBoolean(boolean bValue) {
        this.lValue = bValue ? 1L : 0;
        this.type = VariableType.BOOLEAN;
    }

    public boolean getBoolean() {
        return lValue == 1L;
    }

    public Object getObject() {
        return oValue;
    }

    public void setObject(Object oValue) {
        this.oValue = oValue;
        this.type = VariableType.OBJECT;
    }

    public void setVariable(String name, Object value) {
        if (value instanceof String) {
            setString((String) value);
        } else if (value instanceof Character) {
            setString(value.toString());
        } else if (value instanceof Byte) {
            setLong(((Byte) value).longValue());
        } else if (value instanceof Integer) {
            setLong(((Integer) value).longValue());
        } else if (value instanceof Long) {
            setLong((Long) value);
        } else if (value instanceof Float) {
            setDouble(((Float) value).doubleValue());
        } else if (value instanceof Boolean) {
            setBoolean((Boolean) value);
        } else if (value instanceof Double) {
            setDouble((Double) value);
        } else {
            setObject(value);
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

        if (type == VariableType.BOOLEAN) {
            return lValue == 1L;
        }

        if (type == VariableType.OBJECT) {
            if (oValue instanceof ObjectLazyLoader) {
                return ((ObjectLazyLoader) oValue).getObject();
            }
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

    public void setVariableClass(VariableClass variableClass) {
        this.variableClass = variableClass;
    }

    public VariableClass getVariableClass() {
        return variableClass;
    }
}
