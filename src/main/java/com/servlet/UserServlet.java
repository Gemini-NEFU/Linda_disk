package com.servlet;

import com.beans.UserInfo;
import com.dao.*;
import com.dao.impl.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private UserDao userDao=new UserDaoImpl();
    private HdfsDao hdfsDao=new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String flag=request.getParameter("flag");

        if("login".equals(flag)){
            login(request,response);
        }
        else if("register".equals(flag)){
            this.register(request,response);
        } else if ("logout".equals(flag)) {
            logout(request,response);
        }
    }
    private void login(HttpServletRequest request, HttpServletResponse response){

    }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName=request.getParameter("userName");

        UserInfo oldUser=userDao.getUserByName(userName);
        if(oldUser!=null){
            request.setAttribute("msg", "对不起,该用户名已被其他用户注册!");
            request.setAttribute("registerFlag", false);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return ;
        }
        String password=request.getParameter("password");
        String phone=request.getParameter("phone");

        UserInfo user=new UserInfo();
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone(phone);

        if(userDao.addUser(user)==1){
            //添加用户后,还要在hdfs创建这个用户的文件夹,以用户名做为文件夹名称
            boolean result=hdfsDao.createUserRoot(userName);
//            boolean result=true;
            if(result==true){
                request.setAttribute("registerFlag", true);
                request.setAttribute("msg", "用户注册成功!");
            }
            else{
                userDao.delUser(userName);
                request.setAttribute("registerFlag", false);
                request.setAttribute("msg", "用户注册失败!");
            }

            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
    private void logout(HttpServletRequest request, HttpServletResponse response){

    }
}

