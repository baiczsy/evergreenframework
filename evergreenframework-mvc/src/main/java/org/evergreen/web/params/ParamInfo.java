package org.evergreen.web.params;

import java.lang.annotation.Annotation;

public class ParamInfo {

	/**
	 * 参数名称
	 */
	private String paramName;

	/**
	 * 参数类型
	 */
	private Class<?> paramType;

	/**
	 * 参数定义的注解
	 * 
	 * @return
	 */
	private Annotation[] annotations;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}

}
