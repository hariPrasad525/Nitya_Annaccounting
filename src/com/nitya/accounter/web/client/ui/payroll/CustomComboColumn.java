package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.core.ClientListBox;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.nitya.accounter.web.client.ui.edittable.ComboColumn;
import com.nitya.accounter.web.client.ui.edittable.ListBoxTable;

public abstract class CustomComboColumn<T> extends ComboColumn<T, ClientListBox> {

	List<ClientListBox> valuesBoxs = new ArrayList<ClientListBox>();

	ClientListBox value;

	public CustomComboColumn(List<ClientListBox> values) {
		valuesBoxs = values;
	}

	public CustomComboColumn(Map<Integer, String> values) {
		for (Integer key : values.keySet()) {
			valuesBoxs.add(new ClientListBox(values.get(key), key));
		}
	}

	@Override
	protected ClientListBox getValue(T row) {
		return value;
	}

	@Override
	protected void setValue(T row, ClientListBox newValue) {
		value = newValue;
	}

	@Override
	public String getValueAsString(T row) {
		return value != null ? value.getDisplayName() : "";
	}

	@Override
	public AbstractDropDownTable<ClientListBox> getDisplayTable(T row) {
		return new ListBoxTable(valuesBoxs, false);
	}

	@Override
	public int getWidth() {
		return 200;
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

}
