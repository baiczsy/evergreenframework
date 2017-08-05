package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.evergreen.db.helper.RowProcessor;

public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

	@Override
	protected Map<String, Object> getRow(ResultSet rs)
			throws SQLException {
		return RowProcessor.toMap(rs);
	}

}
