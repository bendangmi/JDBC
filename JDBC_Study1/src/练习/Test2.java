package 练习;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.Customer;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-8:18
 */

public class Test2 {
    @Test
    public void selectTest() {
        String sql = "select id, name, email from customers where id < ?";
        final List<Customer> list = selectList(Customer.class, sql, 12);
        list.forEach(System.out::println);
    }

    public <T> List<T> selectList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final ArrayList<T> list;

        try {
            // 获取数据库连接
            conn = JDBCUtils.getConnection();

            // 预编译sql
            ps = conn.prepareStatement(sql);

            // 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            rs = ps.executeQuery();

            // 获取元数据
            final ResultSetMetaData metaData = rs.getMetaData();

            // 获取字段的数目
            final int columnCount = metaData.getColumnCount();

            // 创建一个对象数组
            list = new ArrayList<>();
            while (rs.next()) {
                // 实例化clazz对象
                final T t = clazz.getConstructor().newInstance();

                // 遍历数据库字段
                for (int i = 0; i < columnCount; i++) {
                    // 获取字段值
                    final Object objectValue = rs.getObject(i + 1);
                    // 获取字段别名
                    final String columnLabel = metaData.getColumnLabel(i + 1);
                    // 利用反射给实例化对象赋值
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
            // 资源关闭
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
