package com.baidu.asf.engine;

import com.baidu.asf.ASFException;

/**
 * Process instance has been modified concurrently
 */
public class ASFConcurrentModificationException extends ASFException {

    public final ASFInstance instance;

    public ASFConcurrentModificationException(ASFInstance instance) {
        this.instance = instance;
    }

    public ASFConcurrentModificationException(ASFInstance instance, String message) {
        super(message);
        this.instance = instance;
    }
}
