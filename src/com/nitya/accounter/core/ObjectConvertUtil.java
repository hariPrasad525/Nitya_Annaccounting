package com.nitya.accounter.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.IAccounterCore;

public class ObjectConvertUtil {
	private static final String classNames[] = { "Contact", "Phone", "Fax",
			"Email", "Address", "UserPreferences", "CompanyPreferences",
			"NominalCodeRange", "ComputationSlab", "ComputaionFormulaFunction",
			"AttendanceOrProductionItem", "UserDefinedPayheadItem" };
	protected ThreadLocal<Map<Object, Object>> cache = new ThreadLocal<Map<Object, Object>>();
	public ThreadLocal<Map<String, Object>> importedObjectsCache = new ThreadLocal<Map<String, Object>>();

	protected Map<String, Field> getAllFields(Class<?> cls) {
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

	protected static <T> Long getFieldInstanceID(T obj) {
		try {
			if (obj == null)
				return null;
			Field idField = obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return (Long) idField.get(obj);
		} catch (Exception e) {
			Class<?> superclass = obj.getClass().getSuperclass();
			while (superclass != null) {
				try {
					Field idField = superclass.getDeclaredField("id");
					if (!idField.isAccessible()) {
						idField.setAccessible(true);
					}
					return (Long) idField.get(obj);
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

	protected Map<Object, Object> getCache() {

		Map<Object, Object> localCache = cache.get();
		if (localCache == null) {
			localCache = new HashMap<Object, Object>();
			cache.set(localCache);
		}
		return localCache;
	}

	public boolean isNotMappingEntity(Class<?> class1) {
		String packge = "com.nitya.accounter.core.";
		return class1.getName().equals(packge + classNames[0])
				|| class1.getName().equals(packge + classNames[1])
				|| class1.getName().equals(packge + classNames[2])
				|| class1.getName().equals(packge + classNames[3])
				|| class1.getName().equals(packge + classNames[4])
				|| class1.getName().equals(packge + classNames[5])
				|| class1.getName().equals(packge + classNames[6])
				|| class1.getName().equals(packge + classNames[7])
				|| class1.getName().equals(packge + classNames[8])
				|| class1.getName().equals(packge + classNames[9])
				|| class1.getName().equals(packge + classNames[10]);
	}

	protected boolean isString(Class<?> type) {
		if (type.getName().equals("java.lang.String"))
			return true;
		return false;
	}

	protected boolean isDate(Class class1) {
		if (class1.getName().equals("java.util.Date"))
			return true;
		if (class1.getName().equals("java.sql.Timestamp"))
			return true;
		return false;
	}

	protected boolean isFinanceDate(Class class1) {
		if (class1.getName().equals("com.nitya.accounter.core.FinanceDate"))
			return true;
		return false;
	}

	protected boolean isClientFinanceDate(Class class1) {
		return class1.getName().equals(
				"com.nitya.accounter.web.client.core.ClientFinanceDate");
	}

	protected boolean isQuantity(Class<?> className) {
		return className.getName()
				.equals("com.nitya.accounter.core.Quantity");
	}

	protected boolean isCreditsAndPayments(Class<?> className) {
		return className.getName().equals(
				"com.nitya.accounter.core.CreditsAndPayments");
	}

	public Class<?> getClientEqualentClass(Class<?> serverClass) {
		try {
			return Class.forName("com.nitya.accounter.web.client.core.Client"
					+ serverClass.getSimpleName());

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;

	}

	public boolean isList(Class<?> fieldType) {
		if (fieldType.getName().endsWith("java.util.List")
				|| fieldType.getName().endsWith("java.util.ArrayList")
				|| fieldType.getName().equals(
						"org.hibernate.collection.PersistentList")) {
			return true;
		}
		for (Class<?> inter : fieldType.getInterfaces()) {
			if (inter.getName().endsWith("java.util.List")
					|| inter.getName().endsWith("java.util.ArrayList")
					|| inter.getName().equals(
							"org.hibernate.collection.PersistentList")) {
				return true;
			}
		}
		return false;
	}

	public boolean isSet(Class<?> fieldType) {
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

	public boolean isPrimitiveWrapper(Class<?> fieldType) {
		String fieldName = fieldType.getName();
		if (fieldName.equals("java.lang.Double")
				|| fieldName.equals("java.lang.Long")
				|| fieldName.equals("java.lang.Boolean")
				|| fieldName.equals("java.lang.Float")) {
			return true;
		}
		return false;
	}

	public boolean isMap(Class<?> fieldType) {
		String fieldName = fieldType.getName();
		if (fieldName.equals("java.util.Map")) {
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
	public boolean isPrimitive(Class<?> fieldType) {
		if (fieldType.getName()
				.equals("com.nitya.accounter.core.FinanceDate")
				|| fieldType.getName().equals("java.lang.String")
				|| fieldType.getName().equals("java.sql.Timestamp")
				|| fieldType.getName().equals("java.lang.Double")) {
			return true;
		}
		return fieldType.isPrimitive();
	}

	/**
	 * Converts the ServerClass from The ClientClass(Serializable)
	 * 
	 * @param class1
	 * @return
	 */

	public static <D extends IAccounterCore, S extends IAccounterServerCore> Class<S> getServerEqivalentClass(
			Class<?> class1) {

		String clientClassName = class1.getSimpleName();

		clientClassName = clientClassName.replaceAll("Client", "");

		Class<S> clazz = null;

		try {
			String qualifiedName = "com.nitya.accounter.core."
					+ clientClassName;
			clazz = (Class<S>) Class.forName(qualifiedName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazz;

	}

	/**
	 * Sets the given Company to the IAccounterServerCore Object
	 * 
	 * @param accounterClient
	 * @param companyFromSession
	 */
	public static void setCompany(IAccounterServerCore accounterClient,
			Company companyFromSession) {
		if (companyFromSession == null)
			return;
		Field field = getFieldByName(accounterClient.getClass(), "company");
		if (field == null)
			return;
		try {
			field.set(accounterClient, companyFromSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Field getFieldByName(Class<?> class1, String name) {
		for (Class obj = class1; !obj.equals(Object.class); obj = obj
				.getSuperclass()) {
			for (Field field : obj.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					if (field.getName() == name)
						return field;
				} catch (IllegalArgumentException e) {
				}
			}
		}
		return null;
	}

	/**
	 * Fernandez
	 * 
	 * @param <T>
	 * @param <R>
	 * @param oldList
	 * @param newList
	 * @param class1
	 * @param hibernateSession
	 * 
	 */
	// public <T, R extends Object> Set<T> modifyCollection(Set<T>
	// oldList,
	// List<R> newList, Class<T> class1, Session session) {
	//
	// if (newList == null)
	// return null;
	//
	// if (session == null)
	// return null;
	//
	// if (oldList == null)
	// oldList = new HashSet<T>();
	// else
	// oldList.clear();
	//
	// for (R r : newList) {
	//
	// try {
	//
	// Constructor<T> constructor = class1.getConstructor(
	// Session.class, r.getClass());
	//
	// T core = constructor.newInstance(session, r);
	//
	// oldList.add(core);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// return oldList;
	// }
	/**
	 * Fernandez
	 * 
	 * @param <T>
	 * @param <R>
	 * @param oldList
	 * @param newList
	 * @param class1
	 * @param session
	 * 
	 */
	// public <T extends IAccounterServerCore, R extends IAccounterCore>
	// Set<T> modifyCollection(
	// Set<T> oldList, Set<R> newList, Class<T> class1, Session session) {
	//
	// if (newList == null)
	// return null;
	//
	// if (session == null)
	// return null;
	//
	// if (oldList == null)
	// oldList = new HashSet<T>();
	// else
	// oldList.clear();
	//
	// for (R r : newList) {
	//
	// try {
	//
	// Constructor<T> constructor = class1.getConstructor(
	// Session.class, r.getClass());
	//
	// T core = constructor.newInstance(session, r);
	//
	// oldList.add(core);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// return oldList;
	// }
	public static Class<?> getEqivalentClientClass(String clientClassSimpleName) {

		try {

			String clientPackageName = "com.nitya.accounter.web.client.core.";

			String qualifiedClientClassName = clientPackageName
					.concat(clientClassSimpleName);

			return Class.forName(qualifiedClientClassName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public Class<?> getEqivalentServerClass(String serverClassSimpleName) {

		try {

			String serverPackageName = "com.nitya.accounter.core.";

			String qualifiedServerClassName = serverPackageName
					.concat(serverClassSimpleName);

			return Class.forName(qualifiedServerClassName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Object loadObjectByid(Session session, Class<?> serverClass,
			long id) {

		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		try {

			if (id == -1)
				return null;

			Object object = session.get(serverClass, id);
			if (object != null) {
				return HibernateUtil.initializeAndUnproxy(object);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.setFlushMode(flushMode);
		}

		return null;
	}

	public boolean isFieldExist(String fieldName, Class<?> class1) {
		return getAllFields(class1).containsKey(fieldName);
	}

	public static Class<?> classforName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Long getIdDirect(T entity) {
		if (entity instanceof HibernateProxy) {
			LazyInitializer lazyInitializer = ((HibernateProxy) entity)
					.getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {
				return (Long) lazyInitializer.getIdentifier();
			}
		}
		if (entity instanceof IAccounterServerCore) {
			return ((IAccounterServerCore) entity).getID();
		}
		return getFieldInstanceID(entity);
	}
}
