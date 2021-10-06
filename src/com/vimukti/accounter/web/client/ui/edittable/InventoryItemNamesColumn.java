package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class InventoryItemNamesColumn extends
		TextEditColumn<ClientTransactionItem> {

	@Override
	protected String getValue(ClientTransactionItem row) {
		return row.getDescription();
	}

	@Override
	protected void setValue(ClientTransactionItem row, String value) {
		row.setDescription(value);
	}

	@Override
	protected String getColumnName() {
		return messages.name();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}

	@Override
	public String getValueAsString(ClientTransactionItem row) {
		return getValue(row).toString();
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

}
