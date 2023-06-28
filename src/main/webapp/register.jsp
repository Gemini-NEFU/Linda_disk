<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>云盘系统</title>
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="css/poposlides.css">
</head>

<body>
<div class="wrap">
    <div class="header">
        <div class="left">
            <img src="images/1.png">
            <h3>注册网盘账号</h3>
        </div>
        <div class="right">
            我已注册，现在就<a href="#">登录</a>
        </div>
    </div>
    <div class="register">
        <form action="UserServlet" method="post">
            <input type="hidden" name="flag" value="register" />
            <div class="left">
                <p><label>手机号:</label><input type="text" name="phone" value=""  placeholder="可用于登录和找回密码"></p>
                <p><label>用户名:</label><input type="text" name="userName" value=""  placeholder="请设置用户名"></p>
                <p><label>密码:</label><input type="password" name="password" placeholder="请设置登录密码"></p>
                <p><label>验证码:</label><input type="text" placeholder="请输入验证码"  class="verification">
                    <button class="verification">获取验证码</button>
                </p>
                <p>
                    <input type="checkbox">
                    阅读并接受<a href="#">《用户协议》</a>及<a href="#">《隐私权保护声明》</a>
                </p>
                <p><button>注册</button></p>
                <c:if test="${registerFlag==true }">
                    <div class="okmsg">
                            ${msg }  现在就  <a href="login.jsp">登录</a>
                    </div>
                </c:if>

                <c:if test="${registerFlag==false }">
                    <div class="errmsg">
                            ${msg }
                    </div>
                </c:if>
            </div>
            <div class="right">
                <h4>手机快速注册</h4>
                <div class="font1">请使用中国大陆手机号，编辑短信:<p> 8-14位字符（支持数字/字母/符号）</p></div>
                <div  class="font1">作为登录密码，发送至:<p>1069 0691 036590</p></div>
                <div>即可注册成功，手机号即为登录帐号。</div>
                <div>快速注册即视为您同意<a href="#">用户协议</a>和<a href="#">隐私权保护声明</a></div>
                <div class="wx">
                    <img src="images/erwei.jpg">
                    <p>请使用手机百度进行扫码</p>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>

