package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionVatColumn extends TransactionAmountColumn {

	public TransactionVatColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider, false);
	}

	@Override
	protected Double getAmount(ClientTransactionItem row) {
		return row.getVATfraction();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, Double value) {
		double taxAmount = (row.getLineTotal() * value) / 100;
		row.setVATfraction(taxAmount);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	protected String getColumnName() {
		return messages.tax();
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
