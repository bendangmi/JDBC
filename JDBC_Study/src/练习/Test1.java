package 练习;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-8:00
 */
public class Test1 {
    @Test
    public void testInsert(){
        String sql = "insert user_table (user, password) values (?, ?)";
        String user = "小当迷";
        String password = "12345678";
        final int insertCount = update(sql, user, password);
        if(insertCount > 0) System.out.println("插入成功！");
        else System.out.println("插入失败！");

    }

    @Test void testUpdate(){
        String sql = "update user_table set password = ? where user = ?";
        String user = "小当迷";
        String password = "Abc123";
        final int updateCount = update(sql, password, user);
        if(updateCount > 0) System.out.println("更新成功！");
        else System.out.println("更新失败！");
    }

    @Test void testDelete(){
        String sql = "delete from user_table where user = ?";
        String user = "小当迷";
        final int deleteCount = update(sql, user);
        if(deleteCount > 0) System.out.println("删除成功！");
        else System.out.println("删除失败");
    }

    // 通用的增删改操作
    public int update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 获取数据库连接
            conn = JDBCUtils.getConnection();

            // sql预编译
            ps = conn.prepareStatement(sql);

            // 填充sql占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行sql语句
            final int update = ps.executeUpdate();
            return update;


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
