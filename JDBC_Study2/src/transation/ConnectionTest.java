package transation;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-13:58
 */
public class ConnectionTest {

    @Test
    public void testGetConnection() throws Exception {
        final Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
    }
}
