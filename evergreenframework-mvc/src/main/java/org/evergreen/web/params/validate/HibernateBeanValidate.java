package org.evergreen.web.params.validate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.evergreen.web.ActionContext;
import org.evergreen.web.ActionMapper;
import org.evergreen.web.FrameworkServlet;
import org.evergreen.web.annotation.Validate;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.ParamsValidate;
import org.evergreen.web.utils.ResourceUtil;

public class HibernateBeanValidate implements ParamsValidate {
	public Map<String, String> validate(ActionMapper mapping) {
		Map<String, String> messages = new HashMap<String, String>();
		// 获取方法参数上的所有注解
		Annotation[][] paramAmmotations = mapping.getDefinition().getMethod()
				.getParameterAnnotations();
		for (int i = 0; i < paramAmmotations.length; i++) {
			for (int j = 0; j < paramAmmotations[i].length; j++) {
				if (paramAmmotations[i][j].annotationType().equals(Validate.class)) {
					Set<ConstraintViolation<Object>> info = validate(
							mapping.getParams()[i], paramAmmotations[i][j]);
					// 封装到map中
					addMessage(mapping.getParams()[i], messages, info);
				}
			}
		}
		return messages;
	}

	// 获取验证器
	private Validator getValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}

	// 调用BeanValidate执行验证
	private Set<ConstraintViolation<Object>> validate(Object param,
			Annotation annotation) {
		Validate validAnnotation = (Validate) annotation;
		// 获取验证器
		Validator validator = getValidator();
		// 执行校验
		Set<ConstraintViolation<Object>> info = validator.validate(param,
				validAnnotation.groups());
		return info;
	}

	// 封装验证消息到map中
	private void addMessage(Object param, Map<String, String> messages,
			Set<ConstraintViolation<Object>> info) {
		for (ConstraintViolation<Object> constraintViolation : info) {
			String validField = constraintViolation.getPropertyPath()
					.toString();
			messages.put(validField, getMessage(param, constraintViolation));
		}
	}

	// 获取消息提示信息
	private String getMessage(Object param,
			ConstraintViolation<Object> constraintViolation) {
		String message = constraintViolation.getMessage();
		if (message.startsWith("{") && message.endsWith("}")) {
			// 获取国际化资源的baseName
			String key = message.substring(1, message.length() - 1);
			return getResourceMessage(key);
		}
		return message;
	}

	// 获取资源文件信息
	private String getResourceMessage(String key) {
		// 获取国际化资源的baseName
		String baseName = ((HttpServletRequest) ActionContext.getContext().get(
				FrameworkServlet.REQUEST)).getServletContext()
				.getInitParameter("i18n");
		// String baseName = param.getClass().getPackage().getName() +
		// ".Validate";
		Locale locale = ActionContext.getContext().getLocale();
		Map<String, String> resourceMap = ResourceUtil.getResource(baseName,
				locale);
		return resourceMap.get(key);
	}
}
