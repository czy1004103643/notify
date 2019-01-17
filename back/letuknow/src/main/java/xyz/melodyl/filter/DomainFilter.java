package xyz.melodyl.filter;

import xyz.melodyl.controller.UserController;
import xyz.melodyl.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DomainFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Accept,Content-Type,Token");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", UserService.TOKEN_ATTRIBUTE_NAME);

        if (httpServletRequest.getMethod().equals("OPTIONS"))
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        else {
            chain.doFilter(request, httpServletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
