package org.evergreen.db.helper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wangl
 * @date 2021/7/29
 * Bean反射工具类
 */
public class BeanUtils {

    /**
     * 根据结果集创建Bean实例
     * @param rs
     * @param beanClass
     * @return
     */
    public static Object createBean(ResultSet rs,
                                    Class<?> beanClass) throws SQLException {
        //创建Bean实例
        Object instance = newInstance(beanClass);
        //获取Bean的属性描述器
        PropertyDescriptor[] pds = propertyDescriptors(beanClass);
        //循环遍历描述器
        for(PropertyDescriptor pd: pds) {
            //根据属性描述器解析出列名
            String columnName = getColumnName(pd, beanClass);
            //调用set方法赋值
            callSetter(pd, columnName, instance, rs);
        }
        return instance;
    }

    /**
     * 根据Class对象创建实例
     * @param beanClass
     * @return
     */
    private static Object newInstance(Class<?> beanClass) throws SQLException{
        try {
            return beanClass.newInstance();
        } catch (Exception e) {
            throw new SQLException("Cannot create " + beanClass.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 通过内省得到所有的属性描述器
     * @return
     */
    private static PropertyDescriptor[] propertyDescriptors(Class<?> beanClass) throws SQLException{
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new SQLException("Bean introspection fail: " + e.getMessage());
        }
    }

    /**
     * 根据属性描述器解析出表的列名
     * @param pd
     * @param beanClass
     * @return
     * @throws SQLException
     */
    private static String getColumnName(PropertyDescriptor pd, Class<?> beanClass) throws SQLException{
        try {
            //获取字段的名称
            String columnName = pd.getName();
            //根据字段名获取Field对象
            Field field = beanClass.getDeclaredField(columnName);
            //获取字段上的注解,如果字段名和表的列名一致，则使用字段名
            //否则使用注解指定的列名
            columnName = field.isAnnotationPresent(Column.class)
                    ? field.getAnnotation(Column.class).value()
                    : columnName;
            return columnName;
        } catch (NoSuchFieldException e) {
            throw new SQLException("Cannot resolve " + pd.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 调用描述器的set方法进行复制操作，成功返回true
     * @param pd 当前属性的描述器
     * @param columnName 字段对应表的列名
     * @param beanInstance Bean的实例
     * @param rs 结果集
     * @return
     */
    private static void callSetter(PropertyDescriptor pd, String columnName,
                                   Object beanInstance, ResultSet rs) throws SQLException{
        try{
            //根据类名获取结果集的值（注意：不要直接使用getObject方法
            // ，应该根据字段的具体类型使用getXxx的方法赋值）
            Object value = ColumnUtils.columnConvert(rs, columnName, pd.getPropertyType());
            //最后通过调用bean的set方法将value赋值进去
            pd.getWriteMethod().invoke(beanInstance, value);
        }catch(Exception e) {
            throw new SQLException("Cannot set " + pd.getName() + ": " + e.getMessage());
        }
    }
}
