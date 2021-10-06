package com.vimukti.accounter.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class Util {

	private static ThreadLocal<Map<Object, Object>> cache = new ThreadLocal<Map<Object, Object>>();

	private static Map<String, Field> getAllFields(Class<?> cls) {
		Map<String, Field> mapFields = new HashMap<String, Field>();

		for (Class obj = cls; !obj.equals(Object.class); obj = obj
				.getSuperclass()) {
			for (Field field : obj.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					mapFields.put(field.getName(), field);
				} catch (IllegalArgumentException e) {
				}
			}
		}

		return mapFields;
	}

	private static <T> String getFieldInstanceID(T obj) {
		try {
			if (obj == null)
				return null;
			Field idField = obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return (String) idField.get(obj);
		} catch (Exception e) {
			Class<?> superclass = obj.getClass().getSuperclass();
			while (superclass != null) {
				try {
					Field idField = superclass.getDeclaredField("id");
					if (!idField.isAccessible()) {
						idField.setAccessible(true);
					}
					return (String) idField.get(obj);
				} catch (SecurityException e1) {
					return null;
				} catch (IllegalArgumentException e1) {
					return null;
				} catch (NoSuchFieldException e1) {
					superclass = superclass.getSuperclass();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}

			}

			return null;
		}
	}

	protected static Map<Object, Object> getCache() {

		Map<Object, Object> localCache = cache.get();
		if (localCache == null) {
			localCache = new HashMap<Object, Object>();
			cache.set(localCache);
		}
		return localCache;
	}

	public static boolean isNotMappingEntity(Class<?> class1) {
		String classNames[] = { "Contact", "Phone", "Fax", "Email", "Address",
				"UserPreferences", "CompanyPreferences", "NominalCodeRange" };
		String packge = "com.vimukti.accounter.core.";
		return class1.getName().equals(packge + classNames[0])
				|| class1.getName().equals(packge + classNames[1])
				|| class1.getName().equals(packge + classNames[2])
				|| class1.getName().equals(packge + classNames[3])
				|| class1.getName().equals(packge + classNames[4])
				|| class1.getName().equals(packge + classNames[5])
				|| class1.getName().equals(packge + classNames[6])
				|| class1.getName().equals(packge + classNames[7]);
	}

	private static boolean isString(Class<?> type) {
		if (type.getName().equals("java.lang.String"))
			return true;
		return false;
	}

	private static boolean isDate(Class class1) {
		if (class1.getName().equals("java.util.Date"))
			return true;
		return false;
	}

	public static Class<?> getClientEqualentClass(Class<?> serverClass) {
		try {
			return Class.forName("com.vimukti.accounter.web.client.core.Client"
					+ serverClass.getSimpleName());

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;

	}

	public static boolean isList(Class<?> fieldType) {
		if (fieldType.getName().endsWith(
				"com.vimukti.accounter.web.client.core.ArrayList")
				|| fieldType.getName().equals(
						"org.hibernate.collection.PersistentList")) {
			return true;
		}
		for (Class<?> inter : fieldType.getInterfaces()) {
			if (inter.getName().endsWith(
					"com.vimukti.accounter.web.client.core.ArrayList")
					|| inter.getName().equals(
							"org.hibernate.collection.PersistentList")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSet(Class<?> fieldType) {
		if (fieldType.getName().equals("java.util.Set")
				|| fieldType.getName().equals(
						"org.hibernate.collection.PersistentSet")) {
			return true;
		}
		for (Class<?> inter : fieldType.getInterfaces()) {
			if (inter.getName().equals("java.util.Set")
					|| inter.getName().equals(
							"org.hibernate.collection.PersistentSet")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPrimitiveWrapper(Class<?> fieldType) {
		String fieldName = fieldType.getName();
		if (fieldName.equals("java.lang.Double")
				|| fieldName.equals("java.lang.Long")
				|| fieldName.equals("java.lang.Boolean")
				|| fieldName.equals("java.lang.Float")) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks for primitive types and some default hibernate types,
	 * returns false only to entity types
	 * 
	 * @param fieldType
	 * @return
	 */
	public static boolean isPrimitive(Class<?> fieldType) {
		if (fieldType.getName().equals("java.util.Date")
				|| fieldType.getName().equals("java.lang.String")) {
			return true;
		}
		return fieldType.isPrimitive();
	}

	public static long getLongIdForGivenid(Class<?> cls, String account) {

		Session session = HibernateUtil.getCurrentSession();
		String hqlQuery = "select entity.id from " + cls.getName()
				+ " entity where entity.id=?";
		Query query = session.createQuery(hqlQuery).setString(0, account);
		List<?> l = query.list();
		if (l != null && !l.isEmpty() && l.get(0) != null) {
			return (Long) l.get(0);
		} else
			return 0;

	}

	@SuppressWarnings("unchecked")
	public static Class<? extends IAccounterCore> getClientClass(Object src) {
		if (src == null) {
			return null;
		}
		Class<?> srcClass = src.getClass();
		if (src instanceof HibernateProxy) {
			LazyInitializer lazyInitializer = ((HibernateProxy) src)
					.getHibernateLazyInitializer();
			srcClass = lazyInitializer.getPersistentClass();
		}
		return (Class<? extends IAccounterCore>) getClientEqualentClass(srcClass);
	}

}
