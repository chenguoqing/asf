package com.baidu.asf.engine.command;

/**
 * Simple abstract adaptor
 */
public abstract class AbstractCommandInterceptor implements CommandInterceptor {

    protected CommandInterceptor next;

    @Override
    public void setNext(CommandInterceptor next) {
        this.next = next;
    }
}

