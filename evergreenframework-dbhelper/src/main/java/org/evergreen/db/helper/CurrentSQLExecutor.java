package org.evergreen.db.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangl on 2016/8/6.
 */
public class CurrentSQLExecutor extends SQLExecutor{

    private static ThreadLocal<Connection> local = new ThreadLocal<Connection>();

    public void setConnection(Connection connection){
        if(local.get() == null){
            local.set(connection);
        }
    }

    public Connection getConnection(){
        return local.get();
    }

    /**
     * 执行查询
     *
     * @param sql
     * @param handler
     * @param params
     * @return
     */
    public <T> T executeQuery(String sql, ResultSetHandler<T> handler,
                              Object... params) throws SQLException {
        connection = getConnection();
        if(isAutoCommit())
            local.remove();
        return super.executeQuery(sql, handler, params);
    }

    /**
     * 执行增删改操作
     *
     * @param sql
     * @param params
     * @return
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        connection = getConnection();
        if(isAutoCommit())
            local.remove();
        return super.executeUpdate(sql, params);
    }

    /**
     * 批量操作
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int[] executeBatch(String sql, Object[][] params)
            throws SQLException {
        connection = getConnection();
        if(isAutoCommit())
            local.remove();
        return super.executeBatch(sql, params);
    }

    /**
     * 执行增删改操作, 并返回一个generatedKey
     *
     * @param sql
     * @param params
     * @return
     */
    public Object insert(String sql, Object... params) throws SQLException {
        connection = getConnection();
        if(isAutoCommit())
            local.remove();
        return super.insert(sql, params);
    }

    /**
     * 执行增删改操作, 并返回多个generatedKey
     *
     * @param sql
     * @param params
     * @return
     */
    public Object[] insertBatch(String sql, Object[][] params) throws SQLException {
        connection = getConnection();
        if(isAutoCommit())
            local.remove();
        return super.insertBatch(sql, params);
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        connection = getConnection();
        super.beginTransaction();
    }

    /**
     * 提交事务
     */
    public void commit() {
        this.connection = getConnection();
        super.commit();
        local.remove();
    }

    /**
     * 事务回滚
     */
    public void rollback() {
        this.connection = getConnection();
        super.rollback();
        local.remove();
    }

    /**
     * 是否自动提交事务
     * @return boolean
     */
    private Boolean isAutoCommit(){
        try {
            if (connection != null && connection.getAutoCommit()) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }
}
