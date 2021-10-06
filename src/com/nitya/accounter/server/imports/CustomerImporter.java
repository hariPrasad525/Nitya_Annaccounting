package com.nitya.accounter.server.imports;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.FinanceDateField;
import com.nitya.accounter.web.client.imports.LongField;
import com.nitya.accounter.web.client.imports.StringField;

public class CustomerImporter extends PayeeImporter<ClientCustomer> {

	@Override
	public ClientCustomer getData() {
		ClientCustomer customer = new ClientCustomer();
		getData(customer);
		customer.setName(getString("name"));
		customer.setFileAs(getString("name"));
		customer.setNumber(getString("number"));
		customer.setSalesPerson(getLong("salesPerson"));
		customer.setCreditRating(getLong(messages.creditRating()));
		customer.setPayeeSince(getFinanceDate("payeeSince") == null ? new ClientFinanceDate()
				.getDate() : getFinanceDate("payeeSince").getDate());
		customer.setBalanceAsOf(getFinanceDate("balanceAsof") == null ? new ClientFinanceDate()
				.getDate() : getFinanceDate("balanceAsof").getDate());
		customer.setAddress(getSetAddressList("adress"));
		customer.setContacts(getSetContactList(messages.contacts()));
		customer.setMemo(getString("memo"));
		customer.setPaymentTerm(getLong("paymentTerm"));
		customer.setCustomerGroup(getLong("group"));
		return customer;
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = super.getAllFields();
		fields.add(new StringField("name", messages.payeeName(Global.get()
				.Customer()), true));
		fields.add(new StringField("number", messages.number()));
		fields.add(new LongField("salesPerson", messages.salesPerson()));
		fields.add(new LongField(messages.creditRating(), messages
				.creditRating()));
		fields.add(new FinanceDateField("payeeSince", messages
				.payeeSince(Global.get().Customer()), true));
		fields.add(new FinanceDateField("balanceAsof", messages.balanceAsOf(),
				true));
		fields.add(new StringField("adress", messages.address()));
		fields.add(new StringField("contacts", messages.contacts()));
		fields.add(new StringField("memo", messages.notes()));
		fields.add(new LongField("paymentTerm", messages.paymentTerm()));
		fields.add(new LongField("group", messages.customergroup()));
		return fields;
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		// TODO Auto-generated method stub

	}
}
