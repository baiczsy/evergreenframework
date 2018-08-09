package org.evergreen.db.helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL执行器
 *
 * @author lenovo
 */
public class SQLExecutor {

    private Connection connection;

    private boolean autoClose = true;

    public SQLExecutor(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
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
                              Object... params) {
        if (connection == null) {
            throw new ExecuteParamsException("Null connection");
        }

        if (sql == null) {
            close();
            throw new ExecuteParamsException("Null SQL statement");
        }

        if (handler == null) {
            close();
            throw new ExecuteParamsException("Null ResultSetHandler");
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
            throw new DataAccessException(e.getMessage(), e);
        } finally {
            close(rs);
            close(ps);
            if (autoClose) {
                close();
            }
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
    public void executeUpdate(String sql, Object... params) {
        if (connection == null) {
            throw new ExecuteParamsException("Null connection");
        }

        if (sql == null) {
            close();
            throw new ExecuteParamsException("Null SQL statement");
        }

        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            setParameters(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } finally {
            close(ps);
            if (autoClose) {
                close();
            }
        }
    }

    /**
     * 批量操作
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public void executeBatch(String sql, Object[][] params) {
        if (connection == null) {
            throw new ExecuteParamsException("Null connection");
        }

        if (sql == null) {
            close();
            throw new ExecuteParamsException("Null SQL statement");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameters(ps, params[i]);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } finally {
            close(ps);
            if (autoClose) {
                close();
            }
        }
    }

    /**
     * 添加, 并返回一个generatedKey
     *
     * @param sql
     * @param params
     * @return
     */
    public Object insert(String sql, Object... params) {
        if (connection == null) {
            throw new ExecuteParamsException("Null connection");
        }

        if (sql == null) {
            close();
            throw new ExecuteParamsException("Null SQL statement");
        }

        if (!sql.trim().toLowerCase().startsWith("insert")) {
            close();
            throw new ExecuteParamsException("Not an insert statement");
        }

        PreparedStatement ps = null;
        Object generatedKey = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(ps, params);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getObject(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } finally {
            close(ps);
            if (autoClose) {
                close();
            }
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
    public Object[] insertBatch(String sql, Object[][] params) {
        if (connection == null) {
            throw new ExecuteParamsException("Null connection");
        }

        if (sql == null) {
            close();
            throw new ExecuteParamsException("Null SQL statement");
        }

        if (!sql.trim().toLowerCase().startsWith("insert")) {
            close();
            throw new ExecuteParamsException("Not an insert statement");
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
            while (rs.next()) {
                generatedKeys.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } finally {
            close(ps);
            if (autoClose) {
                close();
            }
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
            connection.setAutoCommit(false);
            autoClose = false;
        } catch (SQLException e) {
            throw new TransactionException("Begin transaction fail.", e);
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException("Commit transaction fail.",e);
        } finally {
            close();
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException("Rollback transaction fail.", e);
        } finally {
            close();
        }
    }

    /**
     * 关闭结果集
     *
     * @param rs
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new CloseResourcesException("Close resultset fail.", e);
            }
        }
    }

    /**
     * 关闭Statement
     *
     * @param st
     */
    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new CloseResourcesException("Close statement fail.", e);
            }
        }
    }

    /**
     * 关闭连接
     */
    private void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new CloseResourcesException("Close connection fail.", e);
        }
    }

}
