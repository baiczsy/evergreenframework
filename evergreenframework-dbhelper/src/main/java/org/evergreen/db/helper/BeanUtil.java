package org.evergreen.db.helper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean转换工具,将查询结果集转换成Bean对象
 *
 * @author lenovo
 */
public class BeanUtil {

    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

    /**
     * 当数据库取出null值时,给基本数据类型默认值
     */
    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }

    /**
     * 创建Bean实例
     *
     * @param rs
     * @param type
     * @return Object
     */
    public static Object createBean(ResultSet rs, Class<?> type) throws SQLException {
        try {
            Object bean = type.newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnLabel(i);
                columnMapping(columnName, type, bean, rs);
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * 对象关系映射
     *
     * @param columnName
     * @throws Exception
     */
    private static void columnMapping(String columnName, Class<?> type, Object bean,
                                      ResultSet rs) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            //type = pd.getReadMethod().getDeclaringClass();
            String fieldName = pd.getName();
            Field field = type.getDeclaredField(fieldName);
            fieldName = (field.isAnnotationPresent(Column.class)) ? field.getAnnotation(Column.class)
                    .value() : fieldName;
            if (columnName.equalsIgnoreCase(fieldName)) {
                setValue(rs, pd, columnName, bean);
                break;
            }
        }
    }

    /**
     * Bean赋值操作
     *
     * @param rs
     * @param pd
     * @param columnName
     * @param bean
     * @throws Exception
     */
    private static void setValue(ResultSet rs, PropertyDescriptor pd,
                                 String columnName, Object bean) throws Exception {
        Object value = processColumn(rs, columnName, pd.getPropertyType());
        if (value == null && pd.getPropertyType().isPrimitive()) {
            value = primitiveDefaults.get(pd.getPropertyType());
        }
        pd.getWriteMethod().invoke(bean, value);
    }

    /**
     * 处理数据类型的转换
     *
     * @param rs
     * @param columnName
     * @param propType
     * @return
     * @throws SQLException
     */
    static Object processColumn(ResultSet rs, String columnName,
                                        Class propType) throws SQLException {

        if (!propType.isPrimitive() && rs.getObject(columnName) == null) {
            return null;
        }

        Object value = null;

        if (propType.equals(String.class)) {
            value = rs.getString(columnName);

        } else if(propType.isEnum()){
            value = Enum.valueOf(propType, rs.getString(columnName));

        } else if (propType.equals(Integer.TYPE)
                || propType.equals(Integer.class)) {
            value = Integer.valueOf(rs.getInt(columnName));

        } else if (propType.equals(Boolean.TYPE)
                || propType.equals(Boolean.class)) {
            value = Boolean.valueOf(rs.getBoolean(columnName));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            value = Long.valueOf(rs.getLong(columnName));

        } else if (propType.equals(Double.TYPE)
                || propType.equals(Double.class)) {
            value = Double.valueOf(rs.getDouble(columnName));

        } else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            value = Float.valueOf(rs.getFloat(columnName));

        } else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            value = Short.valueOf(rs.getShort(columnName));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            value = Byte.valueOf(rs.getByte(columnName));

        } else if (propType.equals(SQLXML.class)) {
            value = rs.getSQLXML(columnName);

        } else {
            value = rs.getObject(columnName);
            //处理日期类型
            value = processDate(value, propType);

        }
        return value;
    }

    /**
     * 将数据库的日期转换成java中的日期类型
     */
    private static Object processDate(Object value, Class<?> paramType) {
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
