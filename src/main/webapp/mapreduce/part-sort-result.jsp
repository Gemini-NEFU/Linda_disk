<%@ page language="java" import="java.util.*,com.beans.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
  <base href="<%=basePath%>">

  <title></title>

  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">

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
    });

  </script>
</head>

<body>
<div>

  <cite style="color:#A30014;font-weight:bold;font-family: 'Montserrat', 'sans-serif' " >
    <a href="javascript:window.history.back()"  >
      <img  width="25" src="images/fileIcons/back.png"  ></a>
    &nbsp;	&nbsp;	&nbsp;	&nbsp; MR-分区排序示例 ${param.filePath}
  </cite>
  <hr />

  <div id="main" style="width: 600px;height:370px; border:1px solid blue;float:left;margin-right: 20px"></div>
  <div id="chart1" style="width: 600px;height:370px;float:left;margin-bottom:20px"></div>

  <c:forEach var="data" items="${dataList}">
    <table id="center_table">
      <tr>
        <th width="200">棋手ID</th>
        <th>棋手地域</th>
        <th>棋手姓名</th>
        <th>棋手积分</th>
        <th>棋手性别</th>
      </tr>


      <c:forEach var="scoreInfo" items="${data }">
        <tr>
          <td width="200"><img src="images/fileIcons/wordIcon.png"  width="15" > &nbsp;&nbsp; &nbsp; &nbsp;  ${scoreInfo.idCard } </td>
          <td>${scoreInfo.address }</td>
          <td>${scoreInfo.name }</td>
          <td>${scoreInfo.score }</td>
          <td>${scoreInfo.gender }</td>
        </tr>
      </c:forEach>
    </table>
  </c:forEach>

</div>

<%
  //计算每个表的汇总

  List<List<ScoreInfo>> dataList= (List<List<ScoreInfo>>)request.getAttribute("dataList");

  int total0=0;
  int total1=0;
  int total2=0;
  int total3=0;
  int total4=0;

  for(ScoreInfo s:dataList.get(0)){
    total0+=s.getScore();
  }
  for(ScoreInfo s:dataList.get(1)){
    total1+=s.getScore();
  }
  for(ScoreInfo s:dataList.get(2)){
    total2+=s.getScore();
  }
  for(ScoreInfo s:dataList.get(3)){
    total3+=s.getScore();
  }
  for(ScoreInfo s:dataList.get(4)){
    total4+=s.getScore();
  }

%>


<script>
  var myChart = echarts.init(document.getElementById('main'));
  myChart.setOption({
    backgroundColor:'#F9F9F9',
    // 图例
    legend: [{
      selectedMode:true,             // 图例选择的模式，控制是否可以通过点击图例改变系列的显示状态。默认开启图例选择，可以设成 false 关闭。
      top: '10%',
      left: 'center',
      textStyle: {                      // 图例的公用文本样式。
        fontSize: 14,
        color: 'black'
      },
      data: ['北京','上海','广州','深圳','其他']
    }],

    series : [
      {
        name: '地区积分对比',
        type: 'pie',    // 设置图表类型为饼图
        radius: '55%',  // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
        color:['#91CC74', '#5C7BDA', '#EE6666', '#FAC858','#888888'],
        data:[          // 数据数组，name 为数据项名称，value 为数据项值
          {value: <%=total0 %>, name:'北京'},
          {value: <%=total1 %>, name:'上海'},
          {value: <%=total2 %>, name:'广州'},
          {value: <%=total3 %>, name:'深圳'},
          {value: <%=total4 %>, name:'其他'}
        ]
      }
    ]
  })
</script>

