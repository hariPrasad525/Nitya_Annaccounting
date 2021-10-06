package com.nitya.accounter.web.client.ui.grids.columns;

import com.nitya.accounter.web.client.core.ClientTransactionItem;

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
