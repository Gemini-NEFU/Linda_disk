package com.beans;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.fs.FileStatus;

public class DiskFileInfo {
    public DiskFileInfo(FileStatus f) {
        this.f=f;
    }
    //采取装饰设计模式,对FileStatus类进行装饰;
    private FileStatus f;

    private String modificationTime; //文件最后修改时间
    private String len; //文件大小
    private String icon; //文件图标
    private String path; //文件路径  去掉 hdfs://master:9000/ 这样的前缀,只留后面的文件名
    private String name; //文件路径  去掉 hdfs://master:9000/ 这样的前缀,只留后面的文件名

    public String getModificationTime() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd hh:mm");
        this.modificationTime=df.format(new Date(f.getModificationTime()));
        return this.modificationTime;
    }
    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }
    public String getLen() {
        if(f.isDirectory())
            return "-";

        /* 根据文件的字节数,计算出较友好的显示形式 Byte, K, M, G , T 等 */

        // 1024 * 1024 =1M =1048576
        // 1024 * 1024 *1024  =1G  =1073741824
        // 1024 * 1024 *1024  =1T  =1099511627776

        float size=f.getLen();

        if(size>=1099511627776L){
            this.len= String.format("%.2f", size/1024/1024/1024/1024)+" P";
        }
        else if(size>=1073741824){
            this.len= String.format("%.2f", size/1024/1024/1024)+" G";
        }

        else if(size>=1048576){
            this.len= String.format("%.2f", size/1024/1024)+" M";
        }

        else if(size>1024)   {
            this.len= String.format("%.2f", size/1024)+" K";
        }

        else{
            this.len= size+ " Byte";
        }

        return this.len;

    }
    public void setLen(String len) {
        this.len = len;
    }

    private static Map<String,String> iconMap;
    static {
        iconMap=new HashMap<String, String>();
        iconMap.put("folder", "folder.png");
        iconMap.put(".txt", "txtIcon.png");
        iconMap.put(".log", "txtIcon.png");
        iconMap.put(".gz", "zipIcon.png");
        iconMap.put(".rar", "zipIcon.png");
        iconMap.put(".jar", "zipIcon.png");
        iconMap.put(".zip", "zipIcon.png");
        iconMap.put(".7z", "zipIcon.png");
        iconMap.put(".exe", "exeIcon.png");
        iconMap.put(".avi", "aviIcon.png");
        iconMap.put(".wmv", "aviIcon.png");
        iconMap.put(".gif", "picIcon.png");
        iconMap.put(".bmp", "picIcon.png");
        iconMap.put(".jpg", "picIcon.png");
        iconMap.put(".jpeg", "picIcon.png");
        iconMap.put(".png", "picIcon.png");
        iconMap.put(".pdf", "pdfIcons.png");
        iconMap.put(".xls", "xlsIcon.png");
        iconMap.put(".xlsx", "xlsIcon.png");
        iconMap.put(".doc", "wordIcon.png");
        iconMap.put(".docx", "wordIcon.png");
        iconMap.put(".java", "sourceCodeIcon.png");
        iconMap.put(".data", "aviIcon.png");
        iconMap.put(".mp3", "soundIcon.png");
    }

    public String getIcon() {
        if(f.isDirectory()) {
            this.icon=iconMap.get("folder");
        }
        else {
            String fileName=f.getPath().getName();
            //如果文件有扩展名
            if(fileName.lastIndexOf(".")!=-1) {
                //fileExt 就是文件的扩展名,比如 .doc, .exe, .jpg 等等
                String fileExt=fileName.substring(fileName.lastIndexOf("."));
                this.icon=iconMap.get(fileExt);
            }

            if(this.icon==null) {
                this.icon="defaultIcon.png";
            }
        }

        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getPath() {
        return f.getPath()+"";
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return f.getPath().getName();
    }
    public void setName(String name) {
        this.name = name;
    }

}
