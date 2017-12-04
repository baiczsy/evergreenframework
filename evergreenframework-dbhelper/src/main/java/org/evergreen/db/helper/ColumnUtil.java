package org.evergreen.db.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;

public class ColumnUtil {

    /**
     * 处理数据类型的转换
     *
     * @param rs
     * @param columnName
     * @param propType
     * @return
     * @throws SQLException
     */
    public static Object columnConvert(ResultSet rs, String columnName,
                                Class propType) throws SQLException {

        if (!propType.isPrimitive() && rs.getObject(columnName) == null) {
            return null;
        }

        Object value = null;

        if (propType.equals(String.class)) {
            value = rs.getString(columnName);

        } else if(value instanceof String && propType.isEnum()){
            value = Enum.valueOf(propType, rs.getString(columnName));

        } else if (propType.equals(Integer.TYPE)
                || propType.equals(Integer.class)) {
            value = rs.getInt(columnName);

        } else if (propType.equals(Boolean.TYPE)
                || propType.equals(Boolean.class)) {
            value = rs.getBoolean(columnName);

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            value = rs.getLong(columnName);

        } else if (propType.equals(Double.TYPE)
                || propType.equals(Double.class)) {
            value = rs.getDouble(columnName);

        } else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            value = rs.getFloat(columnName);

        } else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            value = rs.getShort(columnName);

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            value = rs.getByte(columnName);

        } else if (propType.equals(Character.TYPE) || propType.equals(Character.class)){
            value = rs.getString(columnName).charAt(0);

        } else if (propType.equals(SQLXML.class)) {
            value = rs.getSQLXML(columnName);

        } else {
            value = rs.getObject(columnName);
            //处理日期类型
            value = convertDate(value, propType);

        }
        return value;
    }

    /**
     * 将数据库的日期转换成java中的日期类型
     */
    private static Object convertDate(Object value, Class<?> paramType) {
        if (value instanceof java.util.Date) {
            final String type = paramType.getName();
            if ("java.sql.Date".equals(type)) {
                value = new java.sql.Date(((java.util.Date) value).getTime());
            } else if ("java.sql.Time".equals(type)) {
                value = new java.sql.Time(((java.util.Date) value).getTime());
            } else if ("java.sql.Timestamp".equals(type)) {
                Timestamp ts = (Timestamp) value;
                int nanos = ts.getNanos();
                value = new java.sql.Timestamp(ts.getTime());
                ((Timestamp) value).setNanos(nanos);
            }
        }
        return value;
    }
}
