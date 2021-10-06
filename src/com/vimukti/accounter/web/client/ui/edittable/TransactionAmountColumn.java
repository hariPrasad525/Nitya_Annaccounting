package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class TransactionAmountColumn extends
		AmountColumn<ClientTransactionItem> {

	public TransactionAmountColumn(ICurrencyProvider currencyProvider,boolean updateFromGUI) {
		super(currencyProvider,updateFromGUI);
	}
}
