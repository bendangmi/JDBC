package 练习;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-9:12
 */




public class Test3 {


    public static void main(String[] args) {
        // 问题一：插入一个新的student信息
        final Examstudent examstudent = new Examstudent();
//        examstudent.insertSql();
        examstudent.selectSql();

    }


    @Test
    public void selectTest() {
        String sql = "select id, name, email from customers where id < ?";
        final List<Customer> list = Utilss.selectUtils(Customer.class, sql, 12);
        list.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        String sql = "insert user_table (user, password) values (?, ?)";
        String user = "小当迷2";
        String password = "12345678";
        final int insertCount = Utilss.updateUtils(sql, user, password);
        if(insertCount > 0) System.out.println("插入成功！");
        else System.out.println("插入失败！");

    }



}
class Examstudent{
    private int type;
    private String idCard;
    private String examCard;
    private String studentName;
    private String location;
    private String grade;

    public Examstudent() {
    }

    public void insertSql(){

        String sql = "insert into examstudent (Type ,IDCard ,ExamCard ,StudentName ,Location ,Grade) values (?, ?, ?, ?, ?, ?)";
        final Scanner scn = new Scanner(System.in);
        System.out.println("请输入信息：");
        type = scn.nextInt();
        idCard = scn.next();
        examCard = scn.next();
        studentName = scn.next();
        location = scn.next();
        grade = scn.next();
        Utilss.updateUtils(sql,type, idCard, examCard, studentName, location, grade);


    }

    public void selectSql(){
        String sql = "select IDCard idCard,ExamCard examCard,StudentName studentName from examstudent";
        final List<Examstudent> list = Utilss.selectUtils(Examstudent.class, sql);
        list.forEach(System.out::println);
    }

    public Examstudent(int type, String idCard, String examCard, String studentName, String location, String grade) {
        this.type = type;
        this.idCard = idCard;
        this.examCard = examCard;
        this.studentName = studentName;
        this.location = location;
        this.grade = grade;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "examstudent{" +
                "type=" + type +
                ", idCard='" + idCard + '\'' +
                ", examCard='" + examCard + '\'' +
                ", studentName='" + studentName + '\'' +
                ", location='" + location + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Examstudent that = (Examstudent) o;
        return type == that.type && idCard.equals(that.idCard) && examCard.equals(that.examCard) && studentName.equals(that.studentName) && location.equals(that.location) && grade.equals(that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, idCard, examCard, studentName, location, grade);
    }
}

class Utilss{


    // 通用的增删改操作
    static public int updateUtils(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 数据库连接操作
            connection = JdbcUtils();

            // 预编译sql语句
            ps = connection.prepareStatement(sql);

            // 填充sql
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行sql
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            closeResource(connection, ps);
        }
    }

    // 通用的查询操作
    static public <T> List<T> selectUtils(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        final ArrayList<T> list;

        try {
            // 获取数据库连接
            conn = JdbcUtils();

            // 预编译sql
            ps = conn.prepareStatement(sql);

            // 执行sql语句
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }

            // 执行
            resultSet = ps.executeQuery();

            // 拿到元数据
            final ResultSetMetaData metaData = resultSet.getMetaData();

            // 通过元数据我们拿到字段的长度
            final int columnCount = metaData.getColumnCount();

            // 我们声明一个数组用于返回查询到的记录
            list = new ArrayList<>();

            while(resultSet.next()){
                // 实例化一个T类型对象
                final T t = clazz.getConstructor().newInstance();
                for(int i = 0; i < columnCount; i++){
                    // 获取字段数据
                    final Object objectValue = resultSet.getObject(i + 1);
                    // 获取字段别名
                    final String columnLabel = metaData.getColumnLabel(i + 1);
                    // 通过反射给对象赋值
                    final Field field = clazz.getDeclaredField(columnLabel);
                    // 设置私有属性可以被外界更改
                    field.setAccessible(true);
                    field.set(t, objectValue);
                }
                list.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            closeResource(conn, ps, resultSet);
        }

        // 以数组的形式返回查询到的记录
        return list;

    }

    // JdbcUtils 工具包
    static public Connection JdbcUtils() throws Exception {
        final Connection connection;

        // 获取配置文件流
        final InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        // 实例化一个配置文件
        final Properties properties = new Properties();
        properties.load(stream);

        // 获取配置文件信息
        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");
        final String url = properties.getProperty("url");
        final String driverClass = properties.getProperty("driverClass");

        // 加载驱动
        Class.forName(driverClass);

        // 获取连接
        connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

        return connection;
    }

    // 资源关闭操作

    // 关闭资源操作
    static void closeResource(Connection conn, Statement ps) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭资源操作
    static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}