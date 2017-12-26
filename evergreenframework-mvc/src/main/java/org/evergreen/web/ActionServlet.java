package org.evergreen.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.exception.ActionException;
import org.evergreen.web.handler.DefaultHandlerInvoker;
import org.evergreen.web.handler.DefaultHandlerMapping;
import org.evergreen.web.view.DefaultViewResult;

/**
 * 核心控制器,接受所有请求,并将请求分发给不同的业务控制器
 */
public class ActionServlet extends FrameworkServlet {

    /**
     *
     */
    private static final long serialVersionUID = 789342728181721564L;

    /**
     * 请求映射处理器
     */
    private HandlerMapping handlerMapping;

    /**
     * Action回调处理器
     */
    private HandlerInvoker handlerInvoker;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 初始化映射处理器
        initHandlerMapping();
        // 初始化action回调处理器
        initHandlerInvoker();
    }

    /**
     * 初始化请求映射处理器
     */
    private void initHandlerMapping() {
        handlerMapping = new DefaultHandlerMapping();
    }

    /**
     * 初始化Action回调处理器
     */
    private void initHandlerInvoker() {
        handlerInvoker = new DefaultHandlerInvoker();
    }

    /**
     * 核心入口
     */
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws IOException, ServletException {
        // 初始化ActionContext对象
        initActionContext(request, response);
        // 请求映射,找到匹配的Action描述,返回ActionMapping对象
        ActionMapper mapper = handlerMapping.handler(request);
        // 如果mapping没有匹配的Action描述定义来处理请求,则当前请求交给默认servlet处理
        if (mapper.getDefinition() == null) {
            forwardDefaultServlet(request, response);
        } else {
            // 执行请求处理服务，并返回试图结果集
            Object viewObject = handlerInvoker.invoke(mapper);
            // 响应视图
            response(viewObject);
            // 清除ActionContext的本地线程副本
            destroyActionContext();
        }
    }

    /**
     * 当不同的请求到达时，为每一个请求初始化相应的ActionContext实例
     *
     * @param request
     * @param response
     */
    private void initActionContext(HttpServletRequest request,
                                   HttpServletResponse response) {
        Map<String, Object> contextMap = ActionContext.getContext()
                .getContextMap();
        // 将request对象放入contextMap中
        contextMap.put(REQUEST, request);
        // 将response对象放入contextMap中
        contextMap.put(RESPONSE, response);
        // 构建HttpServletRequest的map代理,放入contextMap中
        contextMap.put(REQUEST_MAP,
                new ScopeMapContext(REQUEST_MAP).createScopeProxyMap());
        // 构建HttpSession的map代理,放入contextMap中
        contextMap.put(SESSION_MAP,
                new ScopeMapContext(SESSION_MAP).createScopeProxyMap());
        // 构建ServletContext的map代理,放入contextMap中
        contextMap.put(APPLICATION_MAP,
                new ScopeMapContext(APPLICATION_MAP).createScopeProxyMap());
    }

    /**
     * 处理视图结果
     *
     * @param viewObject 视图结果对象
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private void response(Object viewObject) throws IOException, ServletException {
        if (viewObject != null) {
            ViewResult viewResult = (viewObject instanceof ViewResult) ? (ViewResult) viewObject
                    : new DefaultViewResult(viewObject);
            viewResult.execute();
        }
    }

    /**
     * 清除ActionContext的本地线程副本
     */
    private void destroyActionContext() {
        ActionContext.localContext.remove();
    }


}
