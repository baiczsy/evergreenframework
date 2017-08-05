package org.evergreen.beans.factory;

public class NoSuchBeanDefinitionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4095507629752033951L;

	public NoSuchBeanDefinitionException(String exception){
		super(exception);
	}
}
