package com.servlet;
import com.beans.*;
import com.dao.*;
import com.dao.impl.HdfsDaoImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/HiveServlet")
public class HiveServlet extends HttpServlet {
    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String flag = request.getParameter("flag");

        if ("searchLogFiles".equals(flag)) {
            searchLogFiles(request,response);
        } else if ("manageSubFiles".equals(flag)) {

        } else if ("upload".equals(flag)) {

        } else if ("download".equals(flag)) {

        } else if ("delete".equals(flag)) {

        } else if ("createFolder".equals(flag)) {

        } else if ("search".equals(flag)){

        } else if ("searchFiles".equals(flag)) {

        }
    }
    //查询所有的日志,转到日志文件列表页
    private void searchLogFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo) request.getSession().getAttribute("session_user");

        //约定好,日志文件必须放在每个用户的 log目录下
        String parent=user.getUserName()+"/log";

        DiskFileInfo[] hdfsFileList = hdfsDao.getSubFileList(parent);
        request.setAttribute("hdfsFileList", hdfsFileList);

        request.getRequestDispatcher("/hive/list-log.jsp").forward(request, response);
    }

}
