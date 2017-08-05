package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.evergreen.db.helper.ResultSetHandler;
import org.evergreen.db.helper.RowProcessor;

public class MapHandler implements ResultSetHandler<Map<String, Object>> {

	public Map<String, Object> handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toMap(rs) : null;
	}

}
