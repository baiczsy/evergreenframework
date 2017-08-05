package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.RowProcessor;

public class BeanListHandler<T> extends AbstractListHandler<T> {

	private Class<T> type;

	public BeanListHandler(Class<T> type) {
		this.type = type;
	}

	@Override
	protected T getRow(ResultSet rs) throws SQLException {
		return RowProcessor.toBean(rs, type);
	}

}
