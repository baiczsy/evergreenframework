package org.evergreen.web.handler;

import org.evergreen.web.*;
import org.evergreen.web.exception.TargetActionException;
import org.evergreen.web.params.validate.HibernateBeanValidate;
import org.evergreen.web.params.converter.ParamConvertUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DefaultHandlerInvoker implements HandlerInvoker{

    @Override
    public ViewResult invoke(ActionMapper mapper) throws Throwable {
        getAction(mapper);
        paramsConvert(mapper);
        validateParams(mapper);
        return invokeMethod(mapper);
    }

    /**
     * 创建Action实例
     * @param mapper
     */
    private void getAction(ActionMapper mapper) throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        ActionFactory actionFactory = (ActionFactory) request
                .getServletContext().getAttribute(FrameworkServlet.ACTION_FACTORY);
        Object targetAction = actionFactory
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
    private void paramsConvert(ActionMapper mapper) throws Exception {
        Object[] params = ParamConvertUtil.convert(mapper);
        mapper.setParams(params);
    }

    /**
     * 参数校验
     * @param mapper
     * @throws Exception
     */
    private void validateParams(ActionMapper mapper) throws Exception {
        ParamsValidate validateUtil = new HibernateBeanValidate();
        Map<String, String> errors = validateUtil.validate(mapper);
        if (!errors.isEmpty())
            ActionContext.getContext().getRequest().put(ActionContext.ERRORS, errors);
    }

    /**
     * 回调Action方法
     * @param mapper
     */
    private ViewResult invokeMethod(ActionMapper mapper) throws Throwable{
        Object viewResult = null;
        try {
            viewResult = mapper.getDefinition().getMethod()
                    .invoke(mapper.getTarget(), mapper.getParams());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if(e.getCause() != null){
                throw e.getCause();
            }
            e.printStackTrace();
        }
        return (ViewResult) viewResult;
    }

}
