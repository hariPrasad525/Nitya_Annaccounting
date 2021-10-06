package com.nitya.accounter.web.client.ui.forms;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.ListBox;

public class SelectItem extends FormItem<String> {

	ListBox listBox;
	LinkedHashMap<String, String> propertyValueHashMap;

	@Override
	public String getValue() {
		if (listBox.getSelectedIndex() >= 0)
			return listBox.getValue(listBox.getSelectedIndex());
		return null;
	}

	@Override
	public String getDisplayValue() {
		return listBox.getItemText(listBox.getSelectedIndex() != -1 ? listBox
				.getSelectedIndex() : 0);

	}

	public SelectItem(String title, String styleName) {
		super(title, styleName);
		listBox = new ListBox();
		listBox.addStyleName("selectitem");
		this.add(listBox);
	}

	@Override
	public void setDefaultValue(String string) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getItemText(i).equalsIgnoreCase(string)) {
				listBox.setItemSelected(i, true);
				return;
			}
		}
	}

	public void setSelected(String string) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getItemText(i).equalsIgnoreCase(string)) {
				listBox.setItemSelected(i, true);
				return;
			}
		}
	}

	public void setHint(String string) {
		// NOTHING TO DO.
	}

	public void setMultiple(boolean b) {
		this.listBox.setMultipleSelect(b);

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		listBox.setTitle(toolTip);
	}

	@Override
	public void setValue(String value) {
		int valuesCount = this.listBox.getItemCount();
		for (int i = 0; i < valuesCount; i++) {
			if (this.listBox.getItemText(i).equals(value))
				this.listBox.setSelectedIndex(i);
		}
		if (this.listBox.getSelectedIndex() == -1) {
			this.listBox.addItem(value);
			this.listBox.setSelectedIndex(0);

		}
	}

	public void setItemValue(Object value) {
		int valuesCount = this.listBox.getItemCount();
		for (int i = 0; i < valuesCount; i++) {
			if (this.listBox.getValue(i).equals(value)) {
				this.listBox.setSelectedIndex(i);
				return;
			}
		}
	}

	public void setValueMap(String... values) {

		listBox.clear();
		for (final String valueString : values) {
			listBox.addItem(valueString);

		}
	}

	public void setValueMap(LinkedHashMap<String, String> propertyvalueMap) {
		listBox.clear();
		this.propertyValueHashMap = propertyvalueMap;
		int index = 0;
		for (Entry<String, String> propertyValue : propertyvalueMap.entrySet()) {
			this.listBox.addItem(propertyValue.getKey(), propertyValue
					.getValue());
			index++;
		}

	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		this.listBox.addChangeHandler(changeHandler);

	}

	public void setDefaultToFirstOption(boolean b) {
		if (b) {
			// this.listBox.removeItem(0);
			this.listBox.setSelectedIndex(0);
		}
	}

	@Override
	public FocusWidget getMainWidget() {
		return this.listBox;
	}

	// @Override
	// public boolean validate() {
	// return true;
	//
	// }

	public int getSelectedIndex() {
		return this.listBox.getSelectedIndex() != -1 ? this.listBox
				.getSelectedIndex() : 0;

	}

	public String getValue(int index) {
		return this.listBox.getItemText(index);
	}

	public void addChangedHandler(ChangeHandler changeHandler) {
		this.listBox.addChangeHandler(changeHandler);
	}
}
