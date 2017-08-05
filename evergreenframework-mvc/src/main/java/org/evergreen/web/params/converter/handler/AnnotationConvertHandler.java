package org.evergreen.web.params.converter.handler;

import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;

public abstract class AnnotationConvertHandler extends ParamsConvertHandler {

	protected boolean hasAnnotation(ParamInfo paramInfo) {
		return (paramInfo.getAnnotations().length != 0) ? true : false;
	}
}
