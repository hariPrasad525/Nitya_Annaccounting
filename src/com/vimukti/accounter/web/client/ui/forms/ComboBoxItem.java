package com.vimukti.accounter.web.client.ui.forms;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ComboBoxItem extends FormItem<String> {

	ListBox listBox;
	LinkedHashMap<String, String> propertyValueHashMap;

	public ComboBoxItem(String title, String styleName) {
		super(title, styleName);
		listBox = new ListBox();
		listBox.addStyleName("listBox");
		this.add(listBox);
	}


	public void setValue(String value) {

		for (int i = 0; i < this.listBox.getItemCount(); i++) {
			if (this.listBox.getItemText(i).equals(value)) {
				this.listBox.setItemSelected(i, true);
			}
		}
	}

	@Override
	public String getValue() {
		return listBox.getValue(listBox.getSelectedIndex());
	}

	@Override
	public String getDisplayValue() {
		return listBox.getItemText(listBox.getSelectedIndex());

	}

	public void setValueMap(String... values) {
		this.listBox.clear();
		for (String valueString : values) {
			listBox.addItem(valueString);

		}
	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		this.listBox.addChangeHandler(changeHandler);
	}

	public void setValueMap(LinkedHashMap<String, String> propertyvalueMap) {
		this.propertyValueHashMap = propertyvalueMap;
		for (Entry<String, String> propertyValue : propertyvalueMap.entrySet()) {
			this.listBox.addItem(propertyValue.getKey(), propertyValue
					.getValue());
		}

	}

	public void setDefaultToFirstOption(boolean b) {

	}

	@Override
	public Widget getMainWidget() {

		return this.listBox;
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		listBox.setTitle(toolTip);
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		setValue(defaultValue);

	}
}
