package com.baidu.fsm.storage.db;

/**
 * SQL collection for database store
 */
public class Statements {

    public static final String FSM_TABLE = "fsm";

    /**
     * Create state machine instance
     */
    public static final String FSM_CREATE_FSM = String.format("INSERT INTO %s (PARENT_ID,STATE,VERSION,DEF_VERSION,STATUS,CREATED,MODIFIED) VALUES(?,?,?,?,?,?,?)", FSM_TABLE);

    /**
     * Find state machine instance info by id
     */
    public static final String FSM_LOAD_FSM_BY_ID = String.format("SELECT ID,PARENT_ID,STATE,TRANSITIONS,DEF_VERSION,VERSION,STATUS,CREATED,MODIFIED FROM %s WHERE ID=? AND STATUS=0", FSM_TABLE);

    /**
     * Update transition table with MVCC
     */
    public static final String FSM_UPDATE_STATE_TRANSMIT = String.format("UPDATE %s SET STATE=?,TRANSITIONS=?,VERSION=?,MODIFIED=? WHERE ID=? AND VERSION=?", FSM_TABLE);

    /**
     * Delete state machine
     */
    public static final String FSM_DELETE_STATE_MACHINE = String.format("UPDATE %s SET STATUS =1 WHERE ID=? AND VERSION=?", FSM_TABLE);

    /**
     * Create table schema
     */
    public static final String FSM_CREATE_FSM_SCHEMA = "CREATE TABLE IF NOT EXISTS FSM (ID INT PRIMARY KEY AUTO_INCREMENT," +
            "PARENT_ID INT,STATE VARCHAR(50),TRANSITIONS BLOB,DEF_VERSION INT NOT NULL,VERSION INT NOT NULL," +
            "STATUS INT NOT NULL,CREATED TIMESTAMP NOT NULL,MODIFIED TIMESTAMP NOT NULL) CHARACTER SET UTF8 COLLATE UTF8_bin,engine=INNODB";
}
