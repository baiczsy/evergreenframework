package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class ParamMappingException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5988051886996114898L;

	public ParamMappingException(String paramType, String paramName) {
		super("Optional "+paramType+" parameter "+ paramName+ " is present but cannot be translated into a null value due to being declared as a primitive type. ", HttpStatus.SC_INTERNAL_SERVER_ERROR);

	}

}
