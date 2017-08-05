package org.evergreen.db.helper.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evergreen.db.helper.ResultSetHandler;
import org.evergreen.db.helper.RowProcessor;

public class ColumnHandler<T> implements ResultSetHandler<T> {

    private int columnIndex;
    private Class<T> type;

    public ColumnHandler(int columnIndex, Class<T> type) {
        this.columnIndex = columnIndex;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? RowProcessor.toColumnValue(rs, columnIndex, type)
                : null;
    }

}
