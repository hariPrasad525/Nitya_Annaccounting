package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class TransactionAmountColumn extends
		AmountColumn<ClientTransactionItem> {

	public TransactionAmountColumn(ICurrencyProvider currencyProvider,boolean updateFromGUI) {
		super(currencyProvider,updateFromGUI);
	}
}
