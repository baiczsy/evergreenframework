package org.evergreen.web.utils;

import java.lang.reflect.Method;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ParamNameUtil {

	// 获取方法的参数名
	// 使用javaassist的反射方法获取方法的参数名
	public static String[] getParamName(Method method) {
		try {
			ClassPool pool = ClassPool.getDefault();
			// 将声明method的class加入对象池中受管
			pool.insertClassPath(new ClassClassPath(method.getDeclaringClass()));
			CtClass cc = pool.get(method.getDeclaringClass().getName());
			// 将所有参数类型转换成CtClass
			CtClass[] paramsClass = getParamsCtClass(pool, method);
			CtMethod cm = cc.getDeclaredMethod(method.getName(), paramsClass);
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			/*for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(i + pos);*/
			for (int i = 0; i < attr.tableLength(); i++) {
				if(attr.index(i) >= pos && attr.index(i) < paramNames.length + pos)
					paramNames[attr.index(i) - pos] = attr.variableName(i);
			}
			return paramNames;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 将方法所有参数类型转换为CtClass类型
	private static CtClass[] getParamsCtClass(ClassPool pool, Method method)
			throws NotFoundException {
		// 获取此方法的所有参数类型
		Class<?>[] paramsType = method.getParameterTypes();
		// 获取将所有参数类型的完整类名
		String[] paramsClassName = new String[paramsType.length];
		for (int i = 0; i < paramsType.length; i++) {
			paramsClassName[i] = paramsType[i].getName();
		}
		// 将所有参数类型转换成CtClass
		return pool.get(paramsClassName);
	}
}
