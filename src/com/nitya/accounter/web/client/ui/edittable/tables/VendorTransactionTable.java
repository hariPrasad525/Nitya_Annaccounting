package com.nitya.accounter.web.client.ui.edittable.tables;

import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class VendorTransactionTable extends AbstractTransactionTable {

	public VendorTransactionTable(int rowsPerObject,boolean needDiscount,
			ICurrencyProvider currencyProvider) {
		this(rowsPerObject,needDiscount, false,currencyProvider);
	}

	public VendorTransactionTable(int rowsPerObject, boolean needDiscount,
			boolean isCustomerAllowedToAdd, ICurrencyProvider currencyProvider) {
		super(rowsPerObject,needDiscount, false, isCustomerAllowedToAdd, currencyProvider);
	}
}
