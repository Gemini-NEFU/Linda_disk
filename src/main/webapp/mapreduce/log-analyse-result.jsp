<%@ page language="java" import="java.util.*,com.beans.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <meta charset="utf-8">
    <title>数据统计分析</title>
    <meta http-equiv="Cache-Control" content="no-cache" />
    <script src="js/jquery-2.1.4.min.js"></script>
    <script src="js/echarts.js"></script>

    <style>
        table{
            border-collapse: collapse;
            border:1px solid #EAEAEA;
            width:80%;
        }

        td,th{
            border:1px solid #EAEAEA;
            height:25px;
            font-size: 12px; font-family: 微软雅黑;
            padding-left:10px;
        }

        th{
            background:#F4FBFF;
        }

        .highlightTd{
            background:#F4FBFF;
            font-weight:bold;
        }


    </style>
    <script>
        $(function(){
            $("#center_table tr").mouseover(
                function(){
                    $(this).siblings().removeClass("highlightTd");
                    $(this).addClass("highlightTd");

                });

            $()
        });

    </script>

</head>

<body>
<div>

    <cite style="color:#A30014;font-weight:bold;font-family: 'Montserrat', 'sans-serif' " >
        <a href="javascript:window.history.back()"  >
            <img  width="25" src="images/fileIcons/back.png"  ></a>
        &nbsp;	&nbsp;	&nbsp;	&nbsp; MR-日志分析 ${param.filePath}
    </cite>
    <hr />

    <img id="imgwait" src="images/processing.gif" >
    <table id="center_table">
        <tr>
            <th width="200">用户IP</th>
            <th>访问次数</th>
        </tr>

        <c:forEach var="one" items="${ipMap}" >
            <tr>
                <td width="200">  <img src="images/fileIcons/wordIcon.png"  width="15" > &nbsp;&nbsp; &nbsp; &nbsp;  ${one.key }</td>
                <td>${one.value }</td>
            </tr>
        </c:forEach>

    </table>


</div>

<div id="main" style="width: 700px;height:400px;"></div>

<script>

    <%
        Map<String ,Long> ipMap=(Map<String, Long>) request.getAttribute("ipMap");
    %>
    <c:if test="${ipMap.size!=0}">
    $("#imgwait").hide();
    </c:if>
    var length=12;//此处为动态数据的长度
    var hei=(length*60)+'px';//动态获取图表高度
    $("#main").css('height',hei);//动态设置图表高度
    showchart();   //图表执行

    //图表
    function showchart(){//console.log(dbcnumb)
        var myChart = echarts.init(document.getElementById('main'));

        option = {
            title : {
                text: '',
                subtext: ''
            },

            tooltip : {
                trigger: 'item'  //悬浮提示框不显示
            },
            grid:{   //绘图区调整
                x:150,  //左留白
                y:10,   //上留白
                x2:10,  //右留白
                y2:10   //下留白
            },
            xAxis : [
                {
                    show:false,
                    type : 'value',
                    boundaryGap : [0, 0],
                    position: 'top'
                }
            ],
            yAxis : [
                {
                    type : 'category',
                    data : [
                        <%
                        for(Map.Entry entry:ipMap.entrySet()){
                            out.print("'"+entry.getKey()+"',");
                        }
                        %>
                    ],
                    axisLine:{show:false},     //坐标轴
                    axisTick:[{    //坐标轴小标记
                        show:false
                    }],
                    axisLabel:{
                        textStyle:{
                            fontSize:'14'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'',
                    type:'bar',
                    tooltip:{show:false},
                    barMinHeight:40,  //最小柱高
                    barWidth: 22,  //柱宽度
                    barMaxWidth:100,   //最大柱宽度
                    data:[
                        //1, 23489, 29034, 104970, 0, 63030,63230,30230,63030,63230,63030,63230
                        <%
                        for(Map.Entry entry:ipMap.entrySet()){
                            out.print("'"+entry.getValue()+"',");
                        }
                        %>
                    ],
                    itemStyle:{
                        normal:{    //柱状图颜色
                            color:'#E0B478',
                            label:{
                                show: true,   //显示文本
                                position: 'inside',  //数据值位置   //insideleft 为居左
                                textStyle:{
                                    color:'#000',
                                    fontSize:'14'
                                }
                            }
                        }
                    }
                }
            ]
        };
//	          window.onresize = function () {  //适应页面
//	              myChartContainer();
//	              myChart.resize();
//	          }
        myChart.setOption(option);

    }

</script>
</body>
</html>