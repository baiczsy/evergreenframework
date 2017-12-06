package org.evergreen.beans.handler;

public class InjectException extends RuntimeException {

    public InjectException(String message){
        super(message);
    }

    public InjectException(String message, Throwable e){
        super(message, e);
    }
}
