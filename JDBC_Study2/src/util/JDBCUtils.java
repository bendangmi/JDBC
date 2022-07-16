package util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/14-18:14
 */
public class JDBCUtils {

    // 获取数据库的连接
    public static  Connection getConnection() throws Exception {

        final InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");
        final String url = properties.getProperty("url");
        final String driverClass = properties.getProperty("driverClass");

        // 2.加载驱动
        Class.forName(driverClass);
        // 3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;
    }

    // 关闭资源操作
    public static void closeResource(Connection conn, Statement ps){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭资源操作
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
