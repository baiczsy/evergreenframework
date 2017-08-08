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
                setProperty(columnName, type, bean, rs);
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Bean赋值操作，将column的值映射到具体的Bean属性中
     *
     * @param columnName
     * @throws Exception
     */
    private static void setProperty(String columnName, Class<?> type, Object bean,
                                      ResultSet rs) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            type = pd.getReadMethod().getDeclaringClass();
            String fieldName = pd.getName();
            Field field = type.getDeclaredField(fieldName);
            fieldName = (field.isAnnotationPresent(Column.class)) ? field.getAnnotation(Column.class)
                    .value() : fieldName;
            if (columnName.equalsIgnoreCase(fieldName)) {
                Object value = ColumnUtil.columnConvert(rs, columnName, pd.getPropertyType());
                if (value == null && pd.getPropertyType().isPrimitive()) {
                    value = primitiveDefaults.get(pd.getPropertyType());
                }
                pd.getWriteMethod().invoke(bean, value);
                break;
            }
        }
    }

}
