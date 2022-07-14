package 数据库连接方式;


import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/14-12:47
 */
public class Main {

    // 方法一
    @Test
    public void testConnection1() throws SQLException {
        // 获取Driver 实现类对象
        final com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();


        System.out.println("本当迷学jdbc");

        // jdbc:mysql协议
        // localhost:ip协议
        // 3306:默认mysql端口号
        // jdbc:数据库
        String url = "jdbc:mysql://localhost:3306/jdbc";

        // 将用户名和密码封装在 Properties 中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "Abc12345678");

        final Connection connect = driver.connect(url, info);
        System.out.println(connect);

    }

    // 方式二：对方式一的迭代:如下程序中不出现第三方api，使得程序具有更好的可移植性
    @Test
    public void testConnection2() throws Exception {
        // 1.获取Driver实现类的对象，利用反射
        final Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        final Object instance = aClass.getConstructor().newInstance();
        final Driver driver = (Driver)instance;

        // 2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/jdbc";

        // 3.将用户名和密码封装在 Properties 中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "Abc12345678");

        // 4.获取连接
        final Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    // 方式三：
    @Test
    public void testConnection3() throws Exception {
        // 1.获取Driver实现类的对象
        final Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        final Driver driver = (Driver) clazz.getConstructor().newInstance();

        // 2.提供另外三个连接的基本信息：
        String url = "jdbc:mysql://localhost:3306/mysql";
        String user = "root";
        String password = "Abc12345678";

        // 注册驱动
        DriverManager.registerDriver(driver);

        // 获取连接
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 方式四：
    @Test
    public void testConnection4() throws ClassNotFoundException, SQLException {
        // 1.提供三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc";
        String user = "root";
        String password = "Abc12345678";

        // 2.加载Driver
        Class.forName("com.mysql.jdbc.Driver");
        // 向较与方式三：可以省略如下操作：
        /*
        * final Driver driver = (Driver) clazz.getConstructor().newInstance();
        * 注册驱动
        * DriverManager.registerDriver(driver);
        *
        * 为啥可以省略上面操作讷？
        * 原因：在mysql的Driver实现类中，帮我们实现了上面操作
        *
        * */
        // 3.获取连接
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    // 方法五(final版)：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    // 1.实现数据与代码的分离。实现了解耦
    // 2.如果需要修改配置文件，可以避免程序重新打包
    @Test
    public void getConnection5() throws IOException, ClassNotFoundException, SQLException {
        // 1.读取配置文件中的4个基本信息
        final InputStream is = Main.class.getClassLoader().getResourceAsStream("jdbc.properties");

        final Properties properties = new Properties();
        properties.load(is);

        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");
        final String url = properties.getProperty("url");
        final String driverClass = properties.getProperty("driverClass");

        // 2.加载驱动
        Class.forName(driverClass);

        // 3.获取连接
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }


}
