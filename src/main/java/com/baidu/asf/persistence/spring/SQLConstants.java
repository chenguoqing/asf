package com.baidu.asf.persistence.spring;

import com.baidu.asf.persistence.enitity.VariableEntity;

/**
 * SQL Collection.
 */
public class SQLConstants {
    /**
     * Create instance
     */
    public static final String ASF_CREATE_INSTANCE = "INSERT INTO ASF_INSTANCE(DEF_NAME_,DEF_VERSION_," +
            "DEF_CLASS_NAME_,STATUS_,VERSION_,GMT_CREATE,GMT_MODIFIED) VALUES(?,?,?,?,?,SYSDATE(),SYSDATE())";

    /**
     * Load instance by id
     */
    public static final String ASF_LOAD_INSTANCE = "SELECT ID_,DEF_NAME_,DEF_VERSION_,DEF_CLASS_NAME_,STATUS_," +
            "VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_INSTANCE WHERE ID=?";

    /**
     * Load all instances(on processing)
     */
    public static final String ASF_LOAD_ALL_INSTANCES = "SELECT ID_,DEF_NAME_,DEF_VERSION_,DEF_CLASS_NAME_,STATUS_," +
            "VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_INSTANCE";

    /**
     * Update instance status
     */
    public static final String ASF_UPDATE_INSTANCE_STATUS = "UPDATE ASF_INSTANCE SET VERSION_=?," +
            "STATUS_=? WHERE ID_=? AND VERSION_=?";

    /**
     * ASF create execution
     */
    public static final String ASF_CREATE_EXECUTION = "INSERT INTO ASF_EXECUTION (INSTANCE_ID_,ACT_FULL_ID_," +
            "ACT_TYPE_,GMT_CREATED,GMT_MODIFIED)VALUES(?,?,?,SYSDATE(),SYSDATE())";

    /**
     * Load execution entity
     */
    public static final String ASF_LOAD_EXECUTION = "SELECT ID_,INSTANCE_ID_,ACT_FULL_ID_,ACT_TYPE_,GMT_CREATED," +
            "GMT_MODIFIED FROM ASF_EXECUTION WHERE ID=?";

    /**
     * Delete execution
     */
    public static final String ASF_DELETE_EXECUTION = "DELETE ASF_EXECUTION WHERE ID_=?";
    /**
     * Delete all executions associated with instance
     */
    public static final String ASF_DELETE_EXECUTIONS = "DELETE ASF_EXECUTION WHERE INSTANCE_ID_=?";
    /**
     * Load execution entity
     */
    public static final String ASF_FIND_EXECUTIONS = "SELECT ID_,INSTANCE_ID_,ACT_FULL_ID_,ACT_TYPE_,GMT_CREATED," +
            "GMT_MODIFIED FROM ASF_EXECUTION WHERE INSTANCE_ID=?";

    /**
     * Create transition entity
     */
    public static final String ASF_CREATE_TRANSITION = "INSERT INTO ASF_TRANSITION(INSTANCE_ID_,FROM_ACT_FULL_ID_," +
            "TO_ACT_FULL_ID_,FLOW_ID_,FLOW_VIRTUAL_,FROM_ACT_TYPE_,TO_ACT_TYPE_,GMT_CREATE,GMT_MODIFIED)VALUES(?,?,?," +
            "?,?,?,?,SYSDATE(),SYSDATE())";
    /**
     * Query all transitions for instance
     */
    public static final String ASF_FIND_TRANSITIONS = "SELECT ID_,INSTANCE_ID_,FROM_ACT_FULL_ID_,TO_ACT_FULL_ID_," +
            "FLOW_ID_,FLOW_VIRTUAL_,FROM_ACT_TYPE_,TO_ACT_TYPE_,GMT_CREATE,GMT_MODIFIED FROM ASF_TRANSITION WHERE " +
            "INSTANCE_ID_=?";

    /**
     * Create variable
     */
    public static final String ASF_CREATE_VARIABLE = "INSERT INTO ASF_VARIABLE (INSTANCE_ID_,NAME_,DOUBLE_,LONG_," +
            "STRING_,OBJECT_,TYPE_,CLASS_,VERSION_,GMT_CREATED,GMT_MODIFIED)VALUES(?,?,?,?,?,?,?,?,?,SYSDATE()," +
            "SYSDATE())";

    /**
     * Load variable
     */
    public static final String ASF_LOAD_VARIABLE = "SELECT ID_,INSTANCE_ID_,NAME_,DOUBLE_,LONG_,STRING_,OBJECT_," +
            "TYPE_,CLASS_,VERSION_,GMT_CREATED,GMT_MODIFIED FROM ASF_VARIABLE WHERE INSTANCE_ID_=? AND NAME_=? AND " +
            "CLASS_=?";

    public static String getUpdateVariableSQL(VariableEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ASF_VARIABLE SET ");
        if (entity.getType() == VariableEntity.VariableType.DOUBLE) {
            sb.append("DOUBLE_=?");
        } else if (entity.getType() == VariableEntity.VariableType.LONG) {
            sb.append("LONG_=?");
        } else if (entity.getType() == VariableEntity.VariableType.STRING) {
            sb.append("STRING_=?");
        } else {
            sb.append("OBJECT_=?");
        }

        sb.append(",");
        sb.append("SET TYPE_=?");
        sb.append(",");
        sb.append("SET VERSION_=?");
        sb.append("WHERE ID_=? AND VERSION_=?");
        return sb.toString();
    }

    /**
     * Delete variable
     */
    public static final String ASF_DELETE_VARIABLE = "DELETE ASF_VARIABLE WHERE ID_=? AND VERSION_=?";

    public static final String ASF_FIND_VARIABLES = "SELECT ID_,INSTANCE_ID_,NAME_,DOUBLE_,LONG_,STRING_,OBJECT_," +
            "TYPE_,CLASS_,VERSION_,GMT_CREATED,GMT_MODIFIED FROM ASF_VARIABLE WHERE INSTANCE_ID_=?";
}
