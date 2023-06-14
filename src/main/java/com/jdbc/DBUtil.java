package com.jdbc;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 一个数据库底层工具类,提供了得到数据库连接,和关闭数据库连的方法
 * 提供成能更新的方法
 * 提供了万能能查询
 *   可以查一个对象
 *   可以查 一组对象
 *   可以查一个字段
 *   可以把查询一条结果封装成一个map集合
 *   可以把多条查询结果封装成一个List集合,每个元素做为一个map
 */

public class DBUtil {
    private static DataSource dataSource = null;

    // 把构造函数私有化,可以防止别人new本类的实例
    private DBUtil() {
    }

    // 创建数据源
    static {
        dataSource = new ComboPooledDataSource("mysql");  //mysql是在配置文件中配置的
    }

    // 从C3P0数据源获取Connection对象(数据库连接对象)
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection(); // 返回的是经过代理的对象,其close方法已经被重写
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return conn;
    }

    // 关闭资源
    public static void close(ResultSet rs, Statement stm, Connection conn) {
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
                conn.close(); // 关连接的方法和过去一样,由于数据源返回的Connection对象是代理对象,所以close方法被处理
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭资源
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 万能更新(可以进行添加,更新,删除三种操作)
    public static int update(String sql, Object... params) {
        int result = 0;
        QueryRunner qr = new QueryRunner(); // 是一个线程不安全的类
        Connection conn=getConn();
        try {
            result = qr.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            close(conn);
        }

        return result;
    }

    // 添加数据,并将生成的自增ID返回
    public static int addWithId(String sql, Object... params) {
        int autoId = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stm = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }

            // 执行添加操作
            stm.executeUpdate();

            // 取出生成的自增ID
            ResultSet rsKey = stm.getGeneratedKeys();
            rsKey.next();
            autoId = rsKey.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(rs, stm, conn);
        }
        return autoId;
    }

    // 查询出一个单个的对象
    public static <T> T getSingleObj(String sql, Class<T> clazz,
                                     Object... params) {
        QueryRunner qr = new QueryRunner();
        T result = null;
        Connection conn=getConn();
        try {
            result = qr.query(conn, sql, new BeanHandler<T>(clazz),
                    params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            close(conn);
        }

        return result;
    }


    // 查询出对象列表(以ArrayList的方式返回),注意,如果没有查询到数据,该方法返回一个空列表,而不是null
    public static <T> List<T> getList(String sql, Class<T> clazz,
                                      Object... params) {
        List<T> list = new ArrayList<T>();
        QueryRunner qr = new QueryRunner();
        Connection conn =getConn();

        try {
            list = qr.query(conn, sql, new BeanListHandler<T>(clazz), params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally{
            close(conn);
        }

        return list;
    }

    public static <T>List<T> getColumnList(String sql, Class<T> clazz,	Object... params) {
        List<T> list = new ArrayList<T>();
        QueryRunner qr = new QueryRunner();
        Connection conn =getConn();

        try {
            list = qr.query(conn, sql, new ColumnListHandler<T>(), params);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally{
            close(conn);
        }

        return list;
    }

    // 返回Map集合(该方法只将一条数据返回为Map集合,key为字段名称,value为字段值)
    public static Map<String, Object> getMap(String sql, Object... params) {
        Map<String, Object> m = null;
        QueryRunner qr = new QueryRunner();
        Connection conn =getConn();

        try {
            m = qr.query(conn, sql, new MapHandler(), params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            close(conn);
        }

        return m;
    }

    // 返回一个List集合,其中每条数据都被封装成了一个Map集合,
    public static List<Map<String, Object>> getMapList(String sql,
                                                       Object... params) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        QueryRunner qr = new QueryRunner();
        Connection conn =getConn();

        try {
            mapList = qr.query(conn, sql, new MapListHandler(),
                    params);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            close(conn);
        }

        return mapList;
    }

    // 返回单行单个数据,该方法可以用来查询记录数(这时请使用Long型进行接收),单个字段值等数据
    public static <T> T getScalar(String sql, Object... obj) {
        T result = null;
        QueryRunner qr = new QueryRunner();
        Connection conn = getConn();
        try {
            result = qr.query(conn, sql, new ScalarHandler<T>(1), obj);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            close(conn);
        }

        return result;
    }
}