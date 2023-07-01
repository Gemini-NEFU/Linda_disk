package com.jdbc;
import java.sql.*;

//连接hive用的JDBC工具类
public class HiveDBUtil {
    //构造方法私有化,防止别人new本类的实例
    private HiveDBUtil() {}

    private static String url = "jdbc:hive2://hadoophyq:10000/oa";

    //Hive 用户密码!!
    private static String userName = "hadoop";
    private static String password = "hadoop";

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 用来执行命令
     * @param sql  相关命令
     * @return
     */
    public static int update(String sql) {
        Connection conn=null;
        Statement stm=null;

        try {
            conn=getConn();
            stm=conn.createStatement();
            return stm.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            close(null,stm,conn);
        }
    }

    /**
     * 得到hive连接
     * @return 连接
     */
    public static Connection getConn() {
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 清理资源
     * @param conn
     * @param stm
     * @param rs
     */
    public static void close( ResultSet rs,Statement stm, Connection conn ) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
