package org.evergreen.web.utils;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.annotation.RequestMapping;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.params.ParamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActionDefinitionUtil {

    private final static Logger logger = LoggerFactory.getLogger(ActionDefinitionUtil.class);

    public static List<ActionDefinition> transformDefinitions(List<String> classesName){
        List<ActionDefinition> definitionList = new ArrayList<ActionDefinition>();
        // 遍历扫描的ClassName,创建class对象
        for (String className : classesName) {
            Class<?> actionClass = createControllerClass(className);
            // 获取控制器上定义的url
            String controllerUrl = getControllerUrl(actionClass);
            // 获取控制器能支持的请求方法
            String[] requestMethods = getControllerRequestMethods(actionClass);
            // 根据class中方法上定义的注解构建ActionDefinition实例
            for (Method method : actionClass.getMethods()) {
                // 构建ActionDefinition描述信息
                ActionDefinition definition = createDefinition(method,
                        controllerUrl, requestMethods);
                if (definition != null) {
                    definitionList.add(definition);
                }
            }
        }
        logger.info("Description definition initialized.");
        return definitionList;
    }

    /**
     * 构建控制器Class实例
     * @param className
     * @return
     */
    private static Class<?> createControllerClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ActionException("Not found controller class: " + className, e);
        }
    }

    /**
     * 获取控制器上定义的url
     *
     * @param actionClass
     * @return
     */
    private static String getControllerUrl(Class<?> actionClass) {
        return (actionClass.isAnnotationPresent(RequestMapping.class)) ? actionClass
                .getAnnotation(RequestMapping.class).value() : "";
    }

    /**
     * 获取控制器上标识能处理的请求方法
     *
     * @param actionClass
     * @return
     */
    private static String[] getControllerRequestMethods(Class<?> actionClass) {
        return (actionClass.isAnnotationPresent(RequestMapping.class)) ? actionClass
                .getAnnotation(RequestMapping.class).method() : new String[]{};
    }

    /**
     * 构建Action描述定义
     */
    private static ActionDefinition createDefinition(Method method,
                                              String controllerUrl, String[] requestMethods) {
        ActionDefinition definition = null;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method
                    .getAnnotation(RequestMapping.class);
            definition = new ActionDefinition();
            definition.setControllerUrl(controllerUrl);
            definition.setActionUrl(annotation.value());
            definition.setMethod(method);
            definition.setActionClass(method.getDeclaringClass());
            definition.setParamInfo(resolveParams(method));
            // 添加Action能支持的请求方法
            definition.getRequestMethods().addAll(CollectionUtils.arrayToList(requestMethods));
            definition.getRequestMethods().addAll(CollectionUtils.arrayToList(annotation.method()));
        }
        return definition;
    }

    /**
     * 获取回调方法中的所有参数名,参数类型
     *
     * @param method
     */
    private static List<ParamInfo> resolveParams(Method method) {
        // 获取所有的参数名
        String[] paramNames = ParamNameUtil.getParamName(method);
        // 获取所有的参数类型
        Class<?>[] paramTypes = method.getParameterTypes();
        // 获取所有参数的注解
        Annotation[][] annotations = method.getParameterAnnotations();
        // 构建参数信息集合
        return getParamInfos(paramNames, paramTypes, annotations);

    }

    /**
     * 构建参数信息集合
     */
    private static List<ParamInfo> getParamInfos(String[] paramNames,
                                          Class<?>[] paramTypes, Annotation[][] annotations) {
        List<ParamInfo> list = new ArrayList<ParamInfo>();
        for (int i = 0; i < paramNames.length; i++) {
            ParamInfo paramInfo = new ParamInfo();
            // 封装参数名
            paramInfo.setParamName(paramNames[i]);
            // 封装参数类型
            paramInfo.setParamType(paramTypes[i]);
            // 封装参数注解
            paramInfo.setAnnotations(annotations[i]);
            list.add(paramInfo);
        }
        return list;
    }
}
