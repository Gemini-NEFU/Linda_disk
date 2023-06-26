package com.util;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    public static void log(String s)  {
        //后面的 true 表示以追加的方式写入
        BufferedWriter bw=null;
        try {
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            String fileName=df.format(new Date())+".log";  //2023-6-25
            FileWriter fw=new FileWriter("D:/logs/"+fileName,true);

            //包装成缓冲流
            bw=new BufferedWriter(fw);

            //写出日志到文件中
            bw.write(s);
            //写一个换行符
            bw.newLine();
            bw.flush();

        }catch(IOException ex) {
            ex.printStackTrace();
        }finally {
            if(bw!=null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}