<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>海康云盘系统</title>

    <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="css/poposlides.css">
    <script src="js/jquery-2.1.4.min.js"></script>
    <script src="js/search.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/poposlides.js"></script>
    <script>
        window.onload=function(){
            $(".slides").poposlides();
        };
    </script>

</head>

<body>
<div class="main_agileits" id="page">
    <div class="agile_wthree_nav">
        <nav class="navbar navbar-default">
            <div class="navbar-header navbar-left">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">切换导航</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <h1><a class="navbar-brand" href="index.html"><img src="images/1.png"></a></h1>
            </div>
            <div class="collapse navbar-collapse navbar-right" id="bs-example-navbar-collapse-1">
                <nav class="link-effect-8" id="link-effect-8">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="index.html">首页</a></li>
                        <li><a href="#">客户端下载</a></li>
                        <li><a href="#">官方微博</a></li>
                        <li><a href="#">问题反馈</a></li>
                        <li><a href="#">会员中心</a></li>
                    </ul>
                </nav>
            </div>
        </nav>
    </div>

    <div class="s1">
        <form action="UserServlet?flag=login" method="post">
            <div class="login">
                <h3>帐号密码登录</h3>
                <p><input type="text" name="userName"  placeholder="手机/邮箱/用户名"  ></p>
                <p><input type="password" name="password"  placeholder="密码"  ></p>
                <p><input type="checkbox"  checked>下次自动登录   <br><span   class="errmsg"  >${msg }</span>
                <p><button>登录</button></p>
                <p><a href="#">忘记密码</a><a href="register.jsp">立即注册</a></p>
            </div>
            <div class="font">
                <a href="#" class="text">扫一扫登录</a>
                <a href="#"><img src="images/link1.png"></a>
                <a href="#"><img src="images/link2.png"></a>
                <a href="#"><img src="images/link3.png"></a>
            </div>
        </form>
    </div>

    <div class="slides-box">
        <ul class="slides">
            <li style="background: url(images/banner.jpg) center">
            </li>
            <li style="background: url(images/banner1.jpg)center"></li>
            <li style="background: url(images/banner3.jpg) center"></li>
            <li style="background: url(images/banner2.jpg)center"></li>
            <li style="background: url(images/banner1.jpg)center"></li>
        </ul>
    </div>
</div>

<div class="footer">
    <div>
        <a href="#"><img src="images/item1.png"></a>
        <a href="#"><img src="images/item2.png"></a>
        <a href="#"><img src="images/item3.png"></a>
        <a href="#"><img src="images/item4.png"></a>
        <a href="#"><img src="images/item5.png"></a>
    </div>
    <p>
        ?2023 海康
        <a href="#">移动开放平台</a> | <a href="#">服务协议</a> |
        <a href="#">权利声明</a> | <a href="#">版本更新</a> |
        <a href="#">帮助中心</a> | <a href="#">版权投诉</a>
    </p>
</div>
</body>
</html>

