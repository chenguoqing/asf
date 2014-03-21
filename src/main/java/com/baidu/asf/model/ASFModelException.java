package com.baidu.asf.model;

import com.baidu.asf.ASFException;

/**
 * Created by chenguoqing01 on 14-3-20.
 */
public class ASFModelException extends ASFException {
    public ASFModelException() {
    }

    public ASFModelException(String message) {
        super(message);
    }

    public ASFModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASFModelException(Throwable cause) {
        super(cause);
    }
}
