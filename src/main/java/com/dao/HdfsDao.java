package com.dao;


import com.beans.DiskFileInfo;
import org.apache.hadoop.fs.FileStatus;

import javax.servlet.ServletOutputStream;
import java.util.List;

public interface HdfsDao {
    boolean createUserRoot(String foldername);
    DiskFileInfo[] getRootFileList(String userName);
    DiskFileInfo[] getSubFileList(String parent);
    void downLoadFileAsStream(String fileName, ServletOutputStream outputStream);
    void uploadFile(String parent, String localPath);
    long getUserDiskSize(String userRoot);

    boolean deleteFile(String fileName);

    boolean createFolder(String parent, String folderName);

    List<DiskFileInfo> getFileListByName(String userName, String fileName);

    List<DiskFileInfo> getFileListByType(String userName,String fileName);
}