package org.evergreen.db.helper;

public class ExecuteParamsException extends SQLExecutorException {

    public ExecuteParamsException(String message) {
        super(message);
    }

    public ExecuteParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteParamsException(Throwable cause) {
        super(cause);
    }
}
