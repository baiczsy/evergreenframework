package org.evergreen.plugin;

@SuppressWarnings("serial")
public class BeanContainerException extends Exception {

	public BeanContainerException() {
		super("Not a container managed bean.");
	}
}
