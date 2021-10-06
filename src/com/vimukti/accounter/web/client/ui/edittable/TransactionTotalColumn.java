package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionTotalColumn extends AmountColumn<ClientTransactionItem> {

	public TransactionTotalColumn(ICurrencyProvider currencyProvider,
			boolean updateFromGUI) {
		super(currencyProvider, updateFromGUI);
	}

	@Override
	protected Double getAmount(ClientTransactionItem row) {

		return row
				.getLineTotal();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, Double value) {
		row.setLineTotal(value);
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("total");
		textBox.setEnabled(false);
	}

	@Override
	protected String getColumnName() {
		return messages.total();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}

	@Override
	public String getValueAsString(ClientTransactionItem row) {
		return getColumnName()+" : "+getValue(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
