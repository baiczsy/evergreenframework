package org.evergreen.db.helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL执行器
 *
 * @author lenovo
 */
public class SQLExecutor {

    Connection connection;

    public SQLExecutor() {
    }

    public SQLExecutor(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection(){
        return connection;
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
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLException("Null SQL statement");
        }

        if (handler == null) {
            close();
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        T t = null;
        try {
            ps = connection.prepareStatement(sql);
            setParameters(ps, params);
            rs = ps.executeQuery();
            t = handler.handle(rs);
        } catch (SQLException e) {
            rethrow(e);
        } finally {
            close(rs);
            close(ps);
            close();
        }
        return t;
    }

    /**
     * 执行增删改操作
     *
     * @param sql
     * @param params
     * @return
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            setParameters(ps, params);
            i = ps.executeUpdate();
        } catch (SQLException e) {
            rethrow(e);
        } finally {
            close(ps);
            close();
        }
        return i;
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
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement ps = null;
        int[] rows = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameters(ps, params[i]);
                ps.addBatch();
            }
            rows = ps.executeBatch();
        } catch (SQLException e) {
            rethrow(e);
        } finally {
            close(ps);
            close();
        }
        return rows;
    }

    /**
     * 添加, 并返回一个generatedKey
     *
     * @param sql
     * @param params
     * @return
     */
    public Object insert(String sql, Object... params) throws SQLException {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLException("Null SQL statement");
        }

        if (!sql.trim().toUpperCase().startsWith("INSERT")) {
            close();
            throw new SQLException("Not an insert statement");
        }

        PreparedStatement ps = null;
        Object generatedKey = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(ps, params);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                generatedKey = rs.getObject(1);
            }
        } catch (SQLException e) {
            rethrow(e);
        } finally {
            close(ps);
            close();
        }
        return generatedKey;
    }

    /**
     * 批量添加, 并返回多个generatedKey
     *
     * @param sql
     * @param params
     * @return
     */
    public Object[] insertBatch(String sql, Object[][] params) throws SQLException {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLException("Null SQL statement");
        }

        if (!sql.trim().toUpperCase().startsWith("INSERT")) {
            close();
            throw new SQLException("Not an insert statement");
        }
        PreparedStatement ps = null;
        List generatedKeys = new ArrayList();
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                setParameters(ps, params[i]);
                ps.addBatch();
            }
            ps.executeBatch();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                generatedKeys.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rethrow(e);
        } finally {
            close(ps);
            close();
        }
        return generatedKeys.toArray();
    }

    /**
     * 设置SQL语句的参数
     *
     * @param ps
     * @param params
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, Object[] params)
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        try {
            if (connection != null && connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
            }
        }
    }

    /**
     * 事务回滚
     */
    public void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
            }
        }
    }

    /**
     * 关闭结果集
     *
     * @param rs
     */
    private void close(ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
            }
    }

    /**
     * 关闭Statement
     *
     * @param st
     */
    private void close(Statement st) {
        if (st != null)
            try {
                st.close();
            } catch (SQLException e) {
            }
    }

    /**
     * 关闭连接
     */
    private void close() {
        try {
            if (connection != null && connection.getAutoCommit()) {
                connection.close();
            }
        } catch (SQLException e) {
        }

    }

    /**
     * 异常重抛
     */
    private void rethrow(SQLException cause)
            throws SQLException {
        String msg = cause.getMessage() == null ? "" : cause.getMessage();
        SQLException e = new SQLException(msg, cause.getSQLState(),cause.getErrorCode());
        e.setNextException(cause);
        throw e;
    }
}
