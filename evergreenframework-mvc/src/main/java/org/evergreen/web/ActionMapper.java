package org.evergreen.web;

public class ActionMapper {

	/**
	 * Action描述定义
	 */
	private ActionDefinition definition;

	/**
	 * 目标Action实例
	 */
	private Object target;

	/**
	 * 目标Action方法所需的参数
	 */
	private Object[] params;

	/**
	 * Action返回的试图结果集
	 */

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public ActionDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(ActionDefinition definition) {
		this.definition = definition;
	}

}
