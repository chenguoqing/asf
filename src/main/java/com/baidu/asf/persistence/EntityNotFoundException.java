package com.baidu.asf.persistence;

import com.baidu.asf.ASFException;

/**
 * Not found the entity from persistence
 */
public class EntityNotFoundException extends ASFException {

    public final long id;

    public EntityNotFoundException(long id) {
        this.id = id;
    }

    public EntityNotFoundException(long id, String message) {
        super(message);
        this.id = id;
    }
}
