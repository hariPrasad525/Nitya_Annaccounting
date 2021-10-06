package com.vimukti.accounter.web.client.ui.edittable;

public interface IRowFilter<T> {
	public boolean filter(T rowValue);
}
