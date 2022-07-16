package 使用PreparedStatement实现CRUD;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @code Description 针对Order表的通用的查询操作
 * @code author 本当迷
 * @code date 2022/7/14-21:20
 */
public class OrderForQuery {
    /**
     *  针对于表的字段名于类的属性名不相同的情况：
     *  1.必须声明sql时，使用类的属性名来命名字段的别名
     *  2.使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName()获取列的别名
     *  说明：如果sql中没有给字段取别名，getColumnLabel()获取的就是列名
     * */
    @Test
    public void testOrderForQuery(){
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?;";
        final Order order = orderForQuery(sql, 2);
        System.out.println(order);
    }
    // 通用的针对于Order表的查询操作
    public Order orderForQuery(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行，获得结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            final ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            final int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Order order = new Order();
                for(int i = 0; i < columnCount; i++){
                    // 获取每个列的列值
                    final Object coumnValue = rs.getObject(i + 1);
                    // 获取每个列的列名：getColumnName() --不推荐使用
                    // 获取每个列的别名：getColumnLabel()
                    final String columnName = rsmd.getColumnLabel(i + 1);

                    // 通过反射指定名的属性赋值为指定的值
                    final Field field = Order.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(order, coumnValue);
                }
                return order;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id, order_name, order_date from `order` where order_id = ?;";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);

            rs = ps.executeQuery();
            if(rs.next()){
                final int id = rs.getInt(1);
                final String name = rs.getString(2);
                final Date date = rs.getDate(3);

                final Order order = new Order(id, name, date);
                System.out.println(order);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }




    }
}
