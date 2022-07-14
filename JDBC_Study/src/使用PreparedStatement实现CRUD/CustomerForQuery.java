package 使用PreparedStatement实现CRUD;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @code Description 针对Customers表的查询操作
 * @code author 本当迷
 * @code date 2022/7/14-19:45
 */
public class CustomerForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select id, name, birth, email from customers where id = ?";
        final Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);

    }

    // 针对customers表的通用的查询操作
    public Customer queryForCustomers(String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);

            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();

            // 获取结果集的元数据：ResultSetMetaData
            final ResultSetMetaData rsmd = rs.getMetaData();

            // 通过ResultSetMetaData获取结果集中的列数
            final int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                final Customer customer = new Customer();
                for(int i = 0; i < columnCount; i++){
                    // 获取列值
                    final Object columValue = rs.getObject(i + 1);

                    // 获取每个列的别名
                    final String columnName = rsmd.getColumnName(i + 1);

                    // 给cust对象指定的columnName属性，赋值colmValue;通过反射
                    final Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columValue);

                }
                return customer;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;

    }

    @Test
    public void testQuery1() throws Exception {


        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?;";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 1);
            // 执行并返回结果集
            resultSet = ps.executeQuery();

            // 处理结果集:next():判断是否有数据，如果有数据返回true,并指向下一条数据，反之。
            if (resultSet.next()) {
                // 获取当前这条数据的各个字段值
                final int id = resultSet.getInt(1);
                final String name = resultSet.getString(2);
                final String email = resultSet.getString(3);
                final Date birth = resultSet.getDate(4);

                // 方式一：
                // System.out.println("id = " + id + "name = " + name + "email = " + email + "birth = " + birth);

                // 方式二：将数据封装为一个对象（推荐）
                final Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, ps, resultSet);
        }
    }
}
