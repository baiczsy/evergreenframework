package org.evergreen.web.params.converter.handler;

import java.lang.annotation.Annotation;

import org.evergreen.web.annotation.Convert;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.utils.beanutils.ConvertUtils;
import org.evergreen.web.utils.beanutils.Converter;

public class ConverterConvertHandler extends AnnotationConvertHandler {

	@Override
	public Object execute(ParamInfo paramInfo) {
		if (!hasAnnotation(paramInfo))
			return null;		
		for (Annotation annotation : paramInfo.getAnnotations()) {
			if(annotation instanceof Convert){
				String value = getRequest().getParameter(paramInfo.getParamName());
				try {
					Converter converter = (Converter) ((Convert)annotation).value().newInstance();
					ConvertUtils.register(converter, paramInfo.getParamType());
					return ConvertUtils.convert(value,paramInfo.getParamType());
				} catch (InstantiationException e) {
					e.printStackTrace();
					throw new ConvertException(e.getMessage());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new ConvertException(e.getMessage());
				}
			}
		}
		return null;
	}

}
