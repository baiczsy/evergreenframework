package org.evergreen.db.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据行转换处理
 * @author lenovo
 *
 */
public class RowProcessor {

	/**
	 * 将结果集转换成一个值
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static <T> T toColumnValue(ResultSet rs, int columnIndex, Class<T> type)
			throws SQLException {
		Object value = rs.getObject(columnIndex);
		if(value != null){
			String columnName = rs.getMetaData().getColumnLabel(columnIndex);
			value = ColumnUtil.columnConvert(rs, columnName, type);
		}
		return (T) value;
	}

	/**
	 * 将结果集转换成Bean对象
	 * 
	 * @param rs
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
		return (T) BeanUtil.createBean(rs, type);
	}

	/**
	 * 将结果集转换成数组
	 * 
	 * @param rs
	 */
	public static Object[] toArray(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		Object[] result = new Object[meta.getColumnCount()];
		for (int i = 0; i < result.length; i++)
			result[i] = rs.getObject(i + 1);
		return result;
	}

	/**
	 * 将结果集转换成Map
	 * 
	 * @param rs
	 */
	public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); i++)
			result.put(metaData.getColumnLabel(i), rs.getObject(i));
		return result;
	}
}
