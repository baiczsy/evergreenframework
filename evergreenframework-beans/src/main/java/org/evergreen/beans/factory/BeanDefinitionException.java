package org.evergreen.beans.factory;

public class BeanDefinitionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 479673800115089982L;

	public BeanDefinitionException(String errorMessage){
		super(errorMessage);
	}
}
