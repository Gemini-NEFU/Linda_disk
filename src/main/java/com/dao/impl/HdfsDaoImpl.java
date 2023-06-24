package com.dao.impl;
import java.net.URI;

import com.beans.DiskFileInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.dao.HdfsDao;
import org.apache.hadoop.io.IOUtils;

import javax.servlet.ServletOutputStream;

public class HdfsDaoImpl implements HdfsDao{
    static final String USER_NAME="hadoop";
//    static final String HDFS_PATH = "hdfs://mycluster/";
    static final String HDFS_PATH = "hdfs://hadoophyq:8020";
    static final Configuration conf;

//    static {
//        conf=new Configuration();
//        conf.set("dfs.nameservices","mycluster");
//        conf.set("dfs.ha.namenodes.mycluster","nn1,nn2,nn3");
//        conf.set("dfs.namenode.rpc-address.mycluster.nn1","master:8020");
//        conf.set("dfs.namenode.rpc-address.mycluster.nn2","slave1:8020");
//        conf.set("dfs.namenode.rpc-address.mycluster.nn3","slave2:8020");
//        conf.set("dfs.client.failover.proxy.provider.mycluster","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
//    }
    static {
        conf=new Configuration();
        conf.set("fs.hdfs.impl.disable.cache","true");
    }
    /**
     * 用于在hdfs创建目录
     */
    public boolean createUserRoot(String folderName) {
        try{
            FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf,USER_NAME);
            boolean result=fs.mkdirs(new Path("/"+folderName));
            fs.close();
            return result;
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public DiskFileInfo[] getRootFileList(String userName) {
        try {
            FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
            FileStatus[] fileList = fs.listStatus(new Path("/"+userName));
            DiskFileInfo[] diskFileList=new DiskFileInfo[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                FileStatus f=fileList[i];
                diskFileList [i]=new DiskFileInfo(f);
            }
            return diskFileList;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public DiskFileInfo[] getSubFileList(String parent) {
        try {
            FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
            FileStatus[] fileList = fs.listStatus(new Path("/"+parent));
            DiskFileInfo[] diskFileList=new DiskFileInfo[fileList.length];
            for(int i=0;i<fileList.length;i++) {
                FileStatus f=fileList[i];
                diskFileList [i]=new DiskFileInfo(f);
            }

            return diskFileList;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 下载文件
     * @param fileName 文件路径,格式如: admin/javatools/Test.txt
     * @param out servlet对应的输出流,用于下载
     */
    public void downLoadFileAsStream(String fileName, ServletOutputStream out) {
        try {
            FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf);
            Path path = new Path("/"+fileName);
            FSDataInputStream fsInput = fs.open(path);
            IOUtils.copyBytes(fsInput, out, 4096, false);
            out.flush();
            fsInput.close();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public static void main(String[] args) {
        HdfsDaoImpl dao=new HdfsDaoImpl();
        boolean result=dao.createUserRoot("yyyy");
        System.out.println(result);
    }
}
