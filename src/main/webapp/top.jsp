<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <title></title>
</head>
<script>
    //退出
    function logout(){
        if(confirm("确定要注销退出吗")==true)
        {
            window.location.href="UserServlet?flag=logout";
        }
    }
    function f1(){
        window.top.leftFrame.location.href="left.jsp";
        $(this).addClass("active");
    }
    function f2(item) {
        window.top.leftFrame.location.href = "left-analyse.jsp";
        $(this).addClass("active");
    }
</script>
<body>
<div class="frame-all">
    <div class="layoutHeader">
        <div class="listLeft">
            <img src="images/1.png" height="57" >
            <a href="javascript:f1()" class="active">网盘</a>
            <a href="javascript:f2()" class="active">数据分析</a>
            <a href="#">分享</a>
            <a href="#">找资源</a>
            <a href="#">更多</a>
        </div>
        <div class="listRight">
            你好: ${session_user.userName}
            <img src="images/t3.png" class="photo">
            <span> </span>
            <img src="images/level.png">
            <img src="images/btn.png">
            <i>| </i>
            <a href="javascript:logout()">
                <img width="27px" title="注销退出" src="images/logout.png" >
            </a>
            <img src="images/icon1.png">
            <img src="images/icon2.png">
            <img src="images/icon3.png">
            <a href="#">会员中心</a>
        </div>
    </div>
</div>
</body>
</html>

