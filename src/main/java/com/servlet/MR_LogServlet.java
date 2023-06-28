package com.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


@WebServlet("/MR_LogServlet")
public class MR_LogServlet extends HttpServlet {
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
            job.setMapOutputValueClass(LongWritable.class);

            String filePath = request.getParameter("filePath");
            String user=filePath.substring(0,filePath.indexOf("/"));
            String filename=filePath.substring(filePath.lastIndexOf("/"));
            FileInputFormat.setInputPaths(job, new Path(HDFS_PATH + filePath));


            // 设定 reduce 相关的配置
            job.setReducerClass(TotalReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);

            // 因为如果目标目录存在,将出错,所以可以先将目标删除
            URI uri = new URI(HDFS_PATH);
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(uri, conf,"hadoop");
            fs.delete(new Path(HDFS_PATH+user+"/MapReduceResult/LogAnalyse"+filename), true);

            // 指明计算完成以后,输出结果放在哪里
            FileOutputFormat.setOutputPath(job, new Path(HDFS_PATH+user+"/MapReduceResult/LogAnalyse"+filename));

            // 提交作业
            boolean result=job.waitForCompletion(true);


            Path path = new Path(HDFS_PATH+user+"/MapReduceResult/LogAnalyse"+filename+"/part-r-00000");
            FSDataInputStream fsInput = fs.open(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fsInput, "utf-8"));

            Map<String ,Long> ipMap=new HashMap<>();

            String str = null;

            while ((str = br.readLine()) != null) {
                String[] data = str.split("\t");

                String ip=data[0];
                Long times= Long.valueOf(data[1]);
                ipMap.put(ip,times);
            }

            br.close();
            fsInput.close();
            fs.close();

            request.setAttribute("ipMap",ipMap);
            request.getRequestDispatcher("/mapreduce/log-analyse-result.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static class TotalMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        Text outk= new Text();
        LongWritable outv=new LongWritable(1);
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] data = line.split(",");
            outk.set(data[1]);
            context.write(outk, outv);
        }
    }

    public static class TotalReduce extends Reducer<Text, LongWritable, Text,LongWritable> {
        Long count=0L;

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
            count=0L;
            for (LongWritable value : values) {
                count+= value.get();
            }
            context.write(key,new LongWritable(count));
        }
    }

}