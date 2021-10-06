package com.nitya.accounter.server.imports;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEnterBill;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.imports.FinanceDateField;
import com.nitya.accounter.web.client.imports.StringField;

public class EnterBillImporter extends TransactionImporter<ClientEnterBill> {

	@Override
	public ClientEnterBill getData() {
		ClientTransaction enterBill = new ClientEnterBill();
		getTransactionData(enterBill);
		((ClientEnterBill) enterBill).setVendor(getLong(messages.supplier()));
		((ClientEnterBill) enterBill).setContact(getContactData());
		((ClientEnterBill) enterBill)
				.setPhone(getString(messages.phoneNumber()));
		((ClientEnterBill) enterBill).setDueDate(getFinanceDate(
				messages.dueDate()).getDate());
		((ClientEnterBill) enterBill).setDeliveryDate(getFinanceDate(
				messages.deliveryDate()).getDate());
		((ClientEnterBill) enterBill).setMemo(messages.memo());

		return (ClientEnterBill) enterBill;
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> listFields = super.getAllFields();
		listFields.add(new StringField(messages.supplier(), messages
				.payeeName(Global.get().Vendor()), true));
		listFields.add(new StringField(messages.contactName(), messages
				.contactName()));
		listFields.add(new StringField(messages.title(), messages.title()));
		listFields.add(new StringField(messages.businessPhone(), messages
				.businessPhone()));
		listFields.add(new StringField(messages.email(), messages.email()));
		listFields
				.add(new StringField(messages.phoneNumber(), messages.phone()));
		listFields.add(new FinanceDateField(messages.dueDate(), messages
				.dueDate()));
		listFields.add(new FinanceDateField(messages.deliveryDate(), messages
				.deliveryDate()));
		listFields.add(new StringField(messages.memo(), messages.memo()));

		return super.getAllFields();
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		// TODO Auto-generated method stub

	}
}
