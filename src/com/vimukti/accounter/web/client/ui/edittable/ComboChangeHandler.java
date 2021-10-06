package com.vimukti.accounter.web.client.ui.edittable;

public interface ComboChangeHandler<R, C> {
	public void onChange(R row, C newValue);

	public void onAddNew(String text);
}
