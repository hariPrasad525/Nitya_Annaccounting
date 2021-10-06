package com.vimukti.accounter.web.client.ui.grids.columns;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionUnitPriceColumn extends TransactionAmountColumn {

	@Override
	public void update(int index, ClientTransactionItem object, Double value) {
		object.setUnitPrice(value);

	}

	@Override
	public Double getValue(ClientTransactionItem object) {
		return object.getUnitPrice();
	}

}
