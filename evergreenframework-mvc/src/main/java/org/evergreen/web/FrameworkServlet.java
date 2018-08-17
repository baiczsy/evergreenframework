package org.evergreen.web;

import org.evergreen.web.factory.WebAppHandlerFactory;
import org.evergreen.web.utils.ActionDefinitionUtil;
import org.evergreen.web.utils.ScanUtil;
import org.evergreen.web.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class FrameworkServlet extends HttpServlet {

    private static final long serialVersionUID = 8587584075900996812L;

    private final static Logger logger = LoggerFactory.getLogger(ActionServlet.class);

    /**
     * Servlet异步线程池
     */
    protected final static String THREAD_POOL = "org.evergreen.web.ServletBean.threadPool";

    /**
     * Tomcat, Jetty, JBoss, GlassFish 默认Servlet名称
     */
    private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

    /**
     * Google App Engine 默认Servlet名称
     */
    private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";

    /**
     * by Resin 默认Servlet名称
     */
    private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";

    /**
     * WebLogic 默认Servlet名称
     */
    private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";

    /**
     * WebSphere 默认Servlet名称
     */
    private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";

    String defaultServletName;

    /**
     * Servlet API
     */
    public final static String RESPONSE = "org.evergreen.web.FrameWorkServlet.response";

    public final static String REQUEST = "org.evergreen.web.FrameWorkServlet.request";

    public final static String DEST_PATH = "org.evergreen.web.RestfulSupport.dest.path";

    public final static String ORIG_PATH = "org.evergreen.web.RestfulSupport.orig.path";

    public final static String REQUEST_MAP = "org.evergreen.web.FrameWorkServlet.request.map";

    public final static String SESSION_MAP = "org.evergreen.web.FrameWorkServlet.session.map";

    public final static String APPLICATION_MAP = "org.evergreen.web.FrameWorkServlet.application.map";

    /**
     * Action实例工厂
     */
    public final static String HANDLER_FACTORY = "org.evergreen.web.handlerFactory";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 初始化不同web容器的默认servlet
        initDefaultServlet(config);
        //初始化ActionDefinitions
        initActionDefinitions(config.getServletContext());
        // 初始化action工厂
        initHandlerFactory(config.getServletContext());
        // 初始化线程池
        initAsyncThreadPool(config.getServletContext());
    }

    /**
     * 初始化不同WEB容器的默认Servlet名称
     *
     * @param config
     */
    private void initDefaultServlet(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        defaultServletName = servletContext
                .getInitParameter("defaultServletName");
        if (!StringUtils.hasText(defaultServletName)) {
            if (servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = COMMON_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = GAE_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = RESIN_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = WEBLOGIC_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null) {
                defaultServletName = WEBSPHERE_DEFAULT_SERVLET_NAME;
            } else {
                throw new IllegalStateException("Unable to locate the default servlet for serving static content. " +
                        "Please set the 'defaultServletName' property explicitly.");
            }
        }
    }

    /**
     * 初始化ActionDefinitions
     * @param servletContext
     */
    private void initActionDefinitions(ServletContext servletContext){
        List<ActionDefinition> definitionList = ActionDefinitionUtil.transformDefinitions(ScanUtil.scanPackage());
        // 将所有描述定义存入上下文
        servletContext.setAttribute(ActionDefinition.DEFINITION, definitionList);
        logger.info("Action description definition was initialized.");
    }

    /**
     * 初始化Action工厂，用于构建Action实例
     * 如果使用了IOC容器,那么action实例将从IOC容器中获取
     * 否则action实例将由框架中的WebApplicationFactory来构建
     */
    private void initHandlerFactory(ServletContext servletContext) {
        if (servletContext.getAttribute(FrameworkServlet.HANDLER_FACTORY) == null) {
            servletContext.setAttribute(FrameworkServlet.HANDLER_FACTORY, new WebAppHandlerFactory());
        }
    }

    /**
     * 初始化servlet异步线程池
     */
    private void initAsyncThreadPool(ServletContext servletContext) {
        ExecutorService threadPool = Executors.newFixedThreadPool(300);
        servletContext.setAttribute(THREAD_POOL, threadPool);
    }

    /**
     * 当ActionMapping中匹配不到对应的ActionDefinition, 则给容器默认的servlet处理
     *
     * @throws IOException
     * @throws ServletException
     */
    protected void forwardDefaultServlet(HttpServletRequest request,
                                         HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getServletContext().getNamedDispatcher(
                defaultServletName);
        if (rd == null) {
            throw new IllegalStateException(
                    "A RequestDispatcher could not be located for the default servlet '"
                            + this.defaultServletName + "'");
        }
        rd.forward(request, response);
    }

}
