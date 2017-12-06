package org.evergreen.beans.factory;

public class CreateBeanException extends RuntimeException {

    public CreateBeanException(String message){
        super(message);
    }

    public CreateBeanException(String message, Throwable e){
        super(message, e);
    }
}
