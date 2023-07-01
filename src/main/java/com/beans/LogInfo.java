package com.beans;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LogInfo implements WritableComparable<LogInfo>{
    private String day;
    private String ip;
    private String url;
    private String flag;
    private String createTime;
    private String userName;

    public LogInfo(){};
    public LogInfo(String day, String ip, String url, String flag, String createTime, String userName) {
        this.day = day;
        this.ip = ip;
        this.url = url;
        this.flag = flag;
        this.createTime = createTime;
        this.userName = userName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int compareTo(LogInfo o) {
        return this.userName.compareTo(o.userName);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.day);
        out.writeUTF(this.ip);
        out.writeUTF(this.url);
        out.writeUTF(this.flag);
        out.writeUTF(this.createTime);
        out.writeUTF(this.userName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.day=in.readUTF();
        this.ip=in.readUTF();
        this.url=in.readUTF();
        this.flag=in.readUTF();
        this.createTime =in.readUTF();
        this.userName=in.readUTF();
    }

    @Override
    public String toString() {
        return this.day+"\t" +this.ip+"\t"+this.url+"\t"+this.flag+"\t"+this.createTime +"\t"+this.userName;
    }
}
