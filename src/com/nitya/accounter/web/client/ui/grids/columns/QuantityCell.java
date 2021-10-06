package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.nitya.accounter.web.client.core.ClientQuantity;

public class QuantityCell extends AbstractEditableCell<ClientQuantity,ClientQuantity> {

	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientQuantity value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBrowserEvent(Context context, Element parent,
			ClientQuantity value, NativeEvent event,
			ValueUpdater<ClientQuantity> valueUpdater) {
		// TODO Auto-generated method stub
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			ClientQuantity value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub
		
	}

	

	

}
