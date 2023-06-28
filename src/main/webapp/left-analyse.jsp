<%@ page language="java" import="java.util.*,com.dao.*,com.beans.UserInfo,com.util.*,com.dao.impl.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <style>
        #ul1 li:not(:first-child) {
            text-align: left;
            padding-left:25px;
            padding-right:25px;
        }

    </style>
    <script src="js/jquery-2.1.4.min.js"></script>

</head>

<body>
<div class="skin-main">

    <div class="frame-aside">
        <ul id="ul1">
            <li><a href="HdfsServlet?flag=manage"  target="centerFrame" class="active"><img src="images/fileIcons/all.png" />数据分析</a></li>
            <li><a href="MapReduceServlet?flag=searchFilesForWordCount&type=txt" target="centerFrame" ><img src="images/fileIcons/icon0.png" />MR-WordCount</a></li>
            <li><a href="MapReduceServlet?flag=removeRepeat&type=txt" target="centerFrame" ><img src="images/fileIcons/icon0.png" />MR-去重复</a></li>
            <li><a href="MapReduceServlet?flag=searchFilesForSort&type=txt" target="centerFrame" ><img src="images/fileIcons/icon0.png" />MR-排序</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />MR-分区计算示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />MR-日志分析示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />HIVE-分析示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />SPARK-分析示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />FLUME-示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />SCOOP-传输示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />SPARK-分析示例</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />AZKABAN 任务调度</a></li>
            <li><a href="" target="centerFrame" ><img src="images/fileIcons/icon0.png" />手动任务调度</a></li>
            <li>
                <div class="aside-absolute">
                    <div class="remaining">
                        <div class="remainingSpace"></div>
                    </div>
                    <div class="font">
                        <%
                            HdfsDao dao=new HdfsDaoImpl();
                            UserInfo user=(UserInfo)session.getAttribute("session_user");
                            float size=dao.getUserDiskSize(user.getUserName());
                            String sizeStr=StrUtil.getSizeStr(size);
                            double percent = size/(1024*1024*1024);
                            String percentStr=	String.format("%.2f",percent*100);
                        %>

                        <span><%=sizeStr %> / 1 G</span>
                    </div>
                    <div  class="layout-absolute">
                        <img src="images/icon4.png">
                        <img src="images/icon5.png">
                        <img src="images/icon6.png">
                        <img src="images/icon7.png">
                    </div>
                </div>
            </li>

        </ul>

    </div>
</div>

<script>
    $(function (){
        <%--$(".remainingSpace").css("width","<%=percentStr%>");--%>
        let total= parseFloat($(".remaining").css("width"))
        let percent =<%=percent%>
            percent = total * percent;
        $(".remainingSpace").css("width",percent);

        $("li").click(function (){
            $(this).children("a").addClass("active");
            //给当前元素的兄弟元素去掉一个样式
            $(this).siblings().children("a").removeClass("active");
        })
    })
</script>
</body>
</html>