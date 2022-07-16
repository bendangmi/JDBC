package dao;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/15-19:30
 */
public interface CustomerDAO {

    // 将 cust 对象添加到数据库中
    void insert(Connection conn, Customer cust);

    // 针对指定的id，删除表中的一条记录
    void deleteById(Connection conn, int id);

    // 针对内存中的 cust 对象, 修改数据表中指定的记录
    void updateById(Connection conn, Customer cust);

    // 针对指定的id查询对应的Customer信息
    Customer getCustomerById(Connection conn, int id);

    // 查询所有记录构成的集合
    List<Customer> getAll(Connection conn);

    // 返回数据表中的数据的条目数
    Long getCount(Connection conn);

    // 返回数据表中最大的生日
    Date getMaxBirth(Connection conn);

}
