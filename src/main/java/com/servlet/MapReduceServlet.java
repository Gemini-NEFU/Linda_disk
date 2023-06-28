package com.servlet;

import com.beans.DiskFileInfo;
import com.beans.UserInfo;
import com.dao.HdfsDao;
import com.dao.impl.HdfsDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/MapReduceServlet")
public class MapReduceServlet extends HttpServlet {
    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String flag = request.getParameter("flag");

        if ("searchFilesForWordCount".equals(flag)) {
            searchFilesForWordCount(request,response);
        } else if ("".equals(flag)) {

        }
    }
    //分类查看文件,转到文件分析列表页
    private void searchFilesForWordCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-wordcount.jsp").forward(request, response);
    }
}
