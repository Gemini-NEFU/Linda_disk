package com.servlet;

import com.beans.LogInfo;
import com.beans.UserInfo;
import com.dao.HiveDao;
import com.dao.impl.HiveDaoImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/Hive_LogTableServlet")
public class Hive_LogTableServlet extends HttpServlet {
    private HiveDao hiveDao=new HiveDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        List<LogInfo> logList= hiveDao.getLogList(user.getUserName());
        request.setAttribute("logList", logList);
        request.getRequestDispatcher("/hive/log-table-result.jsp").forward(request, response);
    }
}
