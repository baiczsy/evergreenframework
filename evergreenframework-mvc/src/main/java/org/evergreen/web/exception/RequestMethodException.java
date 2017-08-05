package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class RequestMethodException extends ActionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2820182501405625005L;

	
	public RequestMethodException(String requestMethod) {
		super("Request method '"+requestMethod+"' not supported", HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}

}
