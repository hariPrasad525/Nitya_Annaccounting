package com.vimukti.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * 
 * @author vimukti3
 * 
 * @param <IAccounterServerCore>
 *            base type : IAccounterServerCore or IAccounterCore
 */
public class CloneUtil2 extends ObjectConvertUtil {

	private Class<?> clazz;

	public CloneUtil2(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> Object toCloneList(
			List<?> set, boolean ignoreReferredObject) {
		if (set == null)
			return null;
		ArrayList result = new ArrayList();
		if (set.size() == 0)
			return result;
		for (Object src : set) {

			Class<? extends IAccounterCore> clientType = Util
					.getClientClass(src);
			if (clientType != null) {
				result.add(cloneObject(null, (S) src, clientType,
						ignoreReferredObject));
			} else {
				result.add(src);
			}

		}
		return result;
	}

	private <D extends IAccounterServerCore, S extends IAccounterServerCore> Object toCloneSet(
			Set<?> set, boolean ignoreReferredObject) {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;
		Object next = set.iterator().next();
		if (next != null) {
			for (Object src : set) {
				Class<? extends IAccounterCore> clientType = Util
						.getClientClass(src);
				if (clientType != null) {
					result.add(cloneObject(null, (S) src, clientType,
							ignoreReferredObject));
				} else {
					result.add(src);
				}
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "null" })
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> void cloneList(
			Field desField, Field srcField, Object dst, Object src,
			boolean ignoreReferredObject) {

		try {
			List<S> list = null;
			list = (List<S>) toCloneList((List<S>) srcField.get(src),
					ignoreReferredObject);
			desField.set(dst, list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <D extends IAccounterServerCore, S extends IAccounterServerCore> void cloneSet(
			Field desField, Field srcField, Object dst, Object src,
			boolean ignoreReferredObject) {
		try {

			Set<S> set = new HashSet<S>();
			set = (Set<S>) toCloneSet((Set<S>) srcField.get(src),
					ignoreReferredObject);
			desField.set(dst, set);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public <D extends IAccounterServerCore, S extends IAccounterServerCore> D clone(
			D dst, S src, Class<? extends IAccounterCore> clientType) {
		return clone(dst, src, clientType, false);
	}

	public <D extends IAccounterServerCore, S extends IAccounterServerCore> D clone(
			D dst, S src, Class<? extends IAccounterCore> clientType,
			boolean ignoreReferredObject) {
		cache.set(null);
		D ret = cloneObject(dst, src, clientType, ignoreReferredObject);
		cache.set(null);
		return ret;
	}

	private <D extends IAccounterServerCore, S extends IAccounterServerCore, C extends IAccounterCore> D cloneObject(
			D dst, S src, Class<? extends IAccounterCore> clientType,
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

			Map<String, Field> clientMap = getAllFields(clientType);

			for (String srcFieldName : srcMap.keySet()) {

				Field dstField = dstMap.get(srcFieldName);

				Field srcField = srcMap.get(srcFieldName);

				Field clientField = clientMap.get(srcFieldName);

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

				Class<C> clientFieldType = null;
				if (clientField != null) {
					clientFieldType = (Class<C>) clientField.getType();
				} else {
					dstField.set(dst, srcField.get(src));
					continue;
				}

				clientField.setAccessible(true);

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
				} else if (isQuantity(dstfieldType)) {

					dstField.set(dst, srcField.get(src));

				} else if (clazz.isInstance(srcField.get(src))) {
					// Check if we have this object in client side
					if (clientFieldType.isPrimitive()) {
						dstField.set(dst, srcField.get(src));
					} else {
						dstField.set(
								dst,
								getAfterCheckingInCache((D) dstField.get(dst),
										(S) srcField.get(src), clientFieldType,
										ignoreReferredObject));
					}
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

	private <D extends IAccounterServerCore, S extends IAccounterServerCore> D getAfterCheckingInCache(
			D dst, S src, Class<? extends IAccounterCore> clientType,
			boolean ignoreReferredObject) {
		Map<Object, Object> localCache = getCache();

		if (src == null) {
			return null;
		}
		D ret = (D) localCache.get(src);
		if (ret == null) {
			ret = cloneObject(dst, src, clientType, ignoreReferredObject);
			localCache.put(src, ret);
		}
		return ret;
	}

}
