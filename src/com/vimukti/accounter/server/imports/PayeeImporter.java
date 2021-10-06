package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientCustomFieldValue;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.imports.DoubleField;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;

public abstract class PayeeImporter<T> extends AbstractImporter<IAccounterCore> {

	protected void getData(ClientPayee payee) {
		payee.setCurrency(getCurrencyAsLong(messages.currency()));
		payee.setOpeningBalance(getDouble(messages.openingBalance()));
		payee.setFaxNo(getString(messages.fax()));
		payee.setPhoneNo(getString(messages.phone()));
		payee.setWebPageAddress(getString(messages.webPageAddress()));
		payee.setPaymentMethod(getString(messages.paymentMethod()) == null ? messages
				.cash() : getString(messages.paymentMethod()));
		payee.setVATRegistrationNumber(getString(messages.taxRegNo()));
		payee.setTAXCode(getLong(messages.taxCode()));
		payee.setBankAccountNo(getString(messages.bankAccountNumber()));
		payee.setBankName(getString(messages.bankName()));
		payee.setBankBranch(getString(messages.bankBranch()));
	}

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = new ArrayList<ImportField>();
		fields.add(new StringField(messages.currency(), messages.currency()));
		fields.add(new DoubleField(messages.openingBalance(), messages
				.openingBalances()));
		fields.add(new StringField(messages.fax(), messages.fax()));
		fields.add(new StringField(messages.phone(), messages.phone()));
		fields.add(new StringField(messages.webPageAddress(), messages
				.webPageAddress()));
		fields.add(new StringField(messages.paymentMethod(), messages
				.paymentMethod()));
		fields.add(new StringField(messages.taxRegNo(), messages.taxRegNo()));
		fields.add(new LongField(messages.taxCode(), messages.taxCode()));
		fields.add(new StringField(messages.bankAccountNumber(), messages
				.bankAccountNumber()));
		fields.add(new StringField(messages.bankName(), messages.bankName()));
		fields.add(new StringField(messages.bankBranch(), messages.bankBranch()));

		fields.add(new StringField(messages.contactName(), messages
				.contactName()));
		fields.add(new StringField(messages.title(), messages.title()));
		fields.add(new StringField(messages.businessPhone(), messages
				.businessPhone()));
		fields.add(new StringField(messages.email(), messages.email()));

		fields.add(new StringField(messages.streetAddress1(), messages
				.streetAddress1()));

		fields.add(new StringField(messages.address2(), messages.address2()));
		fields.add(new StringField(messages.city(), messages.city()));
		fields.add(new StringField(messages.countryRegion(), messages
				.countryRegion()));
		fields.add(new StringField(messages.stateOrProvince(), messages
				.stateOrProvince()));
		return fields;
	}

	protected Set<ClientCustomFieldValue> getSetCusFieldList(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

}
