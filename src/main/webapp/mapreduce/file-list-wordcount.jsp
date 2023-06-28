<%@ page language="java" import="java.util.*, org.apache.hadoop.fs.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>海康云盘系统</title>

    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <script src="js/jquery-2.1.4.min.js" ></script>

    <script>

        //点击文件夹时,要打开它的子级页面
        function getSubFiles( parent){
            window.location.href="HdfsServlet?flag=manageSubFiles&parent="+ encodeURI(parent);
        }

        function initTrEvent(){
            $("#center_table tr").mouseover(
                function(){
                    $(this).siblings().removeClass("highlightTd");
                    $(this).addClass("highlightTd");
                    $(this).find("div").show();  //显示出对该文件的操作按钮,有分享,下载,删除三个按钮
                    $(this).siblings().find("div").hide();
                });
        }

        $(function(){
            initTrEvent();
        });

        function wordcount(filePath){
            var encodePath=encodeURI(filePath);
            window.location.href="mapreduce/word-count-result.jsp?filePath="+encodePath;
        }

    </script>
</head>

<body>
<div class="frame-center">

    <div class="frame-title">
        	   <span>
	        	    <a href="HdfsServlet?flag=manage"> 全部文件  | MR-WordCount</a>
	              	<c:forEach  var="url" items="${urlList }">
                        <a onclick='getSubFiles("${fn:split(url,"_")[0] }")'>  &gt;  ${fn:split(url,"_")[1]}  </a>
                    </c:forEach>
              	</span>
        <span>
            		  已全部加载，共  <label>${fn:length(hdfsFileList)} </label>个
                </span>
        <img id="img1" style="display:none" src="images/processing.gif" />
    </div>

    <div class="tab">
        <table>
            <tr>
                <td> 可分析文件</td>
                <td></td>
                <td>大小</td>
                <td>修改日期</td>
            </tr>
        </table>

        <div class="datas">
            <table id="center_table">
                <c:forEach var="f" items="${hdfsFileList}">
                    <tr>
                        <td>
                            &nbsp; <img src="images/fileIcons/${f.icon }">
                            <label  <c:if test="${f.f.directory==true }" > onclick="getSubFiles('${f.path}')" </c:if> > ${f.name }</label>
                        </td>
                        <td >
                            <div>
                                <a href="javascript:wordcount('${f.path }')"><img src="images/a.png" title="词频分析"  />词频分析</a>
                            </div>
                        </td>
                        <td>${f.len}</td>
                        <td>${f.modificationTime}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>