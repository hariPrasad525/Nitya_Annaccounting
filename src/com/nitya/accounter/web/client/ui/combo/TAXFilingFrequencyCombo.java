package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

public class TAXFilingFrequencyCombo extends CustomCombo<String> {

	public TAXFilingFrequencyCombo(String title) {
		super(title, false, 1,"TAXFilingFrequencyCombo");
		initCombo(getTAXFilingFrequencies());
	}

	private List<String> getTAXFilingFrequencies() {
		List<String> list = new ArrayList<String>();
		list.add(messages.monthly());
		list.add(messages.quarterly());
		list.add(messages.halfYearly());
		list.add(messages.yearly());
		return list;
	}

	@Override
	protected String getDisplayName(String object) {
		return object;
	}

	@Override
	protected String getColumnData(String object, int col) {
		return object;
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub
	}

	public int getSelectedFrequency() {
		return comboItems.indexOf(selectedObject);
	}

	public String getSelectedFrequencyAsString() {
		return selectedObject;
	}

}
