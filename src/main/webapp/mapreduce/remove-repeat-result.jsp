<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>

    <script src="/js/jquery-2.1.4.min.js"> </script>
    <script src="/js/echarts.js"></script>
    <script>
        $(function(){
            $.ajax({
                url :"MR_RemoveRepeatServlet",
                dataType : "json",
                data:{filePath:'${param.filePath}'},  //要注意,它是从上个页面选的文件
                success : function(wordList) {
                    $("#imgwait").hide();
                    for(var i=0;i<wordList.length;i++){
                        var li= "<li>"+wordList[i]+"</li>"
                        $("#ul1").append(li);
                    }
                }
            })
        });




    </script>
</head>

<body>
<div>

    <cite style="color:#A30014;font-weight:bold;font-family: 'Montserrat', sans-serif" >
        <a href="javascript:window.history.back()"   >
            <img width="25" src="images/fileIcons/back.png" >
        </a>
        &nbsp;	&nbsp;	&nbsp;	&nbsp; MR-去重复     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>${param.filePath} 文件内共有如下单词: </cite>
    <hr />
    <ul id="ul1">
    </ul>

    <div>
        <img id="imgwait"   src="images/processing.gif" />
        <div id="main" style="width: 1080px; height: 400px;"></div>
    </div>



</div>

</body>
</html>