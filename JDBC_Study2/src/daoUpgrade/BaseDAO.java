package daoUpgrade;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @code Description 封装了针对于数据表的增删改(dao基础班的升级版)
 * @code author 本当迷
 * @code date 2022/7/15-18:42
 */

public abstract class BaseDAO <T>{
    private Class<T> clazz = null;
    {
        final Type genericSuperclass = this.getClass().getGenericSuperclass();
        final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // 获取了父类的泛型
        final Type[] typeArguments = parameterizedType.getActualTypeArguments();
        // 泛型的第一个参数
        clazz = (Class<T>) typeArguments[0];
    }

    public <E> E getValue(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 预编译sql
            ps = conn.prepareStatement(sql);

            // 填充占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行
            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 资源关闭
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }


    // 通用的增删改操作
    public int getUpdate(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;

        try {

            // sql预编译
            ps = conn.prepareStatement(sql);

            // 填充sql占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行sql语句
            return ps.executeUpdate();


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(null, ps);
        }
    }


    // 通用的查询:用于返回数据表中的多条记录构成的集合
    public List<T> getForList(Connection conn, String sql, Object ...args) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

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
            JDBCUtils.closeResource(null, ps, rs);
        }

    }

    // 针对于不同的表的通用的查询操作，返回表中的一条记录
    public T getInstance(Connection conn, String sql, Object ...args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;


    }

}
