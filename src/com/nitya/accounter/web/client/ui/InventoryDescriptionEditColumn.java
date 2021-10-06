package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.nitya.accounter.web.client.ui.edittable.TextAreaEditColumn;

public class InventoryDescriptionEditColumn extends
		TextAreaEditColumn<ClientInventoryAssemblyItem> {

	@Override
	protected String getValue(ClientInventoryAssemblyItem row) {
		return row.getDiscription();
	}

	@Override
	protected void setValue(ClientInventoryAssemblyItem row, String value) {
		row.setDescription(value);
	}

	@Override
	protected String getColumnName() {
		return messages.description();
	}

	@Override
	public int getWidth() {
		return -1;
	}


	@Override
	public int insertNewLineNumber() {
		return 3;
	}

	@Override
	public String getValueAsString(ClientInventoryAssemblyItem row) {
		return "";
	}


}
