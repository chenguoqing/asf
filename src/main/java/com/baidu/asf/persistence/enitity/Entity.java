package com.baidu.asf.persistence.enitity;

import java.sql.Timestamp;

/**
 * The base class for all entities
 */
public abstract class Entity {
    /**
     * Unique id
     */
    private long id;
    /**
     * MVCC version
     */
    private int version;
    /**
     * Old version,not associated with persistence
     */
    private int oldVersion;
    /**
     * Create date
     */
    private Timestamp created;
    /**
     * Last modified
     */
    private Timestamp modified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public int getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(int oldVersion) {
        this.oldVersion = oldVersion;
    }

    public void backupAndIncrementVersion() {
        this.oldVersion = version;
        this.version++;
    }
}
