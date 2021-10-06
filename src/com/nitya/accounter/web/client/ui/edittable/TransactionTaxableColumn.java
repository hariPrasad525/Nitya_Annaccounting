package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientTransactionItem;

public class TransactionTaxableColumn extends
		CheckboxEditColumn<ClientTransactionItem> {

	@Override
	protected void onChangeValue(boolean value, ClientTransactionItem row) {
		row.setTaxable(value);
		getTable().update(row);
	}

	@Override
	public void render(IsWidget widget,
			RenderContext<ClientTransactionItem> context) {
		super.render(widget, context);
		CheckBox box = (CheckBox) widget;
		box.setValue(context.getRow().isTaxable());
	}

	@Override
	protected String getColumnName() {
		return messages.taxable();
	}

	@Override
	public IsWidget getHeader() {
		return new Label(getColumnName());
	}

	@Override
	public int getWidth() {
		return 60;
	}

	@Override
	public String getValueAsString(ClientTransactionItem row) {
		return "";
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
