package com.vimukti.accounter.web.client.util;

import com.google.gwt.event.shared.EventHandler;

public interface CoreEventHandler<T> extends EventHandler{

	public void onAdd(T obj);
	public void onDelete(T obj);
	public void onChange(T obj);
}
