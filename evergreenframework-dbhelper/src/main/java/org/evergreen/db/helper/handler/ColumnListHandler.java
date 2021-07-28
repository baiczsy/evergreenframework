package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.RowProcessor;

public class ColumnListHandler<T> extends AbstractListHandler<T>{
	
	private int columnIndex;

	public ColumnListHandler(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	@Override
	protected T getRow(ResultSet rs) throws SQLException {
		return (T) RowProcessor.toValue(rs, columnIndex);
	}

	
}
