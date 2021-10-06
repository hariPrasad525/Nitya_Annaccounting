package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.CoreEvent;
import com.vimukti.accounter.web.client.util.CoreEventHandler;

public abstract class StringListDropDownTable extends CellTable<String> {

	protected AccounterMessages messages = Global.get().messages();
	private RowSelectHandler<String> rowSelectHandler;
	private List<String> data;
	private ListDataProvider<String> dataProvider;
	private SingleSelectionModel<String> singleSelectionModel;
	private boolean isClicked;

	public StringListDropDownTable(List<String> newData) {
		super(1000);
		this.data = new ArrayList<String>(newData);
		Type<CoreEventHandler<String>> type = CoreEvent.getType(getType());
		Accounter.getEventBus().addHandler(type,
				new CoreEventHandler<String>() {

					@Override
					public void onAdd(String obj) {
						reInitData();
					}

					@Override
					public void onDelete(String obj) {
						reInitData();
					}

					@Override
					public void onChange(String obj) {
						reInitData();
					}
				});

		dataProvider = new ListDataProvider<String>();
		dataProvider.setList(data);
		dataProvider.addDataDisplay(this);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		singleSelectionModel = new SingleSelectionModel<String>();
		singleSelectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String selectedObject = singleSelectionModel
						.getSelectedObject();
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
		data = new ArrayList<String>(getTotalRowsData());
		dataProvider.setList(data);
		dataProvider.refresh();
		getRowElement(0);
	}

	public abstract List<String> getTotalRowsData();

	/**
	 * Overide this method to listen to events
	 * 
	 * @return
	 */
	protected Class<?> getType() {
		return null;
	}

	private void sendSelectedObject(String selectedObject) {
		int indexOf = StringListDropDownTable.this.data.indexOf(selectedObject);
		if (rowSelectHandler != null) {
			rowSelectHandler.onRowSelect(selectedObject, isClicked);
		}
		isClicked = false;
	}

	private boolean clickFired;

	@Override
	protected void onBrowserEvent2(Event event) {
		if (event.getTypeInt() == Event.ONCLICK) {
			super.onBrowserEvent2(event);
			isClicked = true;
			String selectedObject = singleSelectionModel.getSelectedObject();
			if (!clickFired) {
				sendSelectedObject(selectedObject);
			}
			clickFired = true;
			return;
		}
		super.onBrowserEvent2(event);
	}

	public void initColumns() {
		TextColumn<String> nameColumn = new TextColumn<String>() {

			@Override
			public String getValue(String object) {
				return object;
			}
		};
		this.addColumn(nameColumn);
	}

	public void addRowSelectHandler(RowSelectHandler<String> handler) {
		rowSelectHandler = handler;
	}

	public void updateSelection(String string) {
		for (String t : data) {
			if (filter(t, string)) {
				if (t == singleSelectionModel.getSelectedObject()) {
					sendSelectedObject(t);
				} else {
					singleSelectionModel.setSelected(t, true);
				}
				break;
			}
		}
	}

	protected boolean filter(String t, String string) {
		if (t.toLowerCase().startsWith(string)) {
			return true;
		}
		return false;
	}

	public String getFilteredValue(String text) {
		if (text.isEmpty()) {
			return null;
		}
		for (String t : data) {
			if (filter(t, text)) {
				return t;
			}
		}
		return null;
	}

	public void upKeyPress() {
		if (data.size() == 0) {
			return;
		}
		String selectedObject = singleSelectionModel.getSelectedObject();
		int selectedIndex = data.indexOf(selectedObject);
		if (selectedIndex == 0) {
			selectedIndex = data.size();
		}
		String t2 = data.get(--selectedIndex);
		singleSelectionModel.setSelected(t2, true);
	}

	public void downKeyPress() {
		if (data.size() == 0) {
			return;
		}
		String selectedObject = singleSelectionModel.getSelectedObject();
		int selectedIndex = data.indexOf(selectedObject);
		selectedIndex++;
		if (selectedIndex == data.size()) {
			selectedIndex = 0;
		}
		String t2 = data.get(selectedIndex);
		singleSelectionModel.setSelected(t2, true);
	}

	protected void selectRow(String result) {
		boolean isExists = false;
		for (String coreObj : dataProvider.getList()) {
			if (result.equals(coreObj)) {
				isExists = true;
			}
		}
		if (!isExists) {
			dataProvider.getList().add(result);
		}
		selectRow(result, true);
	}

	protected void selectRow(String result, boolean needToUpdate) {
		isClicked = needToUpdate;
		clickFired = false;
		singleSelectionModel.setSelected(result, true);
	}

	private native void ensureVisibleImpl(Element scroll, int offset) /*-{
		scroll.scrollTop = offset - scroll.offsetHeight / 2;
	}-*/;
}
