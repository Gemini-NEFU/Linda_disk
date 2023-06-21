package com.dao.impl;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.dao.HdfsDao;
public class HdfsDaoImpl implements HdfsDao{
    static final String USER_NAME="hadoop";
//    static final String HDFS_PATH = "hdfs://mycluster/";
    static final String HDFS_PATH = "hdfs://hadoopxty:8020";
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
    }
    /**
     * 用于在hdfs创建目录
     */
    public boolean createUserRoot(String folderName) {
        try{
            FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf,USER_NAME);
            boolean result=fs.mkdirs(new Path("/hyq_disk/"+folderName));
            fs.close();
            return result;
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        HdfsDaoImpl dao=new HdfsDaoImpl();
        boolean result=dao.createUserRoot("yyyy");
        System.out.println(result);
    }
}
