package org.evergreen.beans.factory.exception;

public class NoSuchBeanException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4095507629752033951L;

	public NoSuchBeanException(String exception){
		super(exception);
	}
}
