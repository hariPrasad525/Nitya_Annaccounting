package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;

public class ReconcilCombo extends CustomCombo {

	public ReconcilCombo(String title) {
		super(title, false, 1, "ReconcilCombo");

		initCombo(getList());
	}

	public ArrayList<String> getList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(messages.findAndMatch());
		list.add(messages.create(""));
		list.add(messages.transfer());
		list.add(messages.comments());

		return list;
	}

	@Override
	protected String getDisplayName(Object object) {
		if (object != null)
			return object.toString() != null ? object.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(Object object, int col) {
		switch (col) {
		case 0:
			return object.toString();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.Reconcile();
	}

	@Override
	public void onAddNew() {
	}

}
