<%@ page language="java" import="java.util.*,com.util.*,com.dao.*,com.dao.impl.*,com.beans.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link rel="stylesheet" href="css/poposlides.css">
  <link rel="stylesheet" href="css/layout.css">
  <script src="js/jquery-2.1.4.min.js"></script>
</head>

<body>
<div class="skin-main">
  <div class="frame-aside">
    <ul>
      <li><a href="HdfsServlet?flag=manage"  target="centerFrame" class="active"><img src="images/fileIcons/all.png" />全部文件</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=picture" target="centerFrame" ><img src="images/fileIcons/picIcon.png" /> 图片</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=txt" target="centerFrame" ><img src="images/fileIcons/wordIcon.png" /> 文档</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=avi" target="centerFrame" ><img src="images/fileIcons/aviIcon.png" /> 视频</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=torrent" target="centerFrame" ><img src="images/fileIcons/pdfIcon.png" /> 种子</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=sound" target="centerFrame" ><img src="images/fileIcons/soundIcon.png" /> 音乐</a></li>
      <li><a href="HdfsServlet?flag=searchFiles&type=gz" target="centerFrame" ><img src="images/fileIcons/zipIcon.png" /> 压缩</a></li>
      <li><a href="HdfsServlet?flag=manage" target="centerFrame" ><img src="images/fileIcons/sourceCodeIcon.png" /> 源码</a></li>
      <li><a href="HdfsServlet?flag=manage" target="centerFrame" ><img src="images/fileIcons/programIcon.png" /> 项目</a></li>
      <li><a href="HdfsServlet?flag=manage" target="centerFrame" ><img src="images/fileIcons/txtIcon.png" /> 资源</a></li>
      <li>
        <div class="aside-absolute">
          <div class="remaining">
            <div class="remainingSpace"></div>
          </div>
          <div class="font">
            <%
              HdfsDao hdfsDao=new HdfsDaoImpl();
              UserInfo user=(UserInfo)session.getAttribute("session_user");
              float size=hdfsDao.getUserDiskSize(user.getUserName());
              String sizeStr=StrUtil.getSizeStr(size);
              double percent = size/(1024*1024*1024);  //1G
              String percentStr=String.format("%.2f",percent*100);
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
