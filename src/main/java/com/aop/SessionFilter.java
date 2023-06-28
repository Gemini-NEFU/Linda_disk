package com.aop;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionFilter implements Filter{
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req=(HttpServletRequest) request;;
        String uri=req.getRequestURI();
        String contextPath=req.getContextPath();  //linda-disk

        //以下这些路径不用过滤
        String [] excludePaths= {
                contextPath+"/images",
                contextPath+"/css",
                contextPath+"/js",
                contextPath+"/login.jsp",
                contextPath+"/register.jsp",
                contextPath+"/UserServlet"
        };

        //不需要拦的请求,直接放行
        for(String path:excludePaths) {
            if(uri.startsWith(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

        //处理直接访问 /linda-disk
        if(uri.equals(contextPath)) {
            chain.doFilter(request, response);
            return;
        }

        //如果没有session,证明没有登录过
        HttpSession session= req.getSession();
        if(session.getAttribute("session_user")==null) {
            String script="<script>alert(' illegal access! ');window.top.location.href='/login.jsp'</script>";
            response.getWriter().println(script);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }

}
