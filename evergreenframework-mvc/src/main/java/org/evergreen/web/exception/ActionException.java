package org.evergreen.web.exception;

public class ActionException extends RuntimeException {


	private static final long serialVersionUID = 6077688573906359576L;

	private int responseStatus;

	public ActionException(String message){
		super(message);
	}

	public ActionException(Throwable cause){
		super(cause);
	}

	public ActionException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ActionException(String message, int responseStatus) {
		super(message);
		this.responseStatus = responseStatus;
	}

	public int getResponseStatus(){
		return responseStatus;
	}

}
