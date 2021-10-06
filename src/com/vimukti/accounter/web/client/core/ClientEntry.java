package com.vimukti.accounter.web.client.core;

public class ClientEntry implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_FINANCIAL_ACCOUNT = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_CUSTOMER = 3;

	public static final int TYPE_VAT = 4;

	public static final int JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT = 1;

	public static final int JOURNAL_ENTRY_TYPE_VENDOR = 2;

	public static final int JOURNAL_ENTRY_TYPE_CUSTOMER = 3;

	long id;

	int type;

	int journalEntryType;

	long account;

	long vendor;

	long customer;

	long taxCode;

	// String taxCode;

	String memo;

	double debit = 0D;

	double credit = 0D;

	long journalEntry;

	String vatItem;

	long entryDate;

	double total;

	private int version;

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	public long getEntryDate() {
		return entryDate;
	}

	/**
	 * @return the vatItem
	 */
	public String getVatItem() {
		return vatItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setVatItem(String vatItem) {
		this.vatItem = vatItem;
	}

	public void setEntryDate(long entryDate) {

		this.entryDate = entryDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getJournalEntryType() {
		return journalEntryType;
	}

	public void setJournalEntryType(int journalEntryType) {
		this.journalEntryType = journalEntryType;
	}

	/**
	 * @return the taxCode
	 */
	public long getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode
	 *            the taxCode to set
	 */
	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public long getVendor() {
		return vendor;
	}

	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	public long getCustomer() {
		return customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}

	// public String getTaxCode() {
	// return taxCode;
	// }
	//
	// public void setTaxCode(String taxCode) {
	// this.taxCode = taxCode;
	// }

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public long getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(long journalEntry) {
		this.journalEntry = journalEntry;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	// public String getVATAgency() {
	// return VATAgency;
	// }
	//
	// public void setVATAgency(String agency) {
	// VATAgency = agency;
	// }

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ENTRY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientEntry clone() {
		ClientEntry entry = (ClientEntry) this.clone();
		return entry;

	}

	@Override
	public int getVersion() {

		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}
}
