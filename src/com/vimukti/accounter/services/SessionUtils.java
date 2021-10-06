package com.vimukti.accounter.services;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class SessionUtils {

	public static final Boolean UPDATE_INCREMENT_REFERENCE_COUNT = true;
	public static final Boolean UPDATE_DECREMENT_REFERENCE_COUNT = false;

	static Logger log = Logger.getLogger(SessionUtils.class);

	public static <T> void updateReferenceCount(T previousObj, T currentObj,
			Session session, Boolean incrementCount) {

		execute(previousObj, currentObj, session, incrementCount);

	}

	public static <T> void execute(T previousObject, T obj, Session session,
			Boolean incrementCount) {

		Class<? extends Object> cls = obj.getClass();
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				Class<?> fieldType = field.getType();

				if (!isPrimitive(fieldType)) {
					if (isSet(fieldType)) {
						iterateSet(field, obj, session);
					} else if (isList(fieldType)) {
						iterateList(field, obj, session);
					} else {
						Object fieldValue = null;
						Object previousFieldValue = null;
						try {
							fieldValue = field.get(obj);
							if (previousObject != null) {
								previousFieldValue = field.get(previousObject);
							}
							log.info("Field:" + fieldValue);
							log.info("Previous Field:" + previousFieldValue);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (fieldValue != null) {

							try {
								if (!fieldType.equals(DecimalFormat.class)) {
									T refObject = (T) fieldValue;
									T prevRefObject = (T) previousFieldValue;
									Class<? extends Object> clazz = refObject
											.getClass();

									try {
										boolean flag = true;
										Class<? extends Object> clazz2 = clazz;
										while (clazz2 != null) {

											if (clazz2
													.getName()
													.equals("com.vimukti.accounter.core.Transaction")) {
												flag = false;
												break;
											} else {
												clazz2 = clazz2.getSuperclass();
											}
										}
										if (flag) {

											if (fieldValue != null
													&& previousFieldValue != null) {// For
												// Updation
												long id = getID(fieldValue);
												long prevId = getID(previousFieldValue);
												if (id != 0 && prevId != 0
														&& id != prevId) {
													// Updating previous
													// reference
													Class<? extends Object> prevRefClazz = prevRefObject
															.getClass();
													Field prop = prevRefClazz
															.getDeclaredField("referenceCount");
													if (!prop.isAccessible()) {
														prop.setAccessible(true);
													}
													prop.setLong(
															prevRefObject,
															((Long) prop
																	.get(prevRefObject))
																	.longValue() - 1l);
													session.saveOrUpdate(prevRefObject);

													Field property = clazz
															.getDeclaredField("referenceCount");
													if (!property
															.isAccessible()) {
														property.setAccessible(true);
													}
													if (incrementCount == true) {
														property.setLong(
																refObject,
																((Long) property
																		.get(refObject))
																		.longValue() + 1l);
													} else {
														property.setLong(
																refObject,
																((Long) property
																		.get(refObject))
																		.longValue() - 1l);
													}
												}
											} else if (fieldValue != null
													&& previousFieldValue == null) {// For
												// Creation
												// and
												// Deletion
												Field property = clazz
														.getDeclaredField("referenceCount");
												if (!property.isAccessible()) {
													property.setAccessible(true);
												}
												if (incrementCount == true) {
													property.setLong(
															refObject,
															((Long) property
																	.get(refObject))
																	.longValue() + 1l);
												} else {
													property.setLong(
															refObject,
															((Long) property
																	.get(refObject))
																	.longValue() - 1l);
												}
											}

										}

									} catch (SecurityException e) {
										e.printStackTrace();
									} catch (NoSuchFieldException e) {
										e.printStackTrace();
									}

								}

							} catch (HibernateException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			cls = cls.getSuperclass();
		}

	}

	public static <T> void update(T obj, Session session) {
		// execute(obj, session, null);

		Class<? extends Object> cls = obj.getClass();
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				Class<?> fieldType = field.getType();

				if (!isPrimitive(fieldType)) {
					if (isSet(fieldType)) {
						iterateSet(field, obj, session);
					} else if (isList(fieldType)) {
						iterateList(field, obj, session);
					} else {
						Object fieldValue = null;
						try {
							fieldValue = field.get(obj);
							log.info("Field:" + fieldValue);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (fieldValue != null) {
							long id = getID(fieldValue);
							log.info("Field ID:" + id);
							try {
								if (id > 0) {
									if (!fieldType.equals(DecimalFormat.class)) {
										field.set(obj,
												session.get(fieldType, id));
									}
								}
							} catch (HibernateException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			cls = cls.getSuperclass();
		}

	}

	public static <T> long getID(T obj) {
		try {
			Field idField = obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return idField.getLong(obj);
		} catch (Exception e) {
			Class<?> superclass = obj.getClass().getSuperclass();
			while (superclass != null) {
				try {
					Field idField = superclass.getDeclaredField("id");
					if (!idField.isAccessible()) {
						idField.setAccessible(true);
					}
					return idField.getLong(obj);
				} catch (SecurityException e1) {
					return -1;
				} catch (IllegalArgumentException e1) {
					return -1;
				} catch (NoSuchFieldException e1) {
					superclass = superclass.getSuperclass();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}

			}

			return -1;
		}
	}

	private static <T> void iterateList(Field field, T obj, Session session) {
		try {
			ArrayList<?> list = (ArrayList<?>) field.get(obj);
			if (list == null)
				return;
			for (Object o : list) {
				update(o, session);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static <T> void iterateSet(Field field, T obj, Session session) {
		try {
			Set<?> list = (Set<?>) field.get(obj);
			if (list == null)
				return;
			for (Object o : list) {
				update(o, session);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isList(Class<?> fieldType) {
		if (fieldType.getName().endsWith(
				"com.vimukti.accounter.web.client.core.ArrayList")) {
			return true;
		}
		for (Class<?> inter : fieldType.getInterfaces()) {
			if (inter.getName().endsWith(
					"com.vimukti.accounter.web.client.core.ArrayList")) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSet(Class<?> fieldType) {
		if (fieldType.getName().equals("java.util.Set")) {
			return true;
		}
		for (Class<?> inter : fieldType.getInterfaces()) {
			if (inter.getName().equals("java.util.Set")) {
				return true;
			}
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
	private static boolean isPrimitive(Class<?> fieldType) {
		if (fieldType.getName().equals("java.util.Date")
				|| fieldType.getName().equals("java.lang.String")) {
			return true;
		}
		return fieldType.isPrimitive();
	}

}
