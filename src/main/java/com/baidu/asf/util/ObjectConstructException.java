package com.baidu.asf.util;

import com.baidu.asf.ASFException;

/**
 * Exception thrown when could not deserialize object instance from byte stream
 */
public class ObjectConstructException extends ASFException {
    public ObjectConstructException() {
    }

    public ObjectConstructException(String message) {
        super(message);
    }

    public ObjectConstructException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectConstructException(Throwable cause) {
        super(cause);
    }
}
