package org.evergreen.web.params.converter.handler;

import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;

import org.evergreen.web.annotation.CookieValue;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;

public class CookieConvertHandler extends AnnotationConvertHandler {

	public Object execute(ParamInfo paramInfo) {
		if (!hasAnnotation(paramInfo))
			return null;
		for (Annotation annotation : paramInfo.getAnnotations()) {
			if(annotation instanceof CookieValue){
				CookieValue cookieAnno = (CookieValue)annotation;
				Cookie[] cookies = getRequest().getCookies();
				if(cookies != null){
					for (Cookie cookie : cookies) {
						if(cookie.getName().equals(cookieAnno.value())){
							Object value =  new ConvertUtilsBean().convert(cookie.getValue(),
									paramInfo.getParamType());
							if(value == null)
								throw new IllegalArgumentException("Cookie value could not cast "+paramInfo.getParamType());
							else
								return value;
						}
					}
					throw new RuntimeException("Can not found the cookie name "+cookieAnno.value());
				}else{
					throw new RuntimeException("Can not found the cookies.");
				}

			}
		}
		return null;
	}
}
