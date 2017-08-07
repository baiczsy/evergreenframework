package org.evergreen.web.handler;

import org.evergreen.web.*;
import org.evergreen.web.exception.RequestMethodException;
import org.evergreen.web.utils.MatcherUtil;
import org.evergreen.web.utils.UrlPatternUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 默认的Action回到处理器
 */
public class DefaultHandlerMapping implements HandlerMapping {
    @Override
    public ActionMapper handler() throws Exception {
        ActionMapper mapper = new ActionMapper();
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        String urlPattern = UrlPatternUtil.getUrlPattern(request);
        List<ActionDefinition> definitions = getActionDefinitions();
        boolean isRequestMethod = false;
        boolean isRequestUri = false;
        for (ActionDefinition actionDefinition : definitions) {
            if(MatcherUtil.pathMatch(actionDefinition.getUrlPattern(),urlPattern)){
                isRequestUri = true;
                if(MatcherUtil.requestMethodMatch(actionDefinition.getRequestMethods(),
                        request.getMethod())){
                    isRequestMethod = true;
                    // 将请求url和匹配的url放入当前请求作用域,用于后面做restful参数映射
                    saveRestPath(actionDefinition.getUrlPattern(), urlPattern);
                    //将definition对象封装到ActionMapping中并返回
                    mapper.setDefinition(actionDefinition);
                }
            }
        }
        if(isRequestUri && !isRequestMethod){
            throw new RequestMethodException(request.getMethod());
        }
        return mapper;
    }


    /**
     * 获取所有的Action描述定义
     */
    private List<ActionDefinition> getActionDefinitions(){
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        List<ActionDefinition> definitions = (List<ActionDefinition>) request
                .getServletContext().getAttribute(ActionDefinition.DEFINITION);
        return definitions;
    }

    /**
     * 将请求的url和Annotation上定义的url放入请求作用域, 在参数映射时解析restful参数
     */
    private void saveRestPath(String destPath, String origPath) {
        ActionContext.getContext().getRequest().put(FrameworkServlet.DEST_PATH, destPath.split("/"));
        ActionContext.getContext().getRequest().put(FrameworkServlet.ORIG_PATH, origPath.split("/"));
    }
}