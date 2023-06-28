package com.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.beans.ScoreInfo;


@WebServlet("/MR_SortServlet")
public class MR_SortServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final String HDFS_PATH = "hdfs://hadoophyq:8020/";
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 创建一个作业
            Job job = Job.getInstance();

            // 指定main函数所在的类
            //job.setJarByClass(SortPattitonReduce.class);

            // 设定map 相关的配置
            job.setMapperClass(TotalMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(ScoreInfo.class);

            String filePath = request.getParameter("filePath");
            String user=filePath.substring(0,filePath.indexOf("/"));
            String filename=filePath.substring(filePath.lastIndexOf("/"));
            FileInputFormat.setInputPaths(job, new Path(HDFS_PATH + filePath));


            // 设定 reduce 相关的配置
            job.setReducerClass(TotalReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(ScoreInfo.class);

            // 因为如果目标目录存在,将出错,所以可以先将目标删除
            URI uri = new URI(HDFS_PATH);
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(uri, conf,"hadoop");
            fs.delete(new Path(HDFS_PATH+user+"/MapReduceResult/SortResult_temp"+filename), true);

            // 指明计算完成以后,输出结果放在哪里
            FileOutputFormat.setOutputPath(job, new Path(HDFS_PATH+user+"/MapReduceResult/SortResult_temp"+filename));

            // 提交作业
            job.waitForCompletion(true); // true 表示在执行作业的时候输出提示信

			/*banbenhuazhi	深圳	板本共织	 39	女
				chenglanru	北京	程兰如	 34	男
				chepingping	上海	车平平	 324	女
				cuijing	上海	崔精	 64	女
				fengyun	广州	风云	 28	女
				huayigang	上海	华以刚	 104	男
				lishishi	广州	李世石	 89	男
				liudahua	北京	柳大华	 112	男
				mengtailing	广州	孟泰龄	 117	男
				sumengzhen	上海	栗梦真	 38	女
				wanyuan	北京	王元	 56	男
				wugongzhengshu	日本	武宫正树	 78	男
				xubaofeng	北京	许宝凤	 68	女
				xuyinchuan	深圳	许银川	 75	男
				yaoyongxun	广州	姚永寻	 144	女
				yindebao	广州	尹德宝	 69	男
				zhanghe	深圳	仗和	 84	男
				zhaozhixun	日本	赵治勋	 159	男
				zhongjinhui	韩国	仲瑾惠	 77	女  */


            //上面是完成了reduce,下面是再进行一次map目的是排序
            Job job2 = Job.getInstance();

            // 指定main函数所在的类,不用了
            //job2.setJarByClass(SortPattitonReduce.class);

            // 设定map 相关的配置
            job2.setMapperClass(SortMapper.class);
            job2.setMapOutputKeyClass(ScoreInfo.class);
            job2.setMapOutputValueClass(Text.class);


            FileInputFormat.setInputPaths(job2, new Path(HDFS_PATH+user+"/MapReduceResult/SortResult_temp"+filename+"/part-r-00000"));

            job2.setOutputKeyClass(ScoreInfo.class);
            job2.setOutputValueClass(Text.class);
            fs.delete(new Path(HDFS_PATH+user+"/MapReduceResult/SortResult"+filename), true);
            FileOutputFormat.setOutputPath(job2, new Path(HDFS_PATH+user+"/MapReduceResult/SortResult"+filename));


            boolean result=job2.waitForCompletion(true);


            Path path = new Path(HDFS_PATH+user+"/MapReduceResult/SortResult"+filename+"/part-r-00000");
            FSDataInputStream fsInput = fs.open(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fsInput, "utf-8"));

            List<ScoreInfo> scoreInfoList = new ArrayList<>();
            String str = null;

            while ((str = br.readLine()) != null) {
                String[] data = str.split("\t");

                String address=data[0];
                String name=data[1];
                String score=data[2];
                String gender=data[3];
                String idCard=data[4];

                scoreInfoList.add(new ScoreInfo(idCard,address,name,Integer.parseInt(score.trim()),gender));
            }

            br.close();
            fsInput.close();
            fs.close();

            request.setAttribute("scoreInfoList",scoreInfoList);
            request.getRequestDispatcher("/mapreduce/sort-result.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class TotalMapper extends Mapper<LongWritable, Text, Text, ScoreInfo> {
        ScoreInfo info = new ScoreInfo();
        Text k2 = new Text();

        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, ScoreInfo>.Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] data = line.split("\t");


            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);
            k2.set(info.getIdCard());
            context.write(k2, info);
        }
    }

    public static class TotalReduce extends Reducer<Text, ScoreInfo, Text, ScoreInfo> {
        int totalScore = 0;
        ScoreInfo info;

        protected void reduce(Text key, Iterable<ScoreInfo> values, Context context) throws IOException, InterruptedException {

            int i = 0;
            for (ScoreInfo o : values) {
                // 因为 其他信息也要输出去，这里假设id相同的用户，其他信息完全相同
                if (i == 0) {
                    info = o;
                }
                totalScore += o.getScore();
                i++;
            }

            info.setScore(totalScore);

            context.write(key, info);

            totalScore=0;
        }
    }

    public static class SortMapper extends Mapper<LongWritable, Text, ScoreInfo, Text> {
        ScoreInfo info = new ScoreInfo();
        Text k2 = new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] data = line.split("\t");

            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3].trim()));
            info.setGender(data[4]);

            k2.set(info.getIdCard());

            context.write(info,k2 );
        }
    }
}