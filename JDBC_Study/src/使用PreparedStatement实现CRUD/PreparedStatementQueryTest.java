package 使用PreparedStatement实现CRUD;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @code Description 使用PreparedStatement实现针对不同表的通用的查询操作
 * @code author 本当迷
 * @code date 2022/7/15-6:22
 */
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList(){
        String sql = "select id, name, email from customers where id < ?";
        final List<Customer> list = getForList(Customer.class, sql, 12);
        list.forEach(System.out::println);

    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 获取数据连接
            conn = JDBCUtils.getConnection();

            // 预编译sql语句
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }

            // 获取结果集
            rs = ps.executeQuery();

            // 获取元数据
            final ResultSetMetaData metaData = rs.getMetaData();
            // 获取字段的数量
            final int columnCount = metaData.getColumnCount();

            final ArrayList<T> list = new ArrayList<>();
            while (rs.next()){
                // 获取对象的一个实例对象
                final T t = clazz.getConstructor().newInstance();
                for(int i = 0; i < columnCount; i++){
                    // 获取字段具体的值
                    final Object objectValue = rs.getObject(i + 1);

                    // 获取字段的别名
                    final String columnLabel = metaData.getColumnLabel(i + 1);

                    // 利用反射给对象赋值
                    final Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, objectValue);
                }
                list.add(t);

            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }

    }

    @Test
    public void testGetInstance(){
        String sql = "select id, name, email from customers where id = ?";
        final Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);

        sql = sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?;";
        final Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
    }


    // 针对于不同的表的通用的查询操作，返回表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1.获取数据量连接
            conn = JDBCUtils.getConnection();

            // 2.预编译sql语句，得到prepareStatement对象
            ps = conn.prepareStatement(sql);

            // 3.填充占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行executeQuery（），得到结果集
            rs = ps.executeQuery();

            // 获取结果集元数据：ResultSetMetaData
            final ResultSetMetaData rsmd = rs.getMetaData();

            // 通过ResultSetMetaData获取结果集中的列数
            final int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                final T t = clazz.getConstructor().newInstance();

                // 处理结果集一行数据中的每一列
                for(int i = 0; i < columnCount; i++){
                    // 获取列值
                    final Object columValue = rs.getObject(i + 1);

                    // 获取每个列的别名
                    final String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给cust对象指定columnName属性，赋值为columValue,通过反射
                    final Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }

                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;


    }
}
