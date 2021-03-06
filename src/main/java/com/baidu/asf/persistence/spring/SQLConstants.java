package com.baidu.asf.persistence.spring;

import com.baidu.asf.persistence.enitity.VariableEntity;

/**
 * SQL Collection.
 */
public class SQLConstants {
    /**
     * Create instance
     */
    public static final String ASF_CREATE_INSTANCE = "INSERT INTO ASF_INSTANCE(DEF_ID_,DEF_VERSION_," +
            "STATUS_,VERSION_,GMT_CREATE,GMT_MODIFIED) VALUES(?,?,?,?,SYSDATE(),SYSDATE())";

    /**
     * Load instance by id
     */
    public static final String ASF_LOAD_INSTANCE = "SELECT ID_,DEF_ID_,DEF_VERSION_,STATUS_," +
            "VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_INSTANCE WHERE ID_=?";

    /**
     * Load all instances(on processing)
     */
    public static final String ASF_LOAD_ALL_INSTANCES = "SELECT ID_,DEF_ID_,DEF_VERSION_,STATUS_," +
            "VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_INSTANCE";

    /**
     * Update instance status
     */
    public static final String ASF_UPDATE_INSTANCE_STATUS = "UPDATE ASF_INSTANCE SET STATUS_=?," +
            "VERSION_=? WHERE ID_=? AND VERSION_=?";

    /**
     * ASF create execution
     */
    public static final String ASF_CREATE_EXECUTION = "INSERT INTO ASF_EXECUTION (INSTANCE_ID_,NODE_FULL_ID_," +
            "NODE_TYPE_,GMT_CREATE,GMT_MODIFIED)VALUES(?,?,?,SYSDATE(),SYSDATE())";

    /**
     * Load execution entity
     */
    public static final String ASF_LOAD_EXECUTION = "SELECT ID_,INSTANCE_ID_,NODE_FULL_ID_,NODE_TYPE_,GMT_CREATE," +
            "GMT_MODIFIED FROM ASF_EXECUTION WHERE ID_=?";

    /**
     * Delete execution
     */
    public static final String ASF_DELETE_EXECUTION = "DELETE FROM ASF_EXECUTION WHERE ID_=?";
    /**
     * Delete all executions associated with instance
     */
    public static final String ASF_DELETE_EXECUTIONS = "DELETE FROM ASF_EXECUTION WHERE INSTANCE_ID_=?";
    /**
     * Load execution entity
     */
    public static final String ASF_FIND_EXECUTIONS = "SELECT ID_,INSTANCE_ID_,NODE_FULL_ID_,NODE_TYPE_,GMT_CREATE," +
            "GMT_MODIFIED FROM ASF_EXECUTION WHERE INSTANCE_ID_=?";

    /**
     * Create transition entity
     */
    public static final String ASF_CREATE_TRANSITION = "INSERT INTO ASF_TRANSITION(INSTANCE_ID_,SOURCE_REF_," +
            "TARGET_REF_,FLOW_VIRTUAL_,SOURCE_NODE_TYPE_,TARGET_NODE_TYPE_,GMT_CREATE,GMT_MODIFIED)VALUES(?,?,?," +
            "?,?,?,SYSDATE(),SYSDATE())";
    /**
     * Query all transitions for instance
     */
    public static final String ASF_FIND_TRANSITIONS = "SELECT ID_,INSTANCE_ID_,SOURCE_REF_,TARGET_REF_," +
            "FLOW_VIRTUAL_,SOURCE_NODE_TYPE_,TARGET_NODE_TYPE_,GMT_CREATE,GMT_MODIFIED FROM ASF_TRANSITION WHERE " +
            "INSTANCE_ID_=?";

    /**
     * Create variable
     */
    public static final String ASF_CREATE_VARIABLE = "INSERT INTO ASF_VARIABLE (INSTANCE_ID_,NAME_,DOUBLE_,LONG_," +
            "STRING_,OBJECT_,TYPE_,CLASS_,VERSION_,GMT_CREATE,GMT_MODIFIED)VALUES(?,?,?,?,?,?,?,?,?,SYSDATE()," +
            "SYSDATE())";

    public static String getCreateVariableSQL(VariableEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ASF_VARIABLE (INSTANCE_ID_,NAME_,");

        final VariableEntity.VariableType type = entity.getType();

        if (type == VariableEntity.VariableType.LONG || type == VariableEntity.VariableType.BOOLEAN) {
            sb.append("LONG_");
        } else if (type == VariableEntity.VariableType.DOUBLE) {
            sb.append("DOUBLE_");
        } else if (type == VariableEntity.VariableType.STRING) {
            sb.append("STRING_");
        } else {
            sb.append("OBJECT_");
        }
        sb.append(",");
        sb.append("TYPE_,CLASS_,VERSION_,GMT_CREATE,GMT_MODIFIED)VALUES(?,?,?,?,?,?,SYSDATE(),SYSDATE())");
        return sb.toString();
    }

    /**
     * Load variable
     */
    public static final String ASF_LOAD_VARIABLE = "SELECT ID_,INSTANCE_ID_,NAME_,DOUBLE_,LONG_,STRING_,OBJECT_," +
            "TYPE_,CLASS_,VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_VARIABLE WHERE INSTANCE_ID_=? AND NAME_=? AND " +
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
        sb.append(" TYPE_=?");
        sb.append(",");
        sb.append(" VERSION_=?");
        sb.append(",");
        sb.append(" GMT_MODIFIED=SYSDATE()");
        sb.append(" WHERE ID_=? AND VERSION_=?");
        return sb.toString();
    }

    /**
     * Delete variable
     */
    public static final String ASF_DELETE_VARIABLE = "DELETE FROM ASF_VARIABLE WHERE INSTANCE_ID_=? AND NAME_=? AND " +
            "CLASS_=?";

    public static final String ASF_FIND_VARIABLES = "SELECT ID_,INSTANCE_ID_,NAME_,DOUBLE_,LONG_,STRING_,OBJECT_," +
            "TYPE_,CLASS_,VERSION_,GMT_CREATE,GMT_MODIFIED FROM ASF_VARIABLE WHERE INSTANCE_ID_=?";

    public static final String ASF_CLEAR_VARIABLES = "DELETE FROM ASF_VARIABLE WHERE INSTANCE_ID_=? AND CLASS_=?";
}
