package 使用PreparedStatement实现CRUD;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

/**
 * @code Description 使用PreparedStatement，实现对数据表的CRUD操作
 * @code author 本当迷
 * @code date 2022/7/14-16:27
 */
public class Main {
    // 往数据库添加记录
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 1.获取数据库连接
            connection = JDBCUtils.getConnection();

            // 4.预编译sql语句，返回PreparedStatement实例
            String sql = "insert into customers(name, email, birth)values(?, ?,?);";
            ps = connection.prepareStatement(sql);

            // 5.填充占位符
            ps.setString(1, "本当迷");
            ps.setString(2, "147@qq.com");
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = simpleDateFormat.parse("2002-06-22");

            ps.setDate(3, new java.sql.Date(date.getTime()));

            // 6.执行操作
            ps.execute();

            System.out.println("写入成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

    // 修改数据库记录
    @Test
    public void testUpdate(){

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            // 1.获取数据库连接
            connection = JDBCUtils.getConnection();

            // 2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ?;";
            ps = connection.prepareStatement(sql);

            // 3.填充占位符
            ps.setObject(1, "本当迷");
            ps.setObject(2, 18);

            // 4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.资源关闭
            JDBCUtils.closeResource(connection, ps);
        }
    }

    // 删除数据库一条记录
    @Test
    public void testDelete(){
        Connection con = null;
        PreparedStatement ps = null;
        try {
            // 1.数据库连接
            con = JDBCUtils.getConnection();

            // 2.预编译sql语句，返回PreparedStatement的实例
            String sql = "delete from customers where id = ?";
            ps = con.prepareStatement(sql);

            // 3.填充占位符
            ps.setInt(1, 19);

            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 5.资源关闭
            JDBCUtils.closeResource(con, ps);
        }

    }

    // 通用的增删改操作
    public void update(String sql, Object ...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.获取数据库连接
             conn = JDBCUtils.getConnection();

            // 2.预编译sql语句，返回PreparedStatement的实例
             ps = conn.prepareStatement(sql);

            // 3.填充占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }

            // 4.执行
            ps.execute();

            // 5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testCommonUpdate(){
        // 对数据库数据的删除操作
//        String sql = "delete from customers where id = ?";
//        update(sql, 3);

        // 对数据库数据的插入操作
//        String sql = "insert into user_table(user, password) values (?, ?);";
//        update(sql, "本当迷", "Abc12345678");

        // 对数据库的数据更改操作
        String sql = "update user_table set password = ? where user = ?";
        update(sql, "Abc123", "本当迷");
    }


}
