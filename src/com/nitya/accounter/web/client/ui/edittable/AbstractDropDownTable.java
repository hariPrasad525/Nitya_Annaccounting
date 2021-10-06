package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.util.CoreEvent;
import com.nitya.accounter.web.client.util.CoreEventHandler;
import com.google.gwt.view.client.SingleSelectionModel;

public abstract class AbstractDropDownTable<T extends IAccounterCore> extends
		CellTable<T> {

	protected AccounterMessages messages = Global.get().messages();
	private RowSelectHandler<T> rowSelectHandler;
	private List<T> data;
	private List<T> fullData;
	private ListDataProvider<T> dataProvider;
	private SingleSelectionModel<T> singleSelectionModel;
	private boolean isClicked;
	private boolean isAddNewReq;

	public AbstractDropDownTable(List<T> newData, boolean isAddNewReq) {
		super(1000);
		this.data = new ArrayList<T>(newData);
		fullData = new ArrayList<T>(data);
		this.isAddNewReq = isAddNewReq;
		if (isAddNewReq) {
			T newRow = getAddNewRow();
			data.add(0, newRow);
		}
		Type<CoreEventHandler<T>> type = CoreEvent.getType(getType());
		Accounter.getEventBus().addHandler(type, new CoreEventHandler<T>() {

			@Override
			public void onAdd(T obj) {
				reInitData();
			}

			@Override
			public void onDelete(T obj) {
				reInitData();
			}

			@Override
			public void onChange(T obj) {
				reInitData();
			}
		});

		dataProvider = new ListDataProvider<T>();
		dataProvider.setList(data);
		dataProvider.addDataDisplay(this);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		singleSelectionModel = new SingleSelectionModel<T>();
		singleSelectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				T selectedObject = singleSelectionModel.getSelectedObject();
				if (!clickFired) {
					sendSelectedObject(selectedObject);
				} else {
					clickFired = false;
				}
				int index = data.indexOf(selectedObject);
				if (index < 0) {
					return;
				}
				TableRowElement element = getRowElement(index);
				int offsetHeight = element.getOffsetTop();
				ensureVisibleImpl(getParent().getElement(), offsetHeight);
			}

		});
		setSelectionModel(singleSelectionModel);
		// setWidth("100%");
		initColumns();
	}

	protected void reInitData() {
		fullData = new ArrayList<T>(getTotalRowsData());
		reInitData(fullData);
	}

	private void reInitData(List<T> totalRowsData) {
		data = new ArrayList<T>(totalRowsData);
		if (isAddNewReq) {
			data.add(0, getAddNewRow());
		}
		dataProvider.setList(data);
		dataProvider.refresh();
		if (isAddNewReq) {
			getRowElement(0);
		}
	}

	public abstract List<T> getTotalRowsData();

	/**
	 * Overide this method to listen to events
	 * 
	 * @return
	 */
	protected Class<?> getType() {
		return null;
	}

	private void sendSelectedObject(T selectedObject) {
		int indexOf = AbstractDropDownTable.this.data.indexOf(selectedObject);
		if (rowSelectHandler != null) {
			if (isAddNewReq) {
				if (indexOf == 0) {
					rowSelectHandler.onRowSelect(null, isClicked);
				} else if (indexOf > 0) {
					rowSelectHandler.onRowSelect(selectedObject, isClicked);
				}
			} else {
				rowSelectHandler.onRowSelect(selectedObject, isClicked);
			}
		}
		isClicked = false;
	}

	private boolean clickFired;

	@Override
	protected void onBrowserEvent2(Event event) {
		if (event.getTypeInt() == Event.ONCLICK) {
			super.onBrowserEvent2(event);
			isClicked = true;
			T selectedObject = singleSelectionModel.getSelectedObject();
			if (!clickFired) {
				sendSelectedObject(selectedObject);
			}
			clickFired = true;
			return;
		}
		super.onBrowserEvent2(event);
	}

	protected abstract T getAddNewRow();

	public abstract void initColumns();

	public void addRowSelectHandler(RowSelectHandler<T> handler) {
		rowSelectHandler = handler;
	}

	public void updateSelection(String string) {
		List<T> filteredData = new ArrayList<T>();
		if (string.isEmpty()) {
			filteredData.addAll(fullData);
		} else {
			for (T t : fullData) {
				if (filter(t, string)) {
					filteredData.add(t);
				}
			}
			if (!filteredData.isEmpty()) {
				T t = filteredData.get(0);
				if (t == singleSelectionModel.getSelectedObject()) {
					sendSelectedObject(t);
				} else {
					singleSelectionModel.setSelected(t, true);
				}
			}
		}
		reInitData(filteredData);
	}

	protected abstract boolean filter(T t, String string);

	protected abstract String getDisplayValue(T value);

	public T getFilteredValue(String text) {
		if (text.isEmpty()) {
			return null;
		}
		for (T t : data) {
			if (filter(t, text)) {
				if (isAddNewReq && data.indexOf(t) == 0) {
					return null;
				}
				return t;
			}
		}
		return null;
	}

	public void upKeyPress() {
		if (data.size() == 0) {
			return;
		}
		T selectedObject = singleSelectionModel.getSelectedObject();
		int selectedIndex = data.indexOf(selectedObject);
		if (selectedIndex == 0) {
			selectedIndex = data.size();
		}
		T t2 = data.get(--selectedIndex);
		singleSelectionModel.setSelected(t2, true);
	}

	public void downKeyPress() {
		if (data.size() == 0) {
			return;
		}
		T selectedObject = singleSelectionModel.getSelectedObject();
		int selectedIndex = data.indexOf(selectedObject);
		selectedIndex++;
		if (selectedIndex == data.size()) {
			selectedIndex = 0;
		}
		T t2 = data.get(selectedIndex);
		singleSelectionModel.setSelected(t2, true);
	}

	/**
	 * When select Add New Item
	 */
	protected abstract void addNewItem(String text);

	protected abstract void addNewItem();

	protected void selectRow(T result) {
		boolean isExists = false;
		for (IAccounterCore coreObj : dataProvider.getList()) {
			if (result.getID() == coreObj.getID()) {
				isExists = true;
			}
		}
		if (!isExists) {
			dataProvider.getList().add(result);
		}
		selectRow(result, true);
	}

	protected void selectRow(T result, boolean needToUpdate) {
		isClicked = needToUpdate;
		clickFired = false;
		singleSelectionModel.setSelected(result, true);
	}

	private native void ensureVisibleImpl(Element scroll, int offset) /*-{
		scroll.scrollTop = offset - scroll.offsetHeight / 2;
	}-*/;
}
