package org.evergreen.web.params.converter.handler;

import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;
import org.evergreen.web.utils.beanutils.BeanUtilsBean;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;
import org.evergreen.web.utils.beanutils.PropertyUtilsBeanFirm;

public class BeanConvertHandler extends ParamsConvertHandler {

	public Object execute(ParamInfo paramInfo) {
		try {
			//构建实例时如果发生异常,表示不是bean类型或者bean未提供默认构造函数s
			Object param = paramInfo.getParamType().newInstance();
			//处理默认的字符串对象，默认instance为空串
			param = param instanceof String ? null : param;
			BeanUtilsBean util = new BeanUtilsBean(new ConvertUtilsBean(),
					new PropertyUtilsBeanFirm());
			// 映射参数
			util.populate(param, getRequest().getParameterMap());
			return param;
		} catch (Throwable e) {
			// e.printStackTrace();
			return null;
		}
	}
}
