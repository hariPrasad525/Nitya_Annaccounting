package com.nitya.accounter.web.client.ui.grids.columns;

import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.nitya.accounter.web.client.core.ClientTAXCode;

public class VatComboCell implements Cell<ClientTAXCode> {

	@Override
	public boolean dependsOnSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getConsumedEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handlesSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientTAXCode value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientTAXCode value, NativeEvent event,
			ValueUpdater<ClientTAXCode> valueUpdater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			ClientTAXCode value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientTAXCode value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientTAXCode value) {
		// TODO Auto-generated method stub

	}

}
