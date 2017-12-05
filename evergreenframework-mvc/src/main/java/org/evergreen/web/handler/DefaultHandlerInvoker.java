package org.evergreen.web.handler;

import org.evergreen.web.*;
import org.evergreen.web.exception.TargetActionException;
import org.evergreen.web.params.validate.HibernateBeanValidate;
import org.evergreen.web.params.converter.ParamConvertUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DefaultHandlerInvoker implements HandlerInvoker{

    @Override
    public Object invoke(ActionMapper mapper) throws IOException, ServletException {
        getAction(mapper);
        paramsConvert(mapper);
        validateParams(mapper);
        return invokeMethod(mapper);
    }

    /**
     * 创建Action实例
     * @param mapper
     */
    private void getAction(ActionMapper mapper) throws IOException{
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        ActionFactory actionFactory = (ActionFactory) request
                .getServletContext().getAttribute(FrameworkServlet.ACTION_FACTORY);
        Object targetAction = null;
            targetAction = actionFactory
                    .crateAction(mapper.getDefinition());
        if (targetAction == null) {
            throw new TargetActionException();
        }
        mapper.setTarget(targetAction);
    }

    /**
     * 参数映射（类型转换）
     * @param mapper
     */
    private void paramsConvert(ActionMapper mapper)  {
        Object[] params = new Object[0];
        try {
            params = ParamConvertUtil.convert(mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapper.setParams(params);
    }

    /**
     * 参数校验
     * @param mapper
     * @throws Exception
     */
    private void validateParams(ActionMapper mapper) {
        ParamsValidate validateUtil = new HibernateBeanValidate();
        Map<String, String> errors = validateUtil.validate(mapper);
        if (!errors.isEmpty())
            ActionContext.getContext().getRequest().put(ActionContext.ERRORS, errors);
    }

    /**
     * 回调Action方法
     * @param mapper
     */
    private Object invokeMethod(ActionMapper mapper) throws ServletException {
        Object viewObject = null;
        try {
            viewObject = mapper.getDefinition().getMethod()
                    .invoke(mapper.getTarget(), mapper.getParams());
        } catch (IllegalAccessException e) {
            throw new ServletException("Invoke target method fail.", e);
        } catch (InvocationTargetException e) {
            throw new ServletException("Invoke target method fail.", e);
        }
        return viewObject;
    }

}
