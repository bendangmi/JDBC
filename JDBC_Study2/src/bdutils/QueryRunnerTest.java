package bdutils;

import bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.util.List;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/16-10:27
 */
public class QueryRunnerTest {

    // 测试插入
    @Test
    public void testInsert()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers (name, email) values (?, ?)";
            runner.update(conn, sql, "小小迷", "111@123.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    // 测试查询
    // BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
    @Test
    public void testQuery1()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email from customers where id = ?";

            final BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            final Customer query = runner.query(conn, sql, handler, 23);
            System.out.println(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }


    }

    // 测试查询
    // BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
    @Test
    public void testQuery2()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email from customers where id < ?";

            final BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            final List<Customer> query = runner.query(conn, sql, handler, 23);
            query.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }


    }
}
