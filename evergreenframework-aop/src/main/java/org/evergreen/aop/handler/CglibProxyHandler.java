package org.evergreen.aop.handler;

import org.evergreen.aop.ProxyHandler;
import org.evergreen.aop.invocation.handler.CglibInvacationHandler;

import net.sf.cglib.proxy.Enhancer;

public class CglibProxyHandler implements ProxyHandler{

	//使用cglib动态代理
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> beanClass) {	
		  Enhancer enhancer = new Enhancer();
		  try{		  
			enhancer.setSuperclass(beanClass);
			enhancer.setCallback(new CglibInvacationHandler());
			return (T)enhancer.create();
		  }catch(Exception e){
			  e.printStackTrace();
			  return null;
		  }
	}
}
