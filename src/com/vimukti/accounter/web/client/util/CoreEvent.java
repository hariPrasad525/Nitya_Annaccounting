package com.vimukti.accounter.web.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;

public class CoreEvent<T> extends GwtEvent<CoreEventHandler<T>> {

	@SuppressWarnings("rawtypes")
	private static final Map map = new HashMap();

	private ChangeType type;
	private T obj;

	public CoreEvent(ChangeType type, T core) {
		this.type = type;
		obj = core;
	}

	@Override
	public Type<CoreEventHandler<T>> getAssociatedType() {
		return getType(obj.getClass());
	}
	@SuppressWarnings("unchecked")
	public static <T> Type<CoreEventHandler<T>> getType(Class<?> cls) {
		Type<CoreEventHandler<T>> type = (Type<CoreEventHandler<T>>) map
				.get(cls);
		if (type == null) {
			type = new Type<CoreEventHandler<T>>();
			map.put(cls, type);
		}
		return type;
	}

	@Override
	protected void dispatch(CoreEventHandler<T> handler) {
		switch (type) {
		case ADD:
			handler.onAdd(obj);
		case CHANGE:
			handler.onChange(obj);
			break;
		case DELETE:
			handler.onDelete(obj);
			break;
		}
	}
}
