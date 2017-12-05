package org.evergreen.web.exception;

public class UploadFileException extends ActionException{

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(String message, Throwable cause){
        super(message, cause);
    }
}
