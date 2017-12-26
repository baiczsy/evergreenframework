package org.evergreen.db.helper;

public class CloseResourcesException extends SQLExecutorException {

    public CloseResourcesException(String message) {
        super(message);
    }

    public CloseResourcesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloseResourcesException(Throwable cause) {
        super(cause);
    }
}
