package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientListBox;

public class ListBoxTable extends AbstractDropDownTable<ClientListBox> {

	private List<ClientListBox> list;

	public ListBoxTable(List<ClientListBox> newData, boolean isAddNewReq) {
		super(newData, false);
	}

	@Override
	protected ClientListBox getAddNewRow() {
		ClientListBox payhead = new ClientListBox();
		return payhead;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientListBox> textColumn = new TextColumn<ClientListBox>() {

			@Override
			public String getValue(ClientListBox object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected void addNewItem(String text) {
	}

	@Override
	protected void addNewItem() {
		addNewItem("");
	}

	@Override
	protected boolean filter(ClientListBox t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientListBox value) {
		return value.getDisplayName();
	}
	
	public void setList(List<ClientListBox> list) {
		this.list = list;
	}

	@Override
	public List<ClientListBox> getTotalRowsData() {
		return list;
	}

}
