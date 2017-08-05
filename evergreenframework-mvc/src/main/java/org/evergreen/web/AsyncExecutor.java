package org.evergreen.web;


public interface AsyncExecutor {

	public void execute(RunnableSupport runnable);
}
