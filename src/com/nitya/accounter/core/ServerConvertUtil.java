package com.nitya.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public class ServerConvertUtil extends ObjectConvertUtil {

	Map<Map<Field, Object>, IAccounterServerCore> collections = new HashMap<Map<Field, Object>, IAccounterServerCore>();

	// private HashMap<Field, Object> fieldValues;
	// private Session currentSession;

	public <D extends IAccounterServerCore, S extends IAccounterCore> Object toServerList(
			List<?> set, Session session) throws AccounterException {
		if (set == null)
			return null;
		ArrayList result = new ArrayList<Account>();
		if (set.size() == 0)
			return result;
		Class class1 = getServerEqivalentClass(set.iterator().next().getClass());
		for (Object src : set) {
			if (isNotMappingEntity(class1)) {
				result.add(toServerObjectInternal(null, (S) src, session));
			} else {
				D obj = (D) loadObjectByid(session, class1, ((S) src).getID());
				result.add(toServerObjectInternal(obj, (S) src, session));
			}
		}
		return result;
	}

	public <D extends IAccounterServerCore, S extends IAccounterCore> Object toServerSet(
			Set<?> set, Session session) throws AccounterException {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;
		Object next = set.iterator().next();
		if (next != null) {
			Class class1 = getServerEqivalentClass(set.iterator().next()
					.getClass());
			for (Object src : set) {
				if (isNotMappingEntity(class1)) {
					result.add(toServerObjectInternal(null, (S) src, session));
				} else {
					D obj = (D) loadObjectByid(session, class1,
							((S) src).getID());
					result.add(toServerObjectInternal(obj, (S) src, session));
				}
			}
		}
		return result;
	}

	private <D extends IAccounterServerCore, S extends IAccounterCore> void modifyList(
			Field desField, Field srcField, Object dst, Object src,
			Session session) {

		try {
			List<S> list = (List<S>) desField.get(dst);
			list = list == null ? new ArrayList() : list;

			if (isSet(srcField.getType())) {
				HashSet set = (HashSet) toServerSet((Set<?>) srcField.get(src),
						session);
				list.clear();
				if (set != null)
					list.addAll(set);

			} else {
				List result = (List<S>) toServerList(
						(List<S>) srcField.get(src), session);
				list.clear();
				if (result != null)
					list.addAll(result);
			}
			if (list != null && !list.isEmpty()) {
				// fieldValues = new HashMap<Field, Object>();
				// fieldValues.put(desField, list);
				// collections.put(fieldValues, (IAccounterServerCore) dst);
				desField.set(dst, list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <D extends IAccounterServerCore, S extends IAccounterCore> void modifySet(
			Field desField, Field srcField, Object dst, Object src,
			Session session) {
		try {

			Set<S> set = (Set<S>) desField.get(dst);
			set = set == null ? new HashSet<S>() : set;

			if (isList(srcField.getType())) {
				List list = (List) toServerList((List<?>) srcField.get(src),
						session);
				set.clear();
				if (list != null)
					set.addAll(list);

			} else {
				Set result = (Set<S>) toServerSet((Set<S>) srcField.get(src),
						session);
				set.clear();
				if (result != null)
					set.addAll(result);
			}
			if (set != null && !set.isEmpty()) {
				// fieldValues = new HashMap<Field, Object>();
				// fieldValues.put(desField, set);
				// collections.put(fieldValues, (IAccounterServerCore) dst);
				desField.set(dst, set);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public <D extends IAccounterServerCore, S extends IAccounterCore> D toServerObject(
			D dst, S src, Session session) throws AccounterException {
		cache.set(null);
		D ret = toServerObjectInternal(dst, src, session);
		cache.set(null);
		// /**
		// * New objects trying save if any query executes during conversion of
		// * Client to Server Object. to solve that problem, hold collections of
		// * objects in Map and set fields values to field after conversion.
		// */
		// try {
		// for (Map<Field, Object> fieldValues : collections.keySet()) {
		// Object dstObj = collections.get(fieldValues);
		// for (Field field : fieldValues.keySet()) {
		// Object collection = field.get(dstObj);
		// if (fieldValues.get(field) instanceof CreditsAndPayments) {
		// field.set(dstObj, fieldValues.get(field));
		// continue;
		//
		// }
		// boolean isSet = ((collection != null) && isSet(collection
		// .getClass()))
		// || (fieldValues.get(field) != null && isSet(fieldValues
		// .get(field).getClass()));
		// if (isSet) {
		// Set set = collection == null ? new HashSet()
		// : (Set) collection;
		// set.clear();
		//
		// set.addAll((Collection) fieldValues.get(field));
		// field.set(dstObj, set);
		// } else {
		// List list = collection == null ? new ArrayList()
		// : (List) collection;
		// list.clear();
		// list.addAll((Collection) fieldValues.get(field));
		// field.set(dstObj, list);
		// }
		//
		// break;
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return ret;
	}

	private <D extends IAccounterServerCore, S extends IAccounterCore> D toServerObjectInternal(
			D dst, S src, Session session) throws AccounterException {

		try {

			if (src == null)
				return null;
			if (dst != null) {
				dst = HibernateUtil.initializeAndUnproxy(dst);
			}
			Class<D> serverCoreClass = getServerEqivalentClass(src.getClass());
			if (dst == null && !isNotMappingEntity(serverCoreClass)) {
				dst = (D) loadObjectByid(session, serverCoreClass, src.getID());
			}

			dst = dst == null ? serverCoreClass.newInstance() : dst;
			boolean isCreating = dst.getID() == 0l;
			getCache().put(src, dst);

			Class<S> srcType = (Class<S>) src.getClass();

			Class<D> dstType = (Class<D>) dst.getClass();

			Map<String, Field> srcMap = getAllFields(srcType);

			Map<String, Field> dstMap = getAllFields(dstType);

			for (String dstFieldName : dstMap.keySet()) {

				Field dstField = dstMap.get(dstFieldName);

				Field srcField = srcMap.get(dstFieldName);

				if (srcField == null)
					continue;

				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;

				srcField.setAccessible(true);

				dstField.setAccessible(true);

				Class<S> srcfieldType = (Class<S>) srcField.getType();

				Class<D> dstfieldType = (Class<D>) dstField.getType();

				if (dstField.getAnnotation(Exempted.class) != null) {
					continue;
				}

				if (!isCreating
						&& dstField.getAnnotation(NonEditable.class) != null) {
					continue;
				}

				// create id to object...
				// if (dstFieldName.equals("id")) {
				// String val = (String) srcField.get(src);
				// if (val == null || val != null && val.isEmpty()) {
				// String id = SecureUtils.createID();
				// dstField.set(dst, id);
				// srcField.set(src, id);
				// continue;
				// }
				// }
				// For Collection Objects
				if (srcField.get(src) == null) {
					continue;
				}

				if (isSet(dstfieldType)) {

					modifySet(dstField, srcField, dst, src, session);

				} else if (isList(dstfieldType)) {

					modifyList(dstField, srcField, dst, src, session);

				} else if (dstfieldType.isPrimitive()) {

					// if (srcfieldType.equals(int.class)) {
					// if (srcField.getInt(src) == 0)
					// continue;
					// }
					// if (srcfieldType.equals(long.class)) {
					// if (srcField.getLong(src) == 0)
					// continue;
					// }
					// if (srcfieldType.equals(boolean.class)) {
					// if (srcField.getBoolean(src) == Boolean.FALSE)
					// continue;
					// }
					// if (srcfieldType.equals(double.class)) {
					// if (DecimalUtil.isEquals(srcField.getDouble(src), 0.0d))
					// continue;
					// }

					// if Primitive
					if (srcfieldType.isPrimitive()) {

						dstField.set(dst, srcField.get(src));

					}

				} else if (isPrimitiveWrapper(dstfieldType)) {
					if (srcField.get(src) != null)
						dstField.set(dst, srcField.get(src));
				} else if (isMap(dstfieldType)
						|| srcField.get(src) instanceof Map) {
					Map<?, ?> map = toMap((Map) srcField.get(src));
					dstField.set(dst, map);
				} else {

					// its A Non Collection Reference

					if (srcfieldType.isPrimitive()) {
						// Check Whether its Date

						long longValue = srcField.getLong(src);
						if (longValue == 0) {
							dstField.set(dst, null);
							continue;
						}

						if (isFinanceDate(dstfieldType)) {
							dstField.set(dst, new FinanceDate(longValue));

						} else if (isDate(srcField.getType())) {
							Date date = (Date) srcField.get(src);
							dstField.setLong(dst, date != null ? date.getTime()
									: 0);
						} else {
							// load the object by given Id.
							Object object = loadObjectByid(session,
									dstfieldType, longValue);
							dstField.set(dst, object);
						}
					} else {
						// Its Not Primitive and Non Collection Object

						if ((isString(srcfieldType) && !isString(dstfieldType))
								&& (srcField.getName().equals(dstField
										.getName()))) {

							// Its a Persistent Object Reference Id
							// So Get the Object with id from Session
							if (isNotMappingEntity(dstfieldType)) {
								dstField.set(
										dst,
										toServerObjectInternal(
												(D) dstField.get(dst),
												(S) srcField.get(src), session));
							} else {
								// Object t = session.get(dstfieldType,
								// longValue);

								dstField.set(
										dst,
										loadObjectByid(session, dstfieldType,
												(Long) srcField.get(src)));
							}

						} else if (isString(dstField.getType())) {

							dstField.set(dst, srcField.get(src));

						} else if (isQuantity(dstfieldType)) {

							dstField.set(
									dst,
									getQuantity(
											(ClientQuantity) srcField.get(src),
											session));

						} else if (isClientFinanceDate(srcfieldType)) {
							dstField.set(dst, new FinanceDate(
									(ClientFinanceDate) srcField.get(src)));
						} else {
							D d = getServerAfterCheckingInCache(
									(D) dstField.get(dst),
									(S) srcField.get(src), session);
							// if
							// (dstField.getAnnotation(SpecialReference.class)
							// != null) {
							// fieldValues = new HashMap<Field, Object>();
							// fieldValues.put(dstField, d);
							// collections.put(fieldValues,
							// (IAccounterServerCore) dst);
							// } else
							dstField.set(dst, d);
						}

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

		return dst;

	}

	/**
	 * @param session
	 * @param object
	 * @return
	 */
	private Quantity getQuantity(ClientQuantity cq, Session session) {
		Quantity q = new Quantity();
		q.setValue(cq.getValue());
		q.setUnit((Unit) session.get(Unit.class, cq.getUnit()));
		return q;
	}

	private Map<?, ?> toMap(Map map) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Map<Object, Object> imprtedMap = new HashMap<Object, Object>();
		for (Object key : map.keySet()) {
			Object val = map.get(key);
			Object clientKey = null;
			if (key instanceof IAccounterServerCore) {
				clientKey = toServerObjectInternal(null, (IAccounterCore) val,
						session);
			}
			if (val instanceof IAccounterServerCore) {
				val = toServerObjectInternal(null, (IAccounterCore) val,
						session);
			}
			if (clientKey == null)
				imprtedMap.put(key, val);
			else {
				imprtedMap.put(clientKey, val);
			}
		}
		return imprtedMap;
	}

	private <D extends IAccounterServerCore, S extends IAccounterCore> D getServerAfterCheckingInCache(
			D dst, S src, Session session) throws AccounterException {
		Map<Object, Object> localCache = getCache();

		if (src == null) {
			return null;
		}
		D ret = (D) localCache.get(src);
		if (ret == null) {
			ret = toServerObjectInternal(dst, src, session);
			localCache.put(src, ret);
		}
		return ret;
	}

	public static <T> void swap(T[] a, int x, int y) {
		T t = a[x];
		a[x] = a[y];
		a[y] = t;
	}

	public static <T extends Comparable<? super T>> void mergeInOrder(T[] src,
			T[] dst, int p1, int p2, int p3, int p4) {
		if (src[p2].compareTo(src[p3]) <= 0)
			return; // already sorted!

		// cut away ends
		while (src[p1].compareTo(src[p3]) <= 0)
			p1++;
		while (src[p2].compareTo(src[p4]) <= 0)
			p4--;

		int i1 = p1;
		int i3 = p3;
		int di = p1;
		while (di < p4) {
			if (src[i1].compareTo(src[i3]) <= 0) {
				dst[di++] = src[i1++];
			} else {
				dst[di++] = src[i3++];
				if (i3 > p4) {
					System.arraycopy(src, i1, dst, di, p2 - i1 + 1);
					break;
				}
			}
		}

		System.arraycopy(dst, p1, src, p1, (p4 - p1) + 1);
	}

	public static <T extends Comparable<? super T>> void mergeSort(T[] src,
			T[] dst, int start, int end) {
		if (start + 1 >= end) {
			if (start >= end)
				return;
			if (src[start].compareTo(src[end]) > 0) {
				swap(src, start, end);
			}
			return;
		}

		int middle = (start + end) / 2;
		mergeSort(src, dst, start, middle);
		mergeSort(src, dst, middle + 1, end);
		mergeInOrder(src, dst, start, middle, middle + 1, end);
	}

	private static ThreadLocal<Comparable<?>[]> mergeSortTemp = new ThreadLocal<Comparable<?>[]>();

	public static <T extends Comparable<? super T>> void mergeSort(T[] src) {
		int length = src.length;
		Comparable<?>[] temp = mergeSortTemp.get();
		if ((temp == null) || (temp.length < length)) {
			temp = new Comparable[length * 3 / 2];
			mergeSortTemp.set(temp);
		}
	}

}