<script>
  var chart1 = echarts.init(document.getElementById("chart1"));

  // 圆环图各环节的颜色
  var color = ['#91CC74', '#5C7BDA', '#EE6666', '#FAC858','#888888'];

  // 圆环图各环节的名称和值(系列中各数据项的名称和值)
  var data =[{
    name: '北京',
    value: <%=total0%>
  },{
    name: '上海',
    value: <%=total1%>
  },{
    name: '广州',
    value: <%=total2%>
  },{
    name: '深圳',
    value: <%=total3%>
  }, {
    name: '其他',
    value: <%=total4%>
  }
  ];

  // 指定图表的配置项和数据
  var option = {

    //背景色
    /*
    backgroundColor: {			// 背景颜色
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
            offset: 0, color: 'rgba(0,0,0,0.4)' // 0% 处的颜色
        }, {
            offset: 0.5, color: 'rgba(0,0,0,0.4)' 	// 50% 处的颜色
        }, {
            offset: 1, color: 'rgba(0,0,0,0.4)' // 100% 处的颜色
        }],
        globalCoord: false // 缺省为 false
    },
    */
    backgroundColor:'#F9F9F9',

    // 标题
    title: [{
      text: '地域积分对比',
      top:'5%',
      left:'3%',
      textStyle:{
        color: '#000',
        fontSize:18,
      }
    }],

    // 图例
    legend: [{
      selectedMode:true,             // 图例选择的模式，控制是否可以通过点击图例改变系列的显示状态。默认开启图例选择，可以设成 false 关闭。
      top: '10%',
      left: 'center',
      textStyle: {                      // 图例的公用文本样式。
        fontSize: 14,
        color: 'black'
      },
      data: ['北京','上海','广洲','深圳','其他']
    }],

    // 提示框
    tooltip: {
      show: true,                 // 是否显示提示框
      formatter: '{b} </br> 积分{c}件 </br> 占比{d}%'      // 提示框显示内容,此处{b}表示各数据项名称，此项配置为默认显示项，{c}表示数据项的值，默认不显示，({d}%)表示数据项项占比，默认不显示。
    },

    // graphic 是原生图形元素组件。可以支持的图形元素包括：image, text, circle, sector, ring, polygon, polyline, rect, line, bezierCurve, arc, group,
    graphic: {
      type: 'text',               // [ default: image ]用 setOption 首次设定图形元素时必须指定。image, text, circle, sector, ring, polygon, polyline, rect, line, bezierCurve, arc, group,
      top: 'center',              // 描述怎么根据父元素进行定位。top 和 bottom 只有一个可以生效。如果指定 top 或 bottom，则 shape 里的 y、cy 等定位属性不再生效。『父元素』是指：如果是顶层元素，父元素是 echarts 图表容器。如果是 group 的子元素，父元素就是 group 元素。
      left: 'center',             // 同上
      style: {
        text: '各地域积分对比',       // 文本块文字。可以使用 \n 来换行。[ default: '' ]
        fill: 'brown',           // 填充色。
        fontSize: 16,           // 字体大小
        fontWeight: 'bold'		// 文字字体的粗细，可选'normal'，'bold'，'bolder'，'lighter'
      }
    },

    // 系列列表
    series: [{
      name: '圆环图系列名称',         // 系列名称
      type: 'pie',                    // 系列类型
      center:['50%','50%'],           // 饼图的中心（圆心）坐标，数组的第一项是横坐标，第二项是纵坐标。[ default: ['50%', '50%'] ]
      radius: ['30%', '45%'],         // 饼图的半径，数组的第一项是内半径，第二项是外半径。[ default: [0, '75%'] ]
      hoverAnimation: true,           // 是否开启 hover 在扇区上的放大动画效果。[ default: true ]
      color: color,                   // 圆环图的颜色
      label: {                        // 饼图图形上的文本标签，可用于说明图形的一些数据信息，比如值，名称等.
        normal: {
          show: true,             // 是否显示标签[ default: false ]
          position: 'outside',    // 标签的位置。'outside'饼图扇区外侧，通过视觉引导线连到相应的扇区。'inside','inner' 同 'inside',饼图扇区内部。'center'在饼图中心位置。
          formatter: '{b} : {c} '  // 标签内容
        }
      },
      labelLine: {                    // 标签的视觉引导线样式,在 label 位置 设置为'outside'的时候会显示视觉引导线。
        normal: {
          show: true,             // 是否显示视觉引导线。
          length: 15,             // 在 label 位置 设置为'outside'的时候会显示视觉引导线。
          length2: 10,            // 视觉引导项第二段的长度。
          lineStyle: {            // 视觉引导线的样式
            //color: '#000',
            //width: 1
          }
        }
      },
      data: data                      // 系列中的数据内容数组。
    }]
  };

  // 使用刚指定的配置项和数据显示图表
  chart1.setOption(option)
</script>
</body>
</html>