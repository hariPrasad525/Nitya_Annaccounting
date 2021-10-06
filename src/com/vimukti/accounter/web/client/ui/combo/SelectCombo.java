package com.vimukti.accounter.web.client.ui.combo;

import java.util.Arrays;

public class SelectCombo extends CustomCombo<String> {

	public SelectCombo(String title) {
		super(title, false, 1, "SelectCombo");
		super.setToolTip(messages.selectWhichWeHaveInOurCompany(title));
	}

	public SelectCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "SelectCombo");
		super.setToolTip(messages.selectWhichWeHaveInOurCompany(title));
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	public void onAddNew() {

	}

	public void setDefaultToFirstOption(boolean b) {
		if (b) {
			this.setComboItem(comboItems.get(0));
		}
	}

	@Override
	protected String getDisplayName(String object) {
		if (object != null)
			return object.toString() != null ? object.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(String object, int col) {
		switch (col) {
		case 0:
			return object.toString();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {

		super.setValue(value);
	}

	/**
	 * 
	 */
	public void setValueMap(String... strings) {
		comboItems.addAll(Arrays.asList(strings));
	}

	public int getSelectedIndex() {
		return comboItems.indexOf(selectedObject);
	}

	@Override
	protected boolean shouldSortData() {
		return false;
	}
}
