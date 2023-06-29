package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapreduce.SortPartitionTest;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.beans.ScoreInfo;


@WebServlet("/MR_PartSortServlet")
public class MR_PartSortServlet extends HttpServlet {
    static final String HDFS_PATH = "hdfs://hadoophyq:8020/";
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            Job job = Job.getInstance();

            //指定main函数所在的类
//            job.setJarByClass(SortPartitionTest.class);

            //设定map相关的信息
            job.setMapperClass(TotalMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(ScoreInfo.class);

            //指定用户输入的文件的路径
            String filePath = request.getParameter("filePath");
            String user=filePath.substring(0,filePath.indexOf("/"));
            String filename=filePath.substring(filePath.lastIndexOf("/"));
            FileInputFormat.setInputPaths(job, new Path(HDFS_PATH + filePath));

            //设定reduce相关的信息
            job.setReducerClass(TotalReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(ScoreInfo.class);

            //指明计算结果存放的位置
            Path outputPath = new Path(HDFS_PATH+user+"/MapReduceResult/PartSortResult_temp"+filename);
            FileOutputFormat.setOutputPath(job, outputPath);

            //如果原来做过计算,先把原来生成的目录删除
            Configuration conf = new Configuration();
            URI uri = new URI(HDFS_PATH);
            FileSystem fs = FileSystem.get(uri, conf, "hadoop");
            fs.delete(outputPath, true);

            //提交作业 true 表示打印提示信息
            job.waitForCompletion(true);

            //创建第二个作业 (进行排序)
            Job job2 = Job.getInstance();
//            job2.setJarByClass(SortPartitionTest.class);

            //指定Map相关的配置
            job2.setMapperClass(SortMapper.class);
            job2.setMapOutputKeyClass(ScoreInfo.class);
            job2.setMapOutputValueClass(Text.class);

            //指定第二个map要读入的文件是哪个
            Path path3 = new Path(HDFS_PATH+user+"/MapReduceResult/PartSortResult_temp"+filename+"/part-r-00000");
            FileInputFormat.setInputPaths(job2, path3);

            job2.setOutputKeyClass(ScoreInfo.class);
            job2.setOutputValueClass(Text.class);

            //指定最终的结果存放地址
            Path path4 = new Path(HDFS_PATH+user+"/MapReduceResult/PartSortResult"+filename);
            FileOutputFormat.setOutputPath(job2, path4);

            //开启分区
            job2.setNumReduceTasks(5);
            job2.setPartitionerClass(MyPartition.class);

            fs.delete(path4, true);

            boolean result = job2.waitForCompletion(true);
//            List<List<ScoreInfo>> dataList= (List<List<ScoreInfo>>)request.getAttribute("dataList");
            List<List<ScoreInfo>> dataList=new ArrayList<>();

            String[] files=new String[]{"/part-r-00000","/part-r-00001","/part-r-00002","/part-r-00003","/part-r-00004"};
            for(String file:files){
                Path path = new Path(HDFS_PATH+user+"/MapReduceResult/PartSortResult"+filename+file);
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
                dataList.add(scoreInfoList);
            }
            fs.close();

            request.setAttribute("dataList",dataList);
            request.getRequestDispatcher("/mapreduce/part-sort-result.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        //建一个作业


    }

    public static class TotalMapper extends Mapper<LongWritable, Text, Text, ScoreInfo> {
        Text k2 = new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            //cuijing	上海	崔精	 64	女
            String line = value.toString();
            String[] data = line.split("\t");

            ScoreInfo info = new ScoreInfo();
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
        int i = 0;
        ScoreInfo info;

        protected void reduce(Text key, Iterable<ScoreInfo> values,
                              Context context) throws IOException, InterruptedException {
            for (ScoreInfo o : values) {
                //其为同一组的中的数据,除了分数外,其他的基本信息都是相同的,所以取第一个数据的信息就可以
                if (i == 0) {
                    info = o;
                }
                totalScore += o.getScore();
                i++;
            }

            info.setScore(totalScore);

            context.write(key, info);

            totalScore = 0;
            i = 0;
        }
    }

    public static class SortMapper extends Mapper<LongWritable, Text, ScoreInfo, Text> {

        ScoreInfo info = new ScoreInfo();
        Text k2 = new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            //zhongjinhui	韩国	仲瑾惠	77	女
            String line = value.toString();
            String[] data = line.split("\t");

            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);
            k2.set(info.getIdCard());

            context.write(info, k2);    //zhongjinhui	韩国	仲瑾惠	77	女   zhongjinhui
        }

    }

    public static class MyPartition extends Partitioner<ScoreInfo, Text> {
        public int getPartition(ScoreInfo key, Text value, int numPartitions) {
            String addr = key.getAddress();

            if (addr.equals("北京")) {
                return 0;
            }
            if (addr.equals("上海")) {
                return 1;
            }
            if (addr.equals("广州")) {
                return 2;
            }
            if (addr.equals("深圳")) {
                return 3;
            } else {
                return 4;
            }
        }
    }
}
