package com.nitya.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author vimukti3
 * 
 * @param <BT>
 *            base type : IAccounterServerCore or IAccounterCore
 */
public class CloneUtil<BT> extends ObjectConvertUtil {

	private Class<?> clazz;

	public CloneUtil(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	private <D extends BT, S extends BT> Object toCloneList(List<?> set,
			boolean ignoreReferredObject) {
		if (set == null)
			return null;
		ArrayList result = new ArrayList();
		if (set.size() == 0)
			return result;
		for (Object src : set) {
			result.add(cloneObject(null, (S) src, ignoreReferredObject));
		}
		return result;
	}

	private <D extends BT, S extends BT> Object toCloneSet(Set<?> set,
			boolean ignoreReferredObject) {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;
		Object next = set.iterator().next();
		if (next != null) {
			for (Object src : set) {
				result.add(cloneObject(null, (S) src, ignoreReferredObject));
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "null" })
	private <D extends BT, S extends BT> void cloneList(Field desField,
			Field srcField, Object dst, Object src, boolean ignoreReferredObject) {

		try {
			List<S> list = null;
			list = (List<S>) toCloneList((List<S>) srcField.get(src),
					ignoreReferredObject);
			desField.set(dst, list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <D extends BT, S extends BT> void cloneSet(Field desField,
			Field srcField, Object dst, Object src, boolean ignoreReferredObject) {
		try {

			Set<S> set = new HashSet<S>();
			set = (Set<S>) toCloneSet((Set<S>) srcField.get(src),
					ignoreReferredObject);
			desField.set(dst, set);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public <D extends BT, S extends BT> D clone(D dst, S src) {
		return clone(dst, src, false);
	}

	public <D extends BT, S extends BT> D clone(D dst, S src,
			boolean ignoreReferredObject) {
		cache.set(null);
		D ret = cloneObject(dst, src, ignoreReferredObject);
		cache.set(null);
		return ret;
	}

	private <D extends BT, S extends BT> D cloneObject(D dst, S src,
			boolean ignoreReferredObject) {

		try {

			if (src == null)
				return null;

			dst = (D) src.getClass().newInstance();

			getCache().put(src, dst);

			Class<S> srcType = (Class<S>) src.getClass();

			Class<D> dstType = (Class<D>) dst.getClass();

			Map<String, Field> srcMap = getAllFields(srcType);

			Map<String, Field> dstMap = getAllFields(dstType);

			for (String srcFieldName : srcMap.keySet()) {

				Field dstField = dstMap.get(srcFieldName);

				Field srcField = srcMap.get(srcFieldName);

				if (srcField == null)
					continue;

				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;

				srcField.setAccessible(true);

				dstField.setAccessible(true);

				Class<D> dstfieldType = (Class<D>) dstField.getType();

				// For Collection Objects
				if (srcField.get(src) == null)
					continue;

				if (!ignoreReferredObject && isRefferedObject(srcField)) {
					dstField.set(dst, srcField.get(src));
					continue;
				}

				if (isSet(dstfieldType)) {

					cloneSet(dstField, srcField, dst, src, ignoreReferredObject);

				} else if (isList(dstfieldType)) {

					cloneList(dstField, srcField, dst, src,
							ignoreReferredObject);

				} else if (dstfieldType.isPrimitive()) {
					dstField.set(dst, srcField.get(src));

				} else if (isPrimitiveWrapper(dstfieldType)) {
					if (srcField.get(src) != null)
						dstField.set(dst, srcField.get(src));
				} else if (isFinanceDate(dstfieldType)) {
					dstField.set(dst, srcField.get(src));
				} else if (isDate(srcField.getType())) {
					// Date date = (Date) srcField.get(src);
					dstField.set(dst, srcField.get(src));
				} else if (isString(dstField.getType())) {
					dstField.set(dst, srcField.get(src));
				} else if (clazz.isInstance(srcField.get(src))) {
					dstField.set(
							dst,
							getAfterCheckingInCache((D) dstField.get(dst),
									(S) srcField.get(src), ignoreReferredObject));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dst;

	}

	private boolean isRefferedObject(Field srcField) {

		if (srcField.getAnnotation(ReffereredObject.class) != null)
			return true;

		return false;
	}

	private <D extends BT, S extends BT> D getAfterCheckingInCache(D dst,
			S src, boolean ignoreReferredObject) {
		Map<Object, Object> localCache = getCache();

		if (src == null) {
			return null;
		}
		D ret = (D) localCache.get(src);
		if (ret == null) {
			ret = cloneObject(dst, src, ignoreReferredObject);
			localCache.put(src, ret);
		}
		return ret;
	}

}
