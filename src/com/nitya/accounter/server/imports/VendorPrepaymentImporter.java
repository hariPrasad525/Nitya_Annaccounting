package com.nitya.accounter.server.imports;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.DoubleField;
import com.nitya.accounter.web.client.imports.StringField;

public class VendorPrepaymentImporter extends
		TransactionImporter<ClientPayBill> {

	@Override
	public ClientPayBill getData() {
		ClientTransaction vendorPrepayment = new ClientPayBill();
		getTransactionData(vendorPrepayment);
		((ClientPayBill) vendorPrepayment)
				.setVendor(getLong(messages.vendor()));
		((ClientPayBill) vendorPrepayment).setPayFrom(getLong(messages
				.payFrom()));
		((ClientPayBill) vendorPrepayment)
				.setTotal(getDouble(messages.amount()));
		((ClientPayBill) vendorPrepayment).setPaymentMethod(getString(messages
				.paymentMethod()));
		((ClientPayBill) vendorPrepayment).setCheckNumber(getString(messages
				.checkNo()));
		((ClientPayBill) vendorPrepayment).setMemo(getString(messages.memo()));
		return ((ClientPayBill) vendorPrepayment);
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> list = super.getAllFields();
		list.add(new StringField(messages.vendor(), messages.payTo(), true));
		list.add(new StringField(messages.payFrom(), messages.payFrom(), true));
		list.add(new DoubleField(messages.amount(), messages.amount(), true));
		list.add(new StringField(messages.paymentMethod(), messages
				.paymentMethod(), true));
		list.add(new StringField(messages.checkNo(), messages.checkNo()));
		list.add(new StringField(messages.memo(), messages.memo()));
		return list;
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		// TODO Auto-generated method stub

	}

}
