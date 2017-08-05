package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.RowProcessor;


public class ArrayListHandler extends AbstractListHandler<Object[]> {

	@Override
	protected Object[] getRow(ResultSet rs) throws SQLException {
		return RowProcessor.toArray(rs);
	}

}
