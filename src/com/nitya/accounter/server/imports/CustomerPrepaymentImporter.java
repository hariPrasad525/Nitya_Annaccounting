package com.nitya.accounter.server.imports;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomerPrePayment;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.DoubleField;
import com.nitya.accounter.web.client.imports.StringField;

public class CustomerPrepaymentImporter extends
		TransactionImporter<ClientCustomerPrePayment> {

	@Override
	public ClientCustomerPrePayment getData() {
		ClientTransaction custPrepayment = new ClientCustomerPrePayment();
		getTransactionData(custPrepayment);
		((ClientCustomerPrePayment) custPrepayment)
				.setCustomer(getLong(messages.customer()));
		((ClientCustomerPrePayment) custPrepayment)
				.setDepositIn(getLong(messages.depositIn()));
		((ClientCustomerPrePayment) custPrepayment).setTotal(getDouble(messages
				.amount()));
		((ClientCustomerPrePayment) custPrepayment)
				.setPaymentMethod(getString(messages.paymentMethod()));
		((ClientCustomerPrePayment) custPrepayment)
				.setCheckNumber(getString(messages.checkNo()));
		((ClientCustomerPrePayment) custPrepayment).setMemo(getString(messages
				.memo()));
		return ((ClientCustomerPrePayment) custPrepayment);
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> list = super.getAllFields();
		list.add(new StringField(messages.customer(), Global.get().Customer(),
				true));
		list.add(new StringField(messages.depositIn(), messages.depositIn(),
				true));
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
