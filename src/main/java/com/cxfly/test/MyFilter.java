package com.cxfly.test;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class MyFilter
 */
public class MyFilter implements Filter {

    /**
     * Default constructor.
     */
    public MyFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @SuppressWarnings({ "unused", "unchecked" })
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("RequestURI: " + req.getRequestURI());
        System.out.println("QueryString: " + req.getQueryString());

        System.out.println("Http HeaderList:");
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            System.out.println(headerKey + ": " + req.getHeader(headerKey));
        }
        System.out.println("---------->>>" + req.getHeader("_aop_secret"));
        System.out.println("Http parameterList :");
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName + ": " + req.getHeader(paramName));
        }
        chain.doFilter(request, response);

        //        res.setContentType("text/html;charset=UTF-8");
        //        PrintWriter out = res.getWriter();
        //        out.print("OK");
        //        out.close();

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
