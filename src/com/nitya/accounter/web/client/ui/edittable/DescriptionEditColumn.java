package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientTransactionItem;

public class DescriptionEditColumn extends
		TextAreaEditColumn<ClientTransactionItem> {

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
	public String getValueAsString(ClientTransactionItem row) {
		return "";
	}

}
