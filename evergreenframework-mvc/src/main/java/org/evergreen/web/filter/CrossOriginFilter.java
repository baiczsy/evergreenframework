package org.evergreen.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源跨域共享过滤器
 * Created by wangl on 2017/3/13.
 */
public class CrossOriginFilter implements Filter{

    private final static String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final static String ALLOW_METHODS = "Access-Control-Allow-Methods";
    private final static String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private final static String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private final static String MAX_AGE = "Access-Control-Max-Age";
    private String allowOrigin;
    private String allowMethods;
    private String allowHeaders;
    private String allowCredentials;
    private String maxAge;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowOrigin = filterConfig.getInitParameter(ALLOW_ORIGIN);
        allowMethods = filterConfig.getInitParameter(ALLOW_METHODS);
        allowHeaders = filterConfig.getInitParameter(ALLOW_HEADERS);
        allowCredentials = filterConfig.getInitParameter(ALLOW_CREDENTIALS);
        maxAge = filterConfig.getInitParameter(MAX_AGE);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(allowOrigin != null) {
            response.setHeader(ALLOW_ORIGIN, allowOrigin);
        }
        if(allowMethods != null) {
            response.setHeader(ALLOW_METHODS, allowMethods);
        }
        if(allowHeaders != null){
            response.setHeader(ALLOW_HEADERS, allowHeaders);
        }
        if(allowCredentials != null) {
            response.setHeader(ALLOW_CREDENTIALS, allowCredentials);
        }
        if(maxAge != null) {
            response.setHeader(MAX_AGE, maxAge);
        }
    }

    @Override
    public void destroy() {
    }
}
