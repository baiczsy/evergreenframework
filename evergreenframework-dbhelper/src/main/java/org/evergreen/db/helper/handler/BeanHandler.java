package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.ResultSetHandler;
import org.evergreen.db.helper.RowProcessor;

public class BeanHandler<T> implements ResultSetHandler<T>{
	
	private Class<T> type;
	
	public BeanHandler(Class<T> type){
		this.type = type;
	}

	public T handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toBean(rs, type) : null;
	}

}
