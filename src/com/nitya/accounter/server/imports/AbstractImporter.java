package com.nitya.accounter.server.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.imports.BooleanField;
import com.nitya.accounter.web.client.imports.DoubleField;
import com.nitya.accounter.web.client.imports.FinanceDateField;
import com.nitya.accounter.web.client.imports.Integer2Field;
import com.nitya.accounter.web.client.imports.LongField;
import com.nitya.accounter.web.client.imports.StringField;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class AbstractImporter<T extends IAccounterCore> implements
		Importer<T> {

	private List<ImportField> fields = new ArrayList<ImportField>();

	private Map<String, String> importedData = new HashMap<String, String>();

	AccounterMessages messages = Global.get().messages();

	private String dateFormat;

	private long companyId;

	private Company comapny;

	public AbstractImporter() {
		fields = getAllFields();
	}

	public abstract List<ImportField> getAllFields();

	protected ImportField getFieldByName(String fieldName) {
		for (ImportField field : getFields()) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	protected abstract void validate(List<AccounterException> exceptions);

	@Override
	public List<AccounterException> loadData(Map<String, String> data) {
		List<AccounterException> exceptions = new ArrayList<AccounterException>();
		for (ImportField field : fields) {
			String value = data.get(field.getColumnName());
			if (field.isFinanceDate()) {
				((FinanceDateField) field).setDateFormate(dateFormat);
			}
			if (value != null) {
				field.validate(value);
			}
		}
		validate(exceptions);
		return exceptions;

	}

	protected String getString(String fieldName) {
		return ((StringField) getFieldByName(fieldName)).getValue();
	}

	protected long getAccountByNumberOrName(String accountNumber,
			boolean isAccountName) {
		String accountNoOrName = getString(accountNumber);
		if (accountNoOrName != null && (!accountNoOrName.isEmpty())) {
			return new FinanceTool().getAccountByNumberOrName(getCompanyId(),
					accountNoOrName, isAccountName);
		}
		return 0;
	}

	protected ClientFinanceDate getFinanceDate(String fieldName) {
		return ((FinanceDateField) getFieldByName(fieldName)).getValue();
	}

	protected Long getLong(String fieldName) {
		return ((LongField) getFieldByName(fieldName)).getValue();
	}

	protected Double getDouble(String fieldName) {
		return ((DoubleField) getFieldByName(fieldName)).getValue();
	}

	protected Set<ClientAddress> getSetAddressList(String fieldName) {
		HashSet<ClientAddress> contactSet = new HashSet<ClientAddress>();
		contactSet.add(getAddresData());
		return contactSet;
	}

	protected Set<ClientContact> getSetContactList(String fieldName) {
		HashSet<ClientContact> contactSet = new HashSet<ClientContact>();
		contactSet.add(getContactData());
		return contactSet;
	}

	protected boolean getBoolean(String fieldName) {
		return ((BooleanField) getFieldByName(fieldName)).getValue();
	}

	protected int getInteger(String fieldName) {
		return ((Integer2Field) getFieldByName(fieldName)).getValue();
	}

	protected long getCurrencyAsLong(String currency) {
		return new FinanceTool().getCurrencyIdByName(currency, getCompanyId());
	}

	protected long getPayeeByName(String payeeName) {
		String payee = getString(payeeName);
		if (payee != null && (!payee.isEmpty())) {
			return new FinanceTool().getPayeeByName(getCompanyId(), payee);
		}
		return 0;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<ImportField> fields) {
		this.fields = fields;
	}

	/**
	 * @return the fields
	 */
	public List<ImportField> getFields() {
		return fields;
	}

	@Override
	public void updateFields(Map<String, String> importMap) {
		for (ImportField field : fields) {
			String string = importMap.get(field.getName());
			field.setColumnName(string);
		}
	}

	protected ClientContact getContactData() {
		ClientContact contact = new ClientContact();
		contact.setName(getString(messages.contactName()));
		contact.setTitle(getString(messages.title()));
		contact.setBusinessPhone(getString(messages.businessPhone()));
		contact.setEmail(getString(messages.email()));
		return contact;
	}

	protected ClientAddress getAddresData() {
		ClientAddress address = new ClientAddress();
		address.setAddress1(getString(messages.streetAddress1()));
		address.setStreet(getString(messages.address2()));
		address.setCity(getString(messages.city()));
		address.setCountryOrRegion(getString(messages.countryRegion()));
		address.setStateOrProvinence(getString(messages.stateOrProvince()));
		return address;
	}

	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	protected long getCompanyId() {
		return companyId;
	}

	@Override
	public void setCompanyId(long CompanyId) {
		this.companyId = CompanyId;
	}

	public Company getCompanyById(long companyId) {
		return new FinanceTool().getCompany(companyId);
	}

}
