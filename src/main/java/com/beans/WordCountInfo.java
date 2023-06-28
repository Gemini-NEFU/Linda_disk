package com.beans;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WordCountInfo implements WritableComparable<WordCountInfo> {
    private int count;
    private String word;

    public WordCountInfo(){

    }
    public WordCountInfo(int count, String word) {
        this.count = count;
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    @Override
    public String toString() {
        return this.count+"\t" +this.word;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.count);
        out.writeUTF(this.word);
    }
    @Override
    //反序列化
    public void readFields(DataInput in) throws IOException {
        this.count=in.readInt();
        this.word=in.readUTF();
    }
    @Override
    public int compareTo(WordCountInfo o) {
        if(this.count==o.count) {
            return 0;
        }
        else if(this.count>o.count) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
