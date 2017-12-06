package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class RequestMappingException extends ActionException{
	/**
	 *
	 */
	private static final long serialVersionUID = 9033302480030050501L;

	public RequestMappingException(String message){
		super(message);
	}

	public RequestMappingException(Throwable cause){
		super(cause);
	}

	public RequestMappingException(String message, Throwable cause){
		super(message, cause);
	}

	public RequestMappingException(String message, int responseStatus) {
		super(message,responseStatus);
	}

}
