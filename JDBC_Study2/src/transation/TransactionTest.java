package transation;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-16:42
 */

/*
* 1.什么叫数据库事务？
* 事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态
*       一组逻辑操作单元：一个或者多个DML操作
*
* 2.事务处理原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
*   当一个事务中执行多个操作时，要么所有事务都被提交（commit）,那么这些修改就被永久保存下来；
*   要么数据库管理系统将放弃所作的所有修改，整个事务回滚（rollback）到最初状态。
*
* 3。数据一旦提交，就不可回滚。
*
* 4.哪些操作会导致数据的自动提交？
*       DDL操作一旦执行，都会被提交。
*           set autocommit = false 对DDL操作无效
*       DML默认情况下，一旦执行，就会自动提交
*           我们可以通过set autocommit = false 的方式取消DML操作的自动提交
*       默认在关闭连接时，会自动的提交数据
* */
public class TransactionTest {

    // 未考虑数据库事务的转账操作
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        String sql2 = "update user_table set balance = balance + 100 where user = ?";

        update(sql1, "AA");
        update(sql2, "BB");
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

    //*****************************************************************************
    // 考虑数据库事务的操作
    @Test
    public void testUpdate1(){
        Connection conn = null;
        try {
            // 获取数据库连接
            conn = JDBCUtils.getConnection();
            // 关闭自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            String sql2 = "update user_table set balance = balance + 100 where user = ?";

            update1(conn, sql1, "AA");

            // 模拟网络异常
//            System.out.println( 10 / 0);

            update1(conn, sql2, "BB");
            // 若没有异常，提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // 恢复每次DML操作的自动提交功能
            // 主要针对于数据库连接池
            try {
                assert conn != null;
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, null);
        }
    }


    // 通用的增删改操作
    public int update1(Connection conn, String sql, Object ...args) {

        PreparedStatement ps = null;

        try {

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
            JDBCUtils.closeResource(null, ps);
        }
    }


}
