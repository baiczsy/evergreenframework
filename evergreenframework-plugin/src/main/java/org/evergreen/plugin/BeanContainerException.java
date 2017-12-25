package org.evergreen.plugin;

public class BeanContainerException extends RuntimeException {

	public BeanContainerException(String message) {
		super(message);
	}

	public BeanContainerException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanContainerException(Throwable cause) {
		super(cause);
	}
}
