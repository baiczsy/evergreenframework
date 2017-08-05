package org.evergreen.web.utils.beanutils;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 对原有PropertyUtilsBean增加了对list和Set以及Map集合映射的支持
 * 
 * @author other wangl
 * 
 */
public class PropertyUtilsBeanFirm extends PropertyUtilsBean {

	public PropertyUtilsBeanFirm() {
		super();
	}

	// 用于映射set集合,由于set是无序的,先使用Map封装
	private Map map = new HashMap();

	// 覆盖父类的getIndexedProperty方法
	public Object getIndexedProperty(Object bean, String name, int index)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (bean == null) {
			throw new IllegalArgumentException("No bean specified");
		}
		if (name == null || name.length() == 0) {
			if (bean.getClass().isArray()) {
				return Array.get(bean, index);
			} else if (bean instanceof List) {
				return ((List<?>) bean).get(index);
			}
		}
		if (name == null) {
			throw new IllegalArgumentException(
					"No name specified for bean class '" + bean.getClass()
							+ "'");
		}

		// Handle DynaBean instances specially
		if (bean instanceof DynaBean) {
			DynaProperty descriptor = ((DynaBean) bean).getDynaClass()
					.getDynaProperty(name);
			if (descriptor == null) {
				throw new NoSuchMethodException("Unknown property '" + name
						+ "' on bean class '" + bean.getClass() + "'");
			}
			return (((DynaBean) bean).get(name, index));
		}

		// Retrieve the property descriptor for the specified property
		PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
		if (descriptor == null) {
			throw new NoSuchMethodException("Unknown property '" + name
					+ "' on bean class '" + bean.getClass() + "'");
		}

		// Call the indexed getter method if there is one
		if (descriptor instanceof IndexedPropertyDescriptor) {
			Method readMethod = ((IndexedPropertyDescriptor) descriptor)
					.getIndexedReadMethod();
			readMethod = MethodUtils.getAccessibleMethod(bean.getClass(),
					readMethod);
			if (readMethod != null) {
				Object[] subscript = new Object[1];
				subscript[0] = new Integer(index);
				try {
					return (invokeMethod(readMethod, bean, subscript));
				} catch (InvocationTargetException e) {
					if (e.getTargetException() instanceof IndexOutOfBoundsException) {
						throw (IndexOutOfBoundsException) e
								.getTargetException();
					} else {
						throw e;
					}
				}
			}
		}

		// Otherwise, the underlying property must be an array
		Method readMethod = getReadMethod(bean.getClass(), descriptor);
		if (readMethod == null) {
			throw new NoSuchMethodException("Property '" + name + "' has no "
					+ "getter method on bean class '" + bean.getClass() + "'");
		}

		// Call the property getter and return the value
		Object value = invokeMethod(readMethod, bean, new Object[0]);

		if (!value.getClass().isArray()) {
			// 映射list集合
			if (value instanceof java.util.List) {
				// get the List's value
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				Type returnType = readMethod.getGenericReturnType();
				Type acturalType = ((ParameterizedType) returnType)
						.getActualTypeArguments()[0];
				while (list.size() <= index) {
					try {
						list.add(((Class<?>) acturalType).newInstance());
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					}
				}
				return list.get(index);
			}
			// 映射set集合
			else if (value instanceof java.util.Set) {
				@SuppressWarnings("unchecked")
				Set<Object> set = (Set<Object>) value;
				Type returnType = readMethod.getGenericReturnType();
				Type acturalType = ((ParameterizedType) returnType)
						.getActualTypeArguments()[0];
				String key = descriptor.hashCode() + "[" + index + "]";
				if (map.get(key) == null) {
					try {
						Object instance = ((Class<?>) acturalType)
								.newInstance();
						map.put(key, instance);
						set.add(instance);
					} catch (InstantiationException e) {
						e.printStackTrace();
					}

				}
				return map.get(key);
			} else {
				throw new IllegalArgumentException("Property '" + name
						+ "' is not indexed on bean class '" + bean.getClass()
						+ "'");
			}
		} else {
			// get the array's value
			try {
				return (Array.get(value, index));
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Index: " + index
						+ ", Size: " + Array.getLength(value)
						+ " for property '" + name + "'");
			}
		}
	}

	// 覆盖父类的getMappedProperty方法
	public Object getMappedProperty(Object bean, String name, String key)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (bean == null) {
			throw new IllegalArgumentException("No bean specified");
		}
		if (name == null) {
			throw new IllegalArgumentException(
					"No name specified for bean class '" + bean.getClass()
							+ "'");
		}
		if (key == null) {
			throw new IllegalArgumentException(
					"No key specified for property '" + name
							+ "' on bean class " + bean.getClass() + "'");
		}

		// Handle DynaBean instances specially
		if (bean instanceof DynaBean) {
			DynaProperty descriptor = ((DynaBean) bean).getDynaClass()
					.getDynaProperty(name);
			if (descriptor == null) {
				throw new NoSuchMethodException("Unknown property '" + name
						+ "'+ on bean class '" + bean.getClass() + "'");
			}
			return (((DynaBean) bean).get(name, key));
		}

		Object result = null;

		// Retrieve the property descriptor for the specified property
		PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
		if (descriptor == null) {
			throw new NoSuchMethodException("Unknown property '" + name
					+ "'+ on bean class '" + bean.getClass() + "'");
		}

		if (descriptor instanceof MappedPropertyDescriptor) {
			// Call the keyed getter method if there is one
			Method readMethod = ((MappedPropertyDescriptor) descriptor)
					.getMappedReadMethod();
			readMethod = MethodUtils.getAccessibleMethod(bean.getClass(),
					readMethod);
			if (readMethod != null) {
				Object[] keyArray = new Object[1];
				keyArray[0] = key;
				result = invokeMethod(readMethod, bean, keyArray);
			} else {
				throw new NoSuchMethodException("Property '" + name
						+ "' has no mapped getter method on bean class '"
						+ bean.getClass() + "'");
			}
		} else {
			/* means that the result has to be retrieved from a map */
			Method readMethod = getReadMethod(bean.getClass(), descriptor);
			if (readMethod != null) {
				Object invokeResult = invokeMethod(readMethod, bean,
						EMPTY_OBJECT_ARRAY);
				/* test and fetch from the map */
				if (invokeResult instanceof java.util.Map) {
					Map<Object, Object> map = (Map<Object, Object>) invokeResult;
					Type returnType = readMethod.getGenericReturnType();
					Type keyType = ((ParameterizedType) returnType)
							.getActualTypeArguments()[0];
					Type valType = ((ParameterizedType) returnType)
							.getActualTypeArguments()[1];
					Object mapKey = ConvertUtils.convert(key,
							(Class<?>) keyType);
					if (map.get(mapKey) == null) {
						try {
							map.put(mapKey, ((Class<?>) valType).newInstance());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return map.get(mapKey);
				}
			} else {
				throw new NoSuchMethodException("Property '" + name
						+ "' has no mapped getter method on bean class '"
						+ bean.getClass() + "'");
			}
		}
		return result;

	}
}