package com.baidu.asf.model;

import com.baidu.asf.ASFException;

/**
 * {@link ASFModelParserException} encapsulates the definition's parser errors
 */
public class ASFModelParserException extends ASFException {
    public ASFModelParserException(String message) {
        super(message);
    }

    public ASFModelParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASFModelParserException(Throwable cause) {
        super(cause);
    }
}
