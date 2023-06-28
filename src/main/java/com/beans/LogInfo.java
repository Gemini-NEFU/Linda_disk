package com.beans;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class LogInfo implements WritableComparable<LogInfo>{
    String day;
    String ip;
    String uri;
    String flag;
    String time;

    public LogInfo(String day, String ip, String uri, String flag, String time, String userName) {
        this.day = day;
        this.ip = ip;
        this.uri = uri;
        this.flag = flag;
        this.time = time;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
    @Override
    public int compareTo(LogInfo o) {
        return this.userName.compareTo(o.userName);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.day);
        out.writeUTF(this.ip);
        out.writeUTF(this.uri);
        out.writeUTF(this.flag);
        out.writeUTF(this.time);
        out.writeUTF(this.userName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.day=in.readUTF();
        this.ip=in.readUTF();
        this.uri=in.readUTF();
        this.flag=in.readUTF();
        this.time=in.readUTF();
        this.userName=in.readUTF();
    }

    @Override
    public String toString() {
        return this.day+"\t" +this.ip+"\t"+this.uri+"\t"+this.flag+"\t"+this.time+"\t"+this.userName;
    }
}
