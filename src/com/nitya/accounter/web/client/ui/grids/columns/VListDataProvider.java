package com.nitya.accounter.web.client.ui.grids.columns;

import java.util.ArrayList;

import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.core.ListListener;

public class VListDataProvider<T> extends ListDataProvider<T> implements
		ListListener<T> {

	/**
	 * Creates new Instance
	 */
	public VListDataProvider() {
	}

	public VListDataProvider(ArrayList<T> ArrayList) {
		setList(ArrayList);
	}

	public void setList(ArrayList<T> ArrayList) {
//		ArrayList.addListener(this);
		this.getList().addAll(ArrayList);
	}

	@Override
	public void onAdd(T e) {
		this.getList().add(e);
		this.refresh();
	}

	@Override
	public void onRemove(T e) {
		this.getList().remove(e);
		this.refresh();
	}
}
