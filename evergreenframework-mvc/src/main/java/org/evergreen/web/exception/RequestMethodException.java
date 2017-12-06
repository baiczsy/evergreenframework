package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class RequestMethodException extends ActionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2820182501405625005L;

	public RequestMethodException(String message){
		super(message);
	}

	public RequestMethodException(Throwable cause){
		super(cause);
	}

	public RequestMethodException(String message, Throwable cause){
		super(message, cause);
	}

	public RequestMethodException(String message, int responseStatus) {
		super(message, responseStatus);
	}

}
