package org.evergreen.beans.factory.exception;

public class CreateBeanException extends RuntimeException {

    public CreateBeanException(String message){
        super(message);
    }

    public CreateBeanException(String message, Throwable e){
        super(message, e);
    }
}
