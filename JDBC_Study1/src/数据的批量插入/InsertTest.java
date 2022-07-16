package 数据的批量插入;

import org.testng.annotations.Test;
import 使用PreparedStatement实现CRUD.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-12:53
 */
public class InsertTest {
    // 方式一：
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            final long start = System.currentTimeMillis();
            // 数据库连接
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods (name) Values (?)";

            // 预编译sql
            ps = conn.prepareStatement(sql);

            // 填充占位符
            for(int i = 0; i < 20000; i++){
                ps.setObject(1, "name" + i);

                // 执行
                ps.execute();
            }
            final long end = System.currentTimeMillis();
            System.out.println("花费时间：" + (end - start));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }

    }

    // 方式二
    /*
     * 修改1： 使用 addBatch() / executeBatch() / clearBatch()
     * 修改2：mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     * 修改3：使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     *
     */
    @Test
    public void testInsert1() throws Exception{
        long start = System.currentTimeMillis();

        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(int i = 1;i <= 1000000;i++){
            ps.setString(1, "name_" + i);

            //1.“攒”sql
            ps.addBatch();
            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));//20000条：625                                                                         //1000000条:14733

        JDBCUtils.closeResource(conn, ps);
    }

    /*
     * 层次三：在层次二的基础上操作
     * 使用Connection 的 setAutoCommit(false)  /  commit()
     */
    @Test
    public void testInsert2() throws Exception{
        long start = System.currentTimeMillis();

        Connection conn = JDBCUtils.getConnection();

        //1.设置为不自动提交数据
        conn.setAutoCommit(false);

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(int i = 1;i <= 1000000;i++){
            ps.setString(1, "name_" + i);

            //1.“攒”sql
            ps.addBatch();

            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }

        //2.提交数据
        conn.commit();

        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));//1000000条:4978

        JDBCUtils.closeResource(conn, ps);
    }


}
