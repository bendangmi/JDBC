package junit;

import bean.Customer;
import daoUpgrade.CustomerDAOImpl;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-20:12
 */
class CustomerDAOImplTest {
    private CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    void test() throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final java.util.Date date = format.parse("2002-06-22");
        final Date date1 = new Date(date.getTime());
        System.out.println(date1);
    }

    @Test
    void insert() {

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final java.util.Date date = format.parse("2002-06-22");
            final Customer customer = new Customer(1, "本当迷" , "147@qq.com" , new Date(date.getTime()));

            dao.insert(conn, customer);
            System.out.println("添加成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn, 22);
            System.out.println("删除成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final Customer customer = new Customer(18, "小当迷" , "123@123.com" , new Date(4324453243L));
            dao.updateById(conn, customer);
            System.out.println("修改成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final Customer customer = dao.getCustomerById(conn, 18);
            System.out.println(customer);
            System.out.println("查询成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final List<Customer> list = dao.getAll(conn);
            list.forEach(System.out::println);
            System.out.println("查询成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final Long count = dao.getCount(conn);
            System.out.println(count);
            System.out.println("查询成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            final Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
            System.out.println("查询成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}