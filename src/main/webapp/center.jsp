<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>海康云盘系统</title>
    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <script src="js/jquery-2.1.4.min.js" ></script>
    <script>
        //这里写一些页面加载的时候要做的事
        $(function(){
            initTrEvent();
        })
        //下面是声明一个javacript函数
        function initTrEvent(){
            //.给选中的元素加一个鼠标滑过的事件
            $("#center_table tr").mouseover(function(){
                //给当前元素加一个样式
                $(this).addClass("highlightTd");
                //给当前元素的兄弟元素去掉一个样式
                $(this).siblings().removeClass("highlightTd");

                //控制某些元素的显示和隐藏
                $(this).find("div").show(); //显示出分享,下载,删除
                $(this).siblings().find("div").hide();
            });
        }

        //
        function getSubFiles(parent) {
            window.location.href="HdfsServlet?flag=managerSubFiles&parent="+encodeURI(parent);
        }
    </script>
</head>

<body>
<div class="frame-center">
    <div class="frame-nav">
        <div class="centerLeft">
            <a class="active"><img src="images/upload.png"  />上传</a>
            <a><img src="images/createfolder.png"/>新建文件夹</a>
            <a><img src="images/download.png" />离线下载</a>
            <a><img src="images/machine.png" />我的设备</a>
        </div>
        <div class="centerRight">
            <p>
                <input type="text" name="fileName" placeholder="搜素你的文件" value="">
                <img src="images/search.png"  title="搜索"  />
            </p>
            <img src="images/menu1.png">
            <img src="images/menu2.png">
        </div>
    </div>

    <div class="frame-title">
        	   <span>
	        	    <a> 全部文件  | </a>
	                <a>  &gt;    </a>
              	</span>
        <span>
            		  已全部加载，共  <label>${fn:length(hdfsFileList)} </label>个
                </span>
    </div>

    <div class="tab">
        <table>
            <tr>
                <td><input type="checkbox">文件名</td>
                <td></td>
                <td>大小</td>
                <td>修改日期</td>
            </tr>
        </table>

        <div class="datas">
            <table id="center_table">
                <c:forEach var="f" items="${hdfsFileList}">
                    <tr class="highlightTd">
                        <td>
                            &nbsp; <input type="checkbox"><img src="images/fileIcons/${f.icon}">
                            <label   <c:if test="${f.f.directory==true }"> onclick="getSubFiles('${f.path }')" style='cursor:pointer' </c:if> >
                                    ${f.name }
                            </label>
                        </td>
                        <td >
                            <div>
                                <a><img src="images/a.png" title="分享"  /></a>
                                <a><img src="images/b.png"  title="下载"  /></a>
                                <a><img src="images/c.png"  title="删除"  /></a>
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

