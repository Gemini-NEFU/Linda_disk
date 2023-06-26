package com.aop;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.beans.UserInfo;
import com.util.LogUtil;
public class LogFilter implements Filter{

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req=(HttpServletRequest)request;
        HttpSession session=req.getSession();
        UserInfo user=(UserInfo)session.getAttribute("session_user");

        String uri=req.getRequestURI();

        //以下这些路径不用过滤
        String contextPath=req.getContextPath();
        String [] excludePaths= {
                contextPath+"/images",
                contextPath+"/css",
                contextPath+"/js"
        };

        //不需要拦的请求,不写日志直接放行
        for(String path:excludePaths) {
            if(uri.startsWith(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

        //String ip=req.getRemoteAddr();
        String [] ipList={
                "192.168.100.100",
                "222.168.100.100",
                "192.145.100.100",
                "192.168.100.190",
                "172.168.120.100",
                "172.169.30.20",
                "192.201.100.70",
                "162.168.200.81",
                "192.168.100.66",
                "192.168.100.100",
                "212.168.100.66",
                "192.168.100.100",
                "122.168.100.55",
                "192.108.100.100",
        };

        String ip=ipList[new Random().nextInt(ipList.length)];
        long time=System.currentTimeMillis();
        String day= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String flag=request.getParameter("flag");
        String userName=user==null?null:user.getUserName();
        String logStr=day+","+ip+","+uri+","+flag+","+time+","+userName;

        chain.doFilter(request, response);
        LogUtil.log(logStr);
    }

    public void destroy() {
    }
}