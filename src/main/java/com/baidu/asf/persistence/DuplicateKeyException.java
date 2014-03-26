package com.baidu.asf.persistence;

import com.baidu.asf.ASFException;
import com.baidu.asf.persistence.enitity.Entity;

/**
 * Exception thrown when an attempt to insert or update data results in violation of an primary key or unique
 * constraint.
 */
public class DuplicateKeyException extends ASFException {

    public final Entity entity;

    public DuplicateKeyException(Entity entity, Throwable cause) {
        super(cause);
        this.entity = entity;
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
        this.entity = null;
    }
}
