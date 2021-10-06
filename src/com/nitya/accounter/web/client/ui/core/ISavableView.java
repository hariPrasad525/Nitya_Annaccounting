package com.nitya.accounter.web.client.ui.core;

public interface ISavableView<T> {
	public T saveView();

	public void restoreView(T viewDate);
}
