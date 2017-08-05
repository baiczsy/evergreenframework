package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.RowProcessor;

public class ColumnListHandler<T> extends AbstractListHandler<T>{
	
	private int columnIndex;
	private Class<T> type;

	public ColumnListHandler(int columnIndex, Class<T> type) {
		this.columnIndex = columnIndex;
		this.type = type;
	}

	protected T getRow(ResultSet rs) throws SQLException {
		return RowProcessor.toColumnValue(rs, columnIndex,type);
	}

	
}
