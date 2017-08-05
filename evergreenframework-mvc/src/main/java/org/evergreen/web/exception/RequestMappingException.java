package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class RequestMappingException extends ActionException{

	private final static String NOT_FOUND = "No mapping found for HTTP request with URI";

	/**
	 *
	 */
	private static final long serialVersionUID = 9033302480030050501L;

	public RequestMappingException() {
		super(NOT_FOUND, HttpStatus.SC_NOT_FOUND);
	}

}
