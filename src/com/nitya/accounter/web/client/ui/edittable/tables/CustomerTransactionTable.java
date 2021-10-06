package com.nitya.accounter.web.client.ui.edittable.tables;

import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

/**
 * This class is used for every Customer Transactions
 * 
 * @author vimukti04
 * 
 */
public abstract class CustomerTransactionTable extends AbstractTransactionTable {

	public CustomerTransactionTable(int rowsPerObject, boolean needDiscount,
			ICurrencyProvider currencyProvider) {
		super(rowsPerObject, needDiscount, true, currencyProvider);
	}

}
