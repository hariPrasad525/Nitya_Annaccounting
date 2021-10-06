package com.vimukti.accounter.web.client.core;

public interface ISorting<T> {
	public T getObject(T parent, T child);

	public int sort(T obj1, T obj2, int col);
}
