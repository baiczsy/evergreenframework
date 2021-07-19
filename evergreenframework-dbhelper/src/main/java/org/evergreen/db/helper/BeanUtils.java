package org.evergreen.db.helper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Bean转换工具,将查询结果集转换成Bean对象
 *
 * @author lenovo
 */
public class BeanUtils {

    /**
     * 创建Bean实例
     *
     * @param rs
     * @param type
     * @return Object
     */
    public static Object createBean(ResultSet rs, Class<?> type) throws SQLException {
        Object bean = newInstance(type);
        PropertyDescriptor[] propertyDescriptors = propertyDescriptors(type);
        ResultSetMetaData metaData = rs.getMetaData();
        for (PropertyDescriptor pd : propertyDescriptors) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnLabel(i);
                if(callSetter(pd, columnName, type, bean, rs)) {
                    break;
                }
            }
        }
        return bean;
    }

    /**
     * 构建Bean实例
     *
     * @param type
     * @return
     * @throws SQLException
     */
    private static Object newInstance(Class<?> type) throws SQLException {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new SQLException("Cannot create " + type.getName() + ": " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new SQLException("Cannot create " + type.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 内省后去所有的属性描述
     *
     * @param type
     * @return
     * @throws SQLException
     */
    private static PropertyDescriptor[] propertyDescriptors(Class<?> type)
            throws SQLException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new SQLException("Bean introspection failed: " + e.getMessage());
        }
    }

    /**
     * Bean赋值操作，将column的值映射到具体的Bean属性中
     *
     * @param columnName
     * @throws Exception
     */
    private static boolean callSetter(PropertyDescriptor pd, String columnName, Class<?> type, Object bean,
                                    ResultSet rs) throws SQLException {
        try {
            type = pd.getReadMethod().getDeclaringClass();
            String fieldName = pd.getName();
            Field field = type.getDeclaredField(fieldName);
            fieldName = (field.isAnnotationPresent(Column.class)) ? field.getAnnotation(Column.class)
                    .value() : fieldName;
            if (columnName.equalsIgnoreCase(fieldName)) {
                Object value = ColumnUtils.columnConvert(rs, columnName, pd.getPropertyType());
                pd.getWriteMethod().invoke(bean, value);
                return true;
            }
        } catch (NoSuchFieldException e) {
            throw new SQLException("Cannot set " + pd.getName() + ": " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new SQLException("Cannot set " + pd.getName() + ": " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new SQLException("Cannot set " + pd.getName() + ": " + e.getMessage());
        }
        return false;
    }

}
