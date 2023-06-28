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
        } else if ("removeRepeat".equals(flag)) {
            removeRepeat(request,response);
        } else if ("searchFilesForSort".equals(flag)){
            searchFilesForSort(request,response);
        } else if ("logAnalyse".equals(flag)){
            logAnalyse(request,response);
        }
    }
    private void logAnalyse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-log-analyse.jsp").forward(request,response);
    }
    private void searchFilesForSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-sort.jsp").forward(request, response);
    }

    //点击 MR-去重复示例 后要把文件列表查出来,然后选中文件进行分析
    private void removeRepeat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-removeRepeat.jsp").forward(request, response);

    }

    //分类查看文件,转到文件分析列表页
    private void searchFilesForWordCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-wordCount.jsp").forward(request, response);
    }
}
