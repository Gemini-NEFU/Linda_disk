package com.test;
import com.jdbc.HiveDBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test3 {

    public static void main(String[] args) throws Exception {
        test3();
    }
    static void test2() throws Exception {
        //加载驱动类
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        System.out.println("加载驱动类成功");

        //创建和hive的连接
//        String hiveUrl = "jdbc:hive2://hadoophyq:10000/default";
        String hiveUrl = "jdbc:hive2://hadoophyq:10000/oa";
//        Connection conn = DriverManager.getConnection(hiveUrl, "root", "root");
        Connection conn = DriverManager.getConnection(hiveUrl, "hadoop","hadoop");
        System.out.println(conn + " 连接成功");

        //创建数据传输对象
        Statement stm = conn.createStatement();

        //执行查询
        String sql = "select count(*) as c, countryname   from people  group by countryname";
        ResultSet rs = stm.executeQuery(sql);

        //从结果集中取出结果
        while (rs.next()) {
            System.out.print(rs.getObject("c"));
            System.out.println(rs.getString("countryname") + "  ");
        }

        rs.close();
        stm.close();
        conn.close();
    }

    static void test1() throws Exception {
        Connection conn =HiveDBUtil.getConn();
        //创建数据传输对象
        Statement stm = conn.createStatement();

        //执行查询
        String sql = "select * from people ";
        ResultSet rs = stm.executeQuery(sql);

        //从结果集中取出结果
        while (rs.next()) {
            System.out.print(rs.getString("id") + "  ");
            System.out.print(rs.getString("peoplename") + "  ");
            System.out.print(rs.getInt("age") + " ");
            System.out.println(rs.getString("countryname"));
        }

        HiveDBUtil.close(rs,stm,conn);
    }
    static void test3() {
        HiveDBUtil.update("create table cat( id int, catName String)");
        System.out.println("ok");
    }

}