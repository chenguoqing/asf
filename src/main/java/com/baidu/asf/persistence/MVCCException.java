package com.baidu.asf.persistence;

import com.baidu.asf.ASFException;
import com.baidu.asf.persistence.enitity.Entity;

/**
 * If the entity is failed as version changed, {@link MVCCException} will raised.
 */
public class MVCCException extends ASFException {
    /**
     * Entity
     */
    public final Entity entity;
    /**
     * MVCC version
     */
    public final int version;

    public MVCCException(Entity entity, int version) {
        this.entity = entity;
        this.version = version;
    }
}
