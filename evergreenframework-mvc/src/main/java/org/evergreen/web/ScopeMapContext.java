package org.evergreen.web;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.evergreen.web.scope.ApplicationMapHandler;
import org.evergreen.web.scope.RequestMapHandler;
import org.evergreen.web.scope.ScopeHandler;
import org.evergreen.web.scope.SessionMapHandler;

/**
 * 为作用域对象封装的map进行代理
 * 
 * @author lenovo
 *
 */
public class ScopeMapContext {
	
	private ScopeHandler handler;
	private Map<String,Object> scopeMap;


	public ScopeMapContext(String scopeName){
		if(scopeName.equals(FrameworkServlet.REQUEST_MAP))
			handler = new RequestMapHandler(new HashMap<String, Object>());
		if(scopeName.equals(FrameworkServlet.SESSION_MAP))
			handler = new SessionMapHandler(new HashMap<String, Object>());
		if(scopeName.equals(FrameworkServlet.APPLICATION_MAP))
			handler = new ApplicationMapHandler(new HashMap<String, Object>());
	}

	public Map<String, Object> createScopeProxyMap() {
		scopeMap = (Map<String, Object>) Proxy.newProxyInstance(ScopeMapContext.class
				.getClassLoader(), new Class<?>[] { Map.class }, handler);
		return scopeMap;
	}

}
