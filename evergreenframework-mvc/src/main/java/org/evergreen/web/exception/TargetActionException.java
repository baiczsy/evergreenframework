package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

public class TargetActionException extends ActionException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3291810734338162508L;

	public TargetActionException(String message){
		super(message);
	}

	public TargetActionException(Throwable cause){
		super(cause);
	}

	public TargetActionException(String message, Throwable cause){
		super(message, cause);
	}

	public TargetActionException(String message, int responseStatus) {
		super(message, responseStatus);
	}

}
