package com.dao.impl;

import com.beans.LogGroupInfo;
import com.beans.LogInfo;
import com.dao.HiveDao;
import com.jdbc.HiveDBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HiveDaoImpl implements HiveDao {

    @Override
    public List<LogInfo> getLogList(String userName) {
        String tableName=userName+"_log";

        //先删除旧的表格
        HiveDBUtil.update("drop table if exists " +tableName );

        //创建表格
        String sql=	"create external table if not exists "+tableName+" (day string,ip string, url string ,flag string ,createTime string,userName string ) row format delimited fields terminated by ',' location '/"+userName+"/log'  ";
        System.out.println(sql);
        HiveDBUtil.update(sql);

        //从表格中提取数据
        List<LogInfo> list=new ArrayList<>();

        Connection conn =null;
        Statement stm=null;
        ResultSet rs=null;

        try {
            conn=HiveDBUtil.getConn();
            stm=conn.createStatement();
            rs=stm.executeQuery("select * from "+tableName);

            while(rs.next()) {
                LogInfo info=new LogInfo();
                info.setDay(rs.getString("day"));
                info.setIp(rs.getString("ip") );
                info.setUrl(rs.getString("url") );
                info.setFlag( rs.getString("flag") );
                info.setCreateTime( rs.getString("createTime") );
                info.setUserName( rs.getString("userName") );

                list.add(info);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally{
            HiveDBUtil.close(rs, stm, conn);
        }

        return list;
    }

    @Override
    //分组查询
    public List<LogGroupInfo> getLogGroupList(String userName) {
        String tableName=userName+"_log";

        //先删除旧的表格
        HiveDBUtil.update("drop table if exists " +tableName );

        //创建表格
        String sql=	"create external table if not exists "+tableName+" (day string,ip string, url string ,flag string ,createTime string,userName string ) row format delimited fields terminated by ',' location '/"+userName+"/log'  ";
        System.out.println(sql);
        HiveDBUtil.update(sql);

        //从表格中提取数据
        List<LogGroupInfo> list=new ArrayList<>();

        Connection conn =null;
        Statement stm=null;
        ResultSet rs=null;

        try {
            conn=HiveDBUtil.getConn();
            stm=conn.createStatement();
            rs=stm.executeQuery("select ip,count(*) as c from "+tableName +" group by ip order by c desc limit 20");

            while(rs.next()) {
                LogGroupInfo info=new LogGroupInfo();
                info.setIp(rs.getString("ip"));
                info.setCount(rs.getInt("c"));

                list.add(info);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally{
            HiveDBUtil.close(rs, stm, conn);
        }

        return list;
    }
}
