package org.evergreen.beans.factory.exception;

public class InjectPropertyException extends RuntimeException {

    public InjectPropertyException(String message){
        super(message);
    }

    public InjectPropertyException(String message, Throwable e){
        super(message, e);
    }
}
