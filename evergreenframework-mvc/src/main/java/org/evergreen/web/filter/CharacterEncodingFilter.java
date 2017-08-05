package org.evergreen.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangl on 2017/3/13.
 */
public class CharacterEncodingFilter implements Filter{

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(encoding != null){
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
