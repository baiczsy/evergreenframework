package org.evergreen.db.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集转换,将查询的结果封装成不同的对象类型
 * @author lenovo
 *
 * @param <T>
 */
public interface ResultSetHandler<T> {

	T handle(ResultSet rs) throws SQLException;
}
