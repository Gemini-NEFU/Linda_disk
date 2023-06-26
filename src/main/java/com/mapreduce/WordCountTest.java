package com.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

public class WordCountTest {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);


        job.setJarByClass(WordCountTest.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        String path="hdfs://hadoophyq:8020/admin/mapreduce/book.txt";
        FileInputFormat.setInputPaths(job,new Path(path));
        String resultpath="hdfs://hadoophyq:8020/admin/book_result";
        FileOutputFormat.setOutputPath(job,new Path(resultpath));

        if(job.waitForCompletion(true))
            System.out.println("OVER!");
    }
    static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
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
    static class MyReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
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
