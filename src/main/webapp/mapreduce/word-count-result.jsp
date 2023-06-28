<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>

    <script src="js/jquery-2.1.4.min.js"> </script>
    <script src="js/echarts.js"></script>
    <script>
        $(function(){
            render();
        });

        //从远程提取数据
        function render(){
            $.ajax({
                url:"MR_WordCountServlet",
                dataType:"json",
                data:{filePath:'${param.filePath}'},
                success: function(wordCountList){
                    //在服务端 wordCountList 就是 List<WordCountInfo>
                    //横轴 单词
                    var array1=new Array();
                    //纵轴 数量
                    var array2=new Array();

                    for(var i=0;i<=wordCountList.length;i++){
                        if(wordCountList[i]){
                            array1.push(wordCountList[i].word);
                            array2.push(wordCountList[i].count);
                        }
                    }

                    var myChart = echarts.init(document.getElementById("main") );
                    initChart(myChart,array1,array2,"文件词频分析", "#4D97FF");

                    $("#imgwait").hide();
                }
            });
        }



        function initChart(myChart, array1, array2, title, color) {
            // 指定图表的配置项和数据
            var option = {
                color : [ color ],
                backgroundColor : '#F9F9F9',

                title : [ {
                    text : title,
                    left : '470',
                    top : '15',
                    textStyle : {
                        fontSize : 16,
                        color : '#A30014',
                    },

                }, {
                    text : "数据排行",
                    left : '590',
                    top : '15',
                    textStyle : {
                        fontSize : 16
                    }
                } ],

                tooltip : {},

                xAxis : {
                    //	type:"category",
                    data : array1,
                    splitLine : {
                        show : true,
                        lineStyle : {
                            color : [ '#D9D9D9' ],
                            width : 1,
                            type : 'solid'
                        }
                    }
                },
                yAxis : {
                    //type:'value',
                    //网格样式
                    splitLine : {
                        show : true,
                        lineStyle : {
                            color : [ '#D9D9D9' ],
                            width : 1,
                            type : 'solid'
                        }
                    }
                },
                series : [ {
                    label : {
                        show : true,//是否显示数值
                        //rotate : 60,//数值是否旋转
                        position : 'top'//设置显示数值位置 top：顶部 bottom：底部
                    },

                    name : title.substring(0, 4),
                    type : 'bar',
                    data : array2
                },

                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }

    </script>

</head>

<body>
<div class="wrap">
    <div class="x-nav">
        <div class="title">
            <a><cite>MR-WordCount示例</cite></a>
        </div>
    </div>
    <div class="x-body">
        <div class="content">
            <div class="monthly">
                <p>
                    <a href="javascript:window.history.back()"   > <img width="25" src="images/fileIcons/back.png" ></a>&nbsp;	&nbsp;	&nbsp;	&nbsp;<label for="sel" style="color:#A30014;font-weight:bold;font-family: 'Montserrat', sans-serif">词频分析 >${param.filePath} </label>
                </p>
                <hr />

                <div>
                    <img id="imgwait"   src="images/processing.gif" />
                    <div id="main" style="width: 1080px; height: 400px;"></div>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>