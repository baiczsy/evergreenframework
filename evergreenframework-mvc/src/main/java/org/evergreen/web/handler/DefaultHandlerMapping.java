package org.evergreen.web.handler;

import org.evergreen.web.*;
import org.evergreen.web.exception.RequestMethodException;
import org.evergreen.web.utils.MatcherUtil;
import org.evergreen.web.utils.UrlPatternUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 默认的Action回调处理器
 */
public class DefaultHandlerMapping implements HandlerMapping {
    @Override
    public ActionMapper handler(HttpServletRequest request) {
        ActionMapper mapper = new ActionMapper();
        String urlPattern = UrlPatternUtil.getUrlPattern(request);
        List<ActionDefinition> definitions = getActionDefinitions(request);
        boolean isRequestMethod = false;
        boolean isRequestUri = false;
        for (ActionDefinition actionDefinition : definitions) {
            if(MatcherUtil.pathMatch(actionDefinition.getUrlPattern(),urlPattern)){
                isRequestUri = true;
                if(MatcherUtil.requestMethodMatch(actionDefinition.getRequestMethods(),
                        request.getMethod())){
                    isRequestMethod = true;
                    // 将请求url和匹配的url放入当前请求作用域,用于后面做restful参数映射
                    saveRestPath(request, actionDefinition.getUrlPattern(), urlPattern);
                    //将definition对象封装到ActionMapper中并返回
                    mapper.setDefinition(actionDefinition);
                }
            }
        }
        if(isRequestUri && !isRequestMethod){
            throw new RequestMethodException("Request method '"+request.getMethod()+"' not supported");
        }
        return mapper;
    }

    /**
     * 获取所有的Action描述定义
     */
    private List<ActionDefinition> getActionDefinitions(HttpServletRequest request){
        List<ActionDefinition> definitions = (List<ActionDefinition>) request
                .getServletContext().getAttribute(ActionDefinition.DEFINITION);
        return definitions;
    }

    /**
     * 将请求的url和Annotation上定义的url放入请求作用域, 在参数映射时解析restful参数
     */
    private void saveRestPath(HttpServletRequest request, String destPath, String origPath) {
        request.setAttribute(FrameworkServlet.DEST_PATH, destPath.split("/"));
        request.setAttribute(FrameworkServlet.ORIG_PATH, origPath.split("/"));
    }
}