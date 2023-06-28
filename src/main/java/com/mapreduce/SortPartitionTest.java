package com.mapreduce;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.beans.ScoreInfo;

//分区,排序相关示例
public class SortPartitionTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        //建一个作业
        Job job=Job.getInstance();

        //指定main函数所在的类
        job.setJarByClass(SortPartitionTest.class);

        //设定map相关的信息
        job.setMapperClass(TotalMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ScoreInfo.class);

        //指定用户输入的文件的路径
        Path path=new Path("hdfs://hadoophyq:8020/admin/score.txt");
        FileInputFormat.setInputPaths(job, path);

        //设定reduce相关的信息
        job.setReducerClass(TotalReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(ScoreInfo.class);

        //指明计算结果存放的位置
        Path outputPath=new Path("hdfs://hadoophyq:8020/admin/sortresulttmp");
        FileOutputFormat.setOutputPath(job, outputPath);

        //如果原来做过计算,先把原来生成的目录删除
        Configuration conf=new Configuration();
        URI uri=new URI("hdfs://hadoophyq:8020/");
        FileSystem fs=	FileSystem.get(uri, conf, "hadoop");
        fs.delete(outputPath,true);

        //提交作业 true 表示打印提示信息
        job.waitForCompletion(true);

        Job job2=Job.getInstance();
        job2.setJarByClass(SortPartitionTest.class);

        //指定Map相关的配置
        job2.setMapperClass(SortMapper.class);
        job2.setMapOutputKeyClass(ScoreInfo.class);
        job2.setMapOutputValueClass(Text.class);

        //指定第二个map要读入的文件是哪个
        Path path3=new Path("hdfs://hadoophyq:8020/admin/sortresulttmp/part-r-00000");
        FileInputFormat.setInputPaths(job2, path3);

        job2.setOutputKeyClass(ScoreInfo.class);
        job2.setOutputValueClass(Text.class);

        //指定最终的结果存放地址
        Path path4=new Path("hdfs://hadoophyq:8020/admin/sortresult");
        FileOutputFormat.setOutputPath(job2, path4);

        //开启分区
        job2.setNumReduceTasks(5);
        job2.setPartitionerClass(MyPartition.class);

        fs.delete(path4,true);

        boolean result= job2.waitForCompletion(true);

        System.out.println(result?"统计成功":"统计失败");

    }

    public static class TotalMapper extends Mapper<LongWritable,Text,Text,ScoreInfo>{
        Text k2=new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line=value.toString();
            String [] data =line.split("\t");

            ScoreInfo info=new ScoreInfo();
            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);
            k2.set(info.getIdCard());
            context.write(k2, info);
        }
    }

    public static class TotalReduce extends Reducer<Text,ScoreInfo,Text,ScoreInfo >{
        int totalScore=0;
        int i=0;
        ScoreInfo info;
        protected void reduce(Text key, Iterable<ScoreInfo> values,
                              Context context) throws IOException, InterruptedException {
            for(ScoreInfo o:values) {
                //其为同一组的中的数据,除了分数外,其他的基本信息都是相同的,所以取第一个数据的信息就可以
                if(i==0) {
                    info=o;
                }
                totalScore+=o.getScore();
                i++;
            }

            info.setScore(totalScore);

            context.write(key, info);

            totalScore=0;
            i=0;
        }
    }
    public static class SortMapper extends Mapper<LongWritable,Text,ScoreInfo,Text>{

        ScoreInfo info=new ScoreInfo();
        Text k2=new Text();

        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            //zhongjinhui	韩国	仲瑾惠	77	女
            String line=value.toString();
            String [] data=line.split("\t");

            info.setIdCard(data[0]);
            info.setAddress(data[1]);
            info.setName(data[2]);
            info.setScore(Integer.parseInt(data[3]));
            info.setGender(data[4]);
            k2.set(info.getIdCard());

            context.write(info, k2);    //zhongjinhui	韩国	仲瑾惠	77	女   zhongjinhui
        }

    }

    public static class MyPartition extends Partitioner<ScoreInfo,Text> {
        public int getPartition(ScoreInfo key , Text value , int numPartitions) {
            String addr=key.getAddress();

            if(addr.equals("北京")) {
                return 0;
            }
            if(addr.equals("上海")) {
                return 1;
            }
            if(addr.equals("广州")){
                return 2;
            }
            if(addr.equals("深圳")) {
                return 3;
            }
            else {
                return 4;
            }
        }
    }
}