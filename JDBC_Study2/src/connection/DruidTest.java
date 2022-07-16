package connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @code Description 数据库连接池
 * @code author 本当迷
 * @code date 2022/7/16-9:36
 */
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
        final Properties properties = new Properties();

        final InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        properties.load(is);

        final DataSource source = DruidDataSourceFactory.createDataSource(properties);

        final Connection conn = source.getConnection();
        System.out.println(conn);

    }
}
