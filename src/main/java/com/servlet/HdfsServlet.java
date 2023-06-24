package com.servlet;
import com.beans.*;
import com.dao.*;
import com.dao.impl.HdfsDaoImpl;
import com.util.StrUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/HdfsServlet")
public class HdfsServlet extends HttpServlet {
    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String flag = request.getParameter("flag");

        if ("manage".equals(flag)) {
            manage(request, response);
        } else if ("manageSubFiles".equals(flag)) {
            manageSubFiles(request, response);
        } else if ("upload".equals(flag)) {
            upload(request, response);
        } else if ("download".equals(flag)) {
            download(request, response);
        } else if ("delete".equals(flag)) {
            delete(request,response);
        } else if ("createFolder".equals(flag)) {
            createFolder(request,response);
        }
    }

    private void manage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user = (UserInfo) request.getSession().getAttribute("session_user");

        DiskFileInfo[] hdfsFileList = hdfsDao.getRootFileList(user.getUserName());
        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/center.jsp").forward(request, response);
        return;
    }

    //点了文件夹以后,查出它的子文件列表
    private void manageSubFiles(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String parent=request.getParameter("parent");   //admin/javatools/aaa/bbb/ccc
        if(StrUtil.IsNullOrEmpty(parent)){

            //如果用户是做文上传传后调有用的 manageSubFiles这个方法,这个 parent是不能通过 request.getParameter() 传过来的  所以这里添了这个
            parent=(String) request.getAttribute("parent");
            if(StrUtil.IsNullOrEmpty(parent)){
                UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
                parent=user.getUserName();
            }
        }


        DiskFileInfo[] hdfsFileList = hdfsDao.getSubFileList(parent);
        request.setAttribute("hdfsFileList", hdfsFileList);


        //以下处理的是前台上面的 导航 类似:  javatools  > aaa  > bbb  > ccc 的呈现
        List<String> urlList=new ArrayList<String>();
        urlList.add(parent);

        //处理完后,是  admin, admin/java, admin/java/lesson7 这样
        while(parent.lastIndexOf("/")!=-1){
            parent=	parent.substring(0,parent.lastIndexOf("/"));
            urlList.add(0,parent );
        }

        urlList.remove(0);  //用户家目录没有必要在前台显示,所以排除

        for (int i=0;i<urlList.size();i++) {
            String str=urlList.get(i);
            urlList.set(i,str+"_"+str.substring(str.lastIndexOf("/")+1));
        }

        request.setAttribute("urlList", urlList);
        request.getRequestDispatcher("center.jsp").forward(request, response);
    }

    private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        String downname = fileName.substring(fileName.lastIndexOf("/") + 1);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downname, "UTF-8"));
        hdfsDao.downLoadFileAsStream(fileName, response.getOutputStream());
    }

    private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 设置内存缓冲区，超过后写入临时文
        factory.setSizeThreshold(50 * 1024 * 1024); //50M

        //设置上传到服务器上文件的临时存放目录 -- 非常重要，防止存放到系统盘造成系统盘空间不足,实测发现这个目录好象必须建好,不然出错,所以使用了默认目录
//        factory.setRepository(new File("D:\\Projects\\IdeaProjects\\disk\\src\\main\\webapp\\upload_files"));  //默认值为 System.getProperty(“java.io.tmpdir”).

        //创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("utf-8");

        //设置上传文件最大为10G
        upload.setSizeMax(10 * 1024 * 1024 * 1024); //10G

        //解析获取新文件
        List<FileItem> fileItems;
        try {
            fileItems = upload.parseRequest(request);
            //得到上传的文件的父级路径
            String parent = fileItems.get(0).getString("UTF-8");

            //如果用户是在根路径上传
            UserInfo user = null;
            if (StrUtil.IsNullOrEmpty(parent)) {
                user = (UserInfo) request.getSession().getAttribute("session_user");
                parent = user.getUserName();
            }

            for (FileItem item : fileItems) {
                if (item.isFormField()) {
					/*如果是普通输入项 ,得到input中的name属性的值,和它对应的value
					String name=item.getFieldName();
					String value=item.getString("UTF-8");
					System.out.println("name="+name+"  value="+value); */
                } else {
                    //上传的是文件，获得文件上传字段中的文件名
                    //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名。
                    String fileName = item.getName(); // D://aaa/bbb/c.txt

                    //将FileItem对象中保存的主体内容保存到某个指定的文件中。
                    // 如果FileItem对象中的主体内容是保存在某个临时文件中，该方法顺利完成后，临时文件有可能会被清除
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    String localPath = getServletContext().getRealPath("/upload_files") + "/" + fileName;

                    item.write(new File(localPath));

                    hdfsDao.uploadFile(parent, localPath);

                    request.setAttribute("msg", "文件上传成功!");

                    //这句的作用是用来刷新磁盘使用百分比显示的
                    //		request.setAttribute("refreshScript", "window.parent.leftFrame.location.reload()");

                    //删除服务器(tomcat上的文件)
                    item.delete();

                    request.setAttribute("parent", parent);  //这句的目的是把paren传给下面的 manageSubFiles   要注意这里
                    request.setAttribute("refreshScript", "window.parent.leftFrame.location.reload()");
                    manageSubFiles(request, response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName=request.getParameter("fileName");
        String parent=fileName.substring(0,fileName.lastIndexOf("/")+1);

        if(hdfsDao.deleteFile(fileName)==true){
            request.setAttribute("msg", "删除成功");
            request.setAttribute("parent", parent);

            //这句的作用是用来刷新磁盘使用百分比显示的
            request.setAttribute("refreshScript", "window.parent.leftFrame.location.reload()");

            this.manageSubFiles(request,response);
        }
    }
    private void createFolder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parent=request.getParameter("parent");
        String folderName=request.getParameter("folderName");

        //如果用户是自已的根目录创建的
        if(StrUtil.IsNullOrEmpty(parent)) {
            UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
            parent=user.getUserName();
        }

        boolean result=hdfsDao.createFolder(parent,folderName);
        if(result==true) {
            request.setAttribute("msg", "文件夹 "+folderName+" 创建成功");
        }
        else {
            request.setAttribute("msg", "存在同名目录,创建失败 ");
        }

        request.setAttribute("parent", parent);
        manageSubFiles(request, response);

    }
}
