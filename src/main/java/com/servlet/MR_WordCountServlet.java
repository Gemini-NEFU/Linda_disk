package com.servlet;

import com.beans.WordCountInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import net.sf.json.JSONArray;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/MR_WordCountServlet")
public class MR_WordCountServlet extends HttpServlet {
    static final String HDFS_PATH = "hdfs://hadoophyq:8020/";
    protected void service(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            // 创建一个作业
            Job job = Job.getInstance();

            // 设定map 相关的配置
            job.setMapperClass(WordCountMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(LongWritable.class);

            // 设定 reduce 相关的配置
            job.setReducerClass(WordCountReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);

            //指定用户输入文件的路径
            String filePath=request.getParameter("filePath");
            String user=filePath.substring(0,filePath.indexOf("/"));
            String filename=filePath.substring(filePath.lastIndexOf("/"));
            Path path=new Path(HDFS_PATH+filePath);
            FileInputFormat.setInputPaths(job, path);

            //指定计算之后的结果存放的位置
            Path path2=new Path(HDFS_PATH+user+"/MapReduceResult/WordCountResult"+filename);
            FileOutputFormat.setOutputPath(job,path2 );

            //如果存放位置存在,则先删除,不然会出错
            URI uri=new URI(HDFS_PATH);
            Configuration conf=new Configuration();
            FileSystem fs=FileSystem.get(uri,conf);
            fs.delete(path2,true);


            job.waitForCompletion(true);

            System.out.println("任务执行完毕");


            //提取出分析结果
            Path path3=new Path(HDFS_PATH+user+"/MapReduceResult/WordCountResult"+filename+"/part-r-00000");

            FSDataInputStream fsInfput = fs.open(path3);

            //把fsInfput这个字节流,包装成缓冲流,包装以后,可以一行一行的读取
            BufferedReader br=new BufferedReader(new InputStreamReader(fsInfput));
            String str= null;
            List<WordCountInfo> wordCountList=new ArrayList<>();
            while( (str=br.readLine())!=null) {
                System.out.println(str);
                String [] data =str.split("\t");
                String word=data[0];
                Integer count= Integer.parseInt(data[1]);
                WordCountInfo info=new WordCountInfo(count, word);
                wordCountList.add(info);
            }

            for(WordCountInfo w:wordCountList) {
                System.out.println(w);
            }

            br.close();
            fsInfput.close();

            response.setContentType("text/html;charset=utf-8");

            //把 wordCountList集合,处理成json 格式传给前台
            JSONArray  jsonObj=JSONArray.fromObject(wordCountList);

            //是向调用者返回数据
            response.getWriter().print(jsonObj);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    // KEYIN LongWritable key 代表行号
    // VALUEIN Text value 代表当前进行处理的那行数据
    // KEYOUT 往后面写的key的类型
    // VALUEOUT 往后面写的value的类型
    static class WordCountMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        Text outk=new Text();
        LongWritable outv=new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String [] wordlist=line.split(" ");

            for(String word:wordlist){
                outk.set(word);
                context.write(outk,outv);
            }

        }
    }
    static class WordCountReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        LongWritable n=new LongWritable();
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values,Context context) throws IOException, InterruptedException {
            Long count=0L;
            for (LongWritable i:values) {
                count+=i.get();
            }
            n.set(count);

            context.write(key,n);
        }
    }
}
