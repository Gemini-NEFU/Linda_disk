package com.servlet;

import com.beans.*;
import com.dao.*;
import com.dao.impl.HdfsDaoImpl;
import com.google.common.base.Strings;
import com.util.StrUtil;
import org.apache.hadoop.fs.FileStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/HdfsServlet")
public class HdfsServlet extends HttpServlet {
    private HdfsDao hdfsDao=new HdfsDaoImpl();
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String flag=request.getParameter("flag");

        if("manager".equals(flag)){
            manager(request,response);
        } else if ("managerSubFiles".equals(flag)) {
            managerSubFiles(request, response);
        } else if ("upload".equals(flag)) {

        } else if ("download".equals(flag)) {

        } else if ("delete".equals(flag)) {

        }
    }
    private void manager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        DiskFileInfo[] hdfsFileList= hdfsDao.getRootFileList(user.getUserName());
        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/center.jsp").forward(request, response);
    }
    //点了文件夹以后,查出它的子文件列表
    private void managerSubFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parent=request.getParameter("parent");  // 格式: /admin/java/lesson3
        if(StrUtil.isNullOrEmpty(parent)) {
            UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
            parent=user.getUserName();
        }

        DiskFileInfo[] hdfsFileList= hdfsDao.getSubFileList(parent);
        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/center.jsp").forward(request, response);

    }

}
