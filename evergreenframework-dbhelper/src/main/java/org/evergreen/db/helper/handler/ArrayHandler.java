package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.ResultSetHandler;
import org.evergreen.db.helper.RowProcessor;

public class ArrayHandler implements ResultSetHandler<Object[]>{

	
	public Object[] handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toArray(rs) : null;
	}

}
