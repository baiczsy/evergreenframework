package org.evergreen.web;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.annotation.RequestMapping;
import org.evergreen.web.factory.WebApplicationFactory;
import org.evergreen.web.handler.DefaultHandlerInvoker;
import org.evergreen.web.handler.DefaultHandlerMapping;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.utils.CollectionUtils;
import org.evergreen.web.utils.ParamNameUtil;
import org.evergreen.web.utils.ScanUtil;
import org.evergreen.web.utils.StringUtils;

public abstract class FrameworkServlet extends HttpServlet {

	private static final long serialVersionUID = 8587584075900996812L;

	/**
	 * Servlet异步线程池
	 */
	protected final static String THREAD_POOL = "org.evergreen.web.ServletBean.threadPool";

	/**
	 * 扫描参数,固定值为scan
	 */
	final static String SCAN_PATH = "scan";

	/**
	 * 用于存放所有Action的描述信息
	 */
	final List<ActionDefinition> definitionList = new ArrayList<ActionDefinition>();

	/** Tomcat, Jetty, JBoss, GlassFish 默认Servlet名称*/
	private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

	/** Google App Engine 默认Servlet名称 */
	private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";

	/** by Resin 默认Servlet名称*/
	private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";

	/** WebLogic 默认Servlet名称*/
	private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";

	/** WebSphere 默认Servlet名称*/
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
	public final static String ACTION_FACTORY = "org.evergreen.web.actionFactory";

	/**
	 * 请求映射处理器
	 */
	protected HandlerMapping handlerMapping;

	/**
	 * Action回调处理器
	 */
	protected HandlerInvoker handlerInvoker;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 初始化不同web容器的默认servlet
		initDefaultServlet(config);
		// 初始化ActionDefinitions
		initActionDefinitions(config);
		// 初始化action工厂
		initActionFactory(config.getServletContext());
		// 初始化映射处理器
		initHandlerMapping();
		// 初始化action回调处理器
		initHandlerInvoker();
		// 初始化线程池
		initAsyncThreadPool(config.getServletContext());
	}

	/**
	 * 初始化不同WEB容器的默认Servlet
	 *
	 * @param config
	 */
	private void initDefaultServlet(ServletConfig config) {
		ServletContext servletContext = config.getServletContext();
		defaultServletName = servletContext
				.getInitParameter("defualtServletName");
		if(!StringUtils.hasText(defaultServletName)){
			if (servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
				defaultServletName = COMMON_DEFAULT_SERVLET_NAME;
			}
			else if (servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null) {
				defaultServletName = GAE_DEFAULT_SERVLET_NAME;
			}
			else if (servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME) != null) {
				defaultServletName = RESIN_DEFAULT_SERVLET_NAME;
			}
			else if (servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null) {
				defaultServletName = WEBLOGIC_DEFAULT_SERVLET_NAME;
			}
			else if (servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null) {
				defaultServletName = WEBSPHERE_DEFAULT_SERVLET_NAME;
			} else {
				throw new IllegalStateException("Unable to locate the default servlet for serving static content. " +
						"Please set the 'defaultServletName' property explicitly.");
			}
		}
	}

	/**
	 * 初始化请求映射处理器
	 */
	private void initHandlerMapping(){
		handlerMapping = new DefaultHandlerMapping();
	}

	/**
	 * 初始化Action回调处理器
	 */
	private void initHandlerInvoker(){
		handlerInvoker = new DefaultHandlerInvoker();
	}

	/**
	 * 初始化ActionDefinitions(所有Action的描述定义)
	 *
	 * @param config
	 */
	private void initActionDefinitions(ServletConfig config) {
		// 初始化所有类名
		String scanPath = config.getInitParameter(SCAN_PATH);
		// 根据路径解析出所有Action的完整类名
		List<String> classesName = ScanUtil.scan((scanPath == null) ? "" : scanPath);
		// 初始化所有Action的描述定义
		initDefinitionList(classesName);
		// 将所有描述存入上下文
		config.getServletContext().setAttribute(ActionDefinition.DEFINITION,
				definitionList);
	}

	/**
	 * 初始化Action工厂，用于构建Action实例
	 * 如果配置了容器插件工厂,则初始化插件,那么action实例将从容器工厂中获取
	 * 否则action实例将由框架中的WebApplicationFactory来构建实例
	 */
	private void initActionFactory(ServletContext servletContext) {
		String actionFactoryName = servletContext.getInitParameter("actionFactory");
		// 目标Action回调处理器
		try {
			ActionFactory actionFactory = (actionFactoryName != null) ? (ActionFactory) Class
					.forName(actionFactoryName).newInstance() : new WebApplicationFactory();
			// 将回调处理器放入上下文
			servletContext.setAttribute(FrameworkServlet.ACTION_FACTORY,
					actionFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将扫描的ClassName解析后存放在Action描述信息的集合中
	 *
	 * @param classesName
	 * @return
	 */
	private void initDefinitionList(List<String> classesName) {
		// 遍历扫描的ClassName,创建class对象
		for (String className : classesName) {
			try {
				Class<?> actionClass = Class.forName(className);
				// 获取控制器上定义的url
				String controllerUrl = getControllerUrl(actionClass);
				// 获取控制器能支持的请求方法
				String[] requestMethods = getControllerRequestMethods(actionClass);
				// 根据class中方法上定义的注解构建ActionDefinition实例
				for (Method method : actionClass.getMethods()) {
					// 构建ActionDefinition描述信息
					ActionDefinition definition = createDefinition(method,
							controllerUrl, requestMethods);
					if (definition != null)
						definitionList.add(definition);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取控制器上定义的url
	 *
	 * @param actionClass
	 * @return
	 */
	private String getControllerUrl(Class<?> actionClass) {
		return (actionClass.isAnnotationPresent(RequestMapping.class)) ? actionClass
				.getAnnotation(RequestMapping.class).value() : "";
	}

	/**
	 * 获取控制器上标识能处理的请求方法
	 *
	 * @param actionClass
	 * @return
	 */
	private String[] getControllerRequestMethods(Class<?> actionClass) {
		return (actionClass.isAnnotationPresent(RequestMapping.class)) ? actionClass
				.getAnnotation(RequestMapping.class).method() : new String[] {};
	}

	/**
	 * 构建Action描述定义
	 */
	private ActionDefinition createDefinition(Method method,
											  String controllerUrl, String[] requestMethods) {
		if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping annotation = method
					.getAnnotation(RequestMapping.class);
			ActionDefinition definition = new ActionDefinition();
			definition.setControllerUrl(controllerUrl);
			definition.setActionUrl(annotation.value());
			definition.setMethod(method);
			definition.setActionClass(method.getDeclaringClass());
			definition.setParamInfo(resolveParams(method));
			// 添加Action能支持的请求方法
			definition.getRequestMethods().addAll(CollectionUtils.arrayToList(requestMethods));
			definition.getRequestMethods().addAll(CollectionUtils.arrayToList(annotation.method()));
			return definition;
		}
		return null;
	}

	/**
	 * 获取回调方法中的所有参数名,参数类型
	 *
	 * @param method
	 */
	private List<ParamInfo> resolveParams(Method method) {
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
	private List<ParamInfo> getParamInfos(String[] paramNames,
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
