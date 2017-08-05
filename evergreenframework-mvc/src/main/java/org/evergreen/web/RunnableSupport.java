package org.evergreen.web;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.view.Json;

public abstract class RunnableSupport implements Runnable {

	private AsyncContext asyncContext;

	void setAsyncContext(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
	}

	protected AsyncContext getAsyncContext() {
		return asyncContext;
	}

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) asyncContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) asyncContext.getResponse();
	}

	protected void setTimeout(long time) {
		asyncContext.setTimeout(time);
	}

	protected void complete() {
		asyncContext.complete();
	}

	protected void dispatcher() {
		asyncContext.dispatch();
	}

	protected void dispatcher(String arg0) {
		asyncContext.dispatch(arg0);
	}
	
	protected void writeToJson(String json){
		asyncContext.getResponse().setContentType(Json.CONTENT_TYPE);
		try {
			asyncContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void run();
}
