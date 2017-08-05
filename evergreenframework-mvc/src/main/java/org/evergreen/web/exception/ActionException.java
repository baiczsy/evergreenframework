package org.evergreen.web.exception;

public class ActionException extends RuntimeException {


	private static final long serialVersionUID = 6077688573906359576L;

	private int responseStatus;
	
	public ActionException(String exception, int responseStatus) {
		super(exception);
		this.responseStatus = responseStatus;
	}

	public int getResponseStatus(){
		return responseStatus;
	}

}
