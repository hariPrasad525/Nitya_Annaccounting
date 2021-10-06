package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ClientPayee implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_NONE = 0;

	public static final int TYPE_CUSTOMER = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_TAX_AGENCY = 4;

	public static final int TYPE_EMPLOYEE = 3;

	String name;

	long payeeSince;

	String fileAs;

	long id;

	int type;

	int version;

	long date;

	double balance;

	double currencyFactor = 1;

	String bankAccountNo;
	String bankName;
	String bankBranch;
	String routingNumber;
	String panNumber;
	String cstNumber;
	String serviceTaxRegistrationNumber;
	String tinNumber;
	private long currency;

	Set<ClientAddress> address = new HashSet<ClientAddress>();

	Set<ClientPhone> phoneNumbers = new HashSet<ClientPhone>();
	Set<ClientFax> faxNumbers = new HashSet<ClientFax>();
	Set<ClientEmail> emails = new HashSet<ClientEmail>();
	Set<ClientContact> contacts = new HashSet<ClientContact>();
	protected Set<ClientLocation> locations = new HashSet<ClientLocation>();
	Set<ClientCustomFieldValue> customFieldValues = new HashSet<ClientCustomFieldValue>();
	String webPageAddress;
	boolean isActive = Boolean.TRUE;
	String memo;
	String paymentMethod;

	private String phoneNo;

	private String faxNo;

	protected double openingBalance = 0D;

	private HashMap<String, String> payeeFields = new HashMap<String, String>();

	// UK variables
	// boolean isEUVATExemptPayee;
	String VATRegistrationNumber;
	long TAXCode;
	long TAXItem;

	public ClientPayee() {
		// TODO Auto-generated constructor stub
	}

	public ClientPayee(long currency) {
		this.currency = currency;
	}

	public long getTaxItemCode() {
		return TAXItem;
	}

	public void setTaxItemCode(long taxItemCode) {
		TAXItem = taxItemCode;
	}

	boolean isOpeningBalanceEditable = Boolean.TRUE;

	private String email;
	boolean isDefault;

	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the vATRegistrationNumber
	 */
	public String getVATRegistrationNumber() {
		return VATRegistrationNumber;
	}

	/**
	 * @param registrationNumber
	 *            the vATRegistrationNumber to set
	 */
	public void setVATRegistrationNumber(String registrationNumber) {
		VATRegistrationNumber = registrationNumber;
	}

	/**
	 * @return the vATCode
	 */
	public long getTAXCode() {
		return TAXCode;
	}

	/**
	 * @param code
	 *            the vATCode to set
	 */
	public void setTAXCode(long code) {
		TAXCode = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileAs() {
		return fileAs;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getBalance() {
		return balance;
	}

	/**
	 * @return the id
	 */

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isCustomer() {
		return this != null && this instanceof ClientCustomer;
	}

	public boolean isVendor() {
		return this != null && this instanceof ClientVendor;
	}

	// public boolean isTaxAgency() {
	// return this != null && this instanceof ClientTaxAgency;
	// }

	public Set<ClientAddress> getAddress() {
		return address;
	}

	public void setAddress(Set<ClientAddress> address) {
		this.address = address;
	}

	public Set<ClientPhone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<ClientPhone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Set<ClientFax> getFaxNumbers() {
		return faxNumbers;
	}

	public void setFaxNumbers(Set<ClientFax> faxNumbers) {
		this.faxNumbers = faxNumbers;
	}

	public Set<ClientEmail> getEmails() {
		return emails;
	}

	public void setEmails(Set<ClientEmail> emails) {
		this.emails = emails;
	}

	public Set<ClientContact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ClientContact> contacts) {
		this.contacts = contacts;
	}

	public void addContact(ClientContact contact) {
		this.contacts.add(contact);
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public long getPayeeSince() {
		return this.payeeSince;
	}

	public void setPayeeSince(long payeeSince) {
		this.payeeSince = payeeSince;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	
	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getCstNumber() {
		return cstNumber;
	}

	public void setCstNumber(String cstNumber) {
		this.cstNumber = cstNumber;
	}

	public String getServiceTaxRegistrationNumber() {
		return serviceTaxRegistrationNumber;
	}

	public void setServiceTaxRegistrationNumber(
			String serviceTaxRegistrationNumber) {
		this.serviceTaxRegistrationNumber = serviceTaxRegistrationNumber;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public ClientPayee clone() {
		ClientPayee payee = (ClientPayee) this.clone();
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		for (ClientAddress clientAddress : this.address) {
			addresses.add(clientAddress.clone());
		}
		payee.address = address;

		Set<ClientContact> contacts = new HashSet<ClientContact>();
		for (ClientContact clientContact : this.contacts) {
			contacts.add(clientContact.clone());
		}
		payee.contacts = contacts;

		Set<ClientEmail> emails = new HashSet<ClientEmail>();
		for (ClientEmail clientEmail : this.emails) {
			emails.add(clientEmail.clone());
		}
		payee.emails = emails;

		Set<ClientFax> faxes = new HashSet<ClientFax>();
		for (ClientFax clientFax : this.faxNumbers) {
			faxes.add(clientFax.clone());
		}
		payee.faxNumbers = faxes;

		Set<ClientPhone> phones = new HashSet<ClientPhone>();
		for (ClientPhone clientPhone : this.phoneNumbers) {
			phones.add(clientPhone.clone());
		}
		payee.phoneNumbers = phones;

		return payee;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientPayee) {
			ClientPayee payee = (ClientPayee) obj;
			return this.getID() == payee.getID() ? true : false;
		}
		return false;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	/**
	 * @return the currencyFactor
	 */
	public double getCurrencyFactor() {
		return currencyFactor;
	}

	/**
	 * @param currencyFactor
	 *            the currencyFactor to set
	 */
	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public Set<ClientCustomFieldValue> getCustomFieldValues() {
		Set<ClientCustomFieldValue> newCustomFieldValues = new HashSet<ClientCustomFieldValue>();
		for (ClientCustomFieldValue clientCustomFieldValue : customFieldValues) {
			if (clientCustomFieldValue.getCustomField() != 0) {
				newCustomFieldValues.add(clientCustomFieldValue);
			}
		}
		setCustomFieldValues(newCustomFieldValues);
		return customFieldValues;
	}

	public void setCustomFieldValues(
			Set<ClientCustomFieldValue> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	/**
	 * @return the payeeFields
	 */
	public HashMap<String, String> getPayeeFields() {
		return payeeFields;
	}

	/**
	 * @param payeeFields
	 *            the payeeFields to set
	 */
	public void setPayeeFields(HashMap<String, String> payeeFields) {
		this.payeeFields = payeeFields;
	}

}
