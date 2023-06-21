package com.dao;


import com.beans.DiskFileInfo;
import org.apache.hadoop.fs.FileStatus;

public interface HdfsDao {
    boolean createUserRoot(String foldername);
    DiskFileInfo[] getRootFileList(String userName);

}