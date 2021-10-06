package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.FinanceDateField;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;

public class VendorImporter extends PayeeImporter<ClientVendor> {

	@Override
	public ClientVendor getData() {
		ClientVendor vendor = new ClientVendor();
		getData(vendor);
		vendor.setCreditLimit(getLong(messages.creditLimit()));
		vendor.setShippingMethod(getLong(messages.preferredShippingMethod()));
		vendor.setCustomFieldValues(getSetCusFieldList(messages.CustomField()));
		vendor.setName(getString("payeeName"));
		vendor.setFileAs(getString("payeeName"));
		vendor.setVendorNumber(getString("number"));
		vendor.setPayeeSince(getFinanceDate("payeeSince") == null ? new ClientFinanceDate()
				.getDate() : getFinanceDate("payeeSince").getDate());
		vendor.setBalanceAsOf(getFinanceDate(messages.balanceAsOf()) == null ? new ClientFinanceDate()
				.getDate() : getFinanceDate(messages.balanceAsOf()).getDate());
		vendor.setAddress(getSetAddressList(messages.address()));
		vendor.setContacts(getSetContactList(messages.contacts()));
		vendor.setMemo(getString(messages.notes()));
		vendor.setPaymentTerms(getLong(messages.paymentTerm()));
		vendor.setVendorGroup(getLong(messages.group()));
		return vendor;
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = super.getAllFields();
		fields.add(new StringField("payeeName", messages.payeeName(Global.get()
				.Vendor()), true));
		fields.add(new StringField("number", messages.number()));
		fields.add(new LongField(messages.creditLimit(), messages.creditLimit()));
		fields.add(new LongField(messages.preferredShippingMethod(), messages
				.preferredShippingMethod()));
		fields.add(new FinanceDateField("payeeSince", messages
				.payeeSince(Global.get().Vendor())));
		fields.add(new FinanceDateField(messages.balanceAsOf(), messages
				.balanceAsOf()));
		fields.add(new StringField(messages.address(), messages.address()));
		fields.add(new StringField(messages.contacts(), messages.contacts()));
		fields.add(new StringField(messages.notes(), messages.notes()));
		fields.add(new LongField(messages.paymentTerm(), messages.paymentTerm()));
		fields.add(new LongField(messages.group(), messages.group()));
		return fields;
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		// TODO Auto-generated method stub

	}
}
