package org.evergreen.beans.factory;

public class BeanCreationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1431898772701907978L;

	public BeanCreationException(String exception){
		super(exception);
	}

	public BeanCreationException(String exception, Throwable e){
		super(exception, e);
	}
}
