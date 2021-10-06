package com.vimukti.accounter.web.client.core;

public class ClientPaymentTerms implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DUE_NONE = 0;
	public static final int DUE_CURRENT_MONTH = 1;
	public static final int DUE_CURRENT_QUARTER = 2;
	public static final int DUE_CURRENT_HALF_YEAR = 3;
	public static final int DUE_CURRENT_YEAR = 4;
	public static final int DUE_CURRENT_SIXTY = 5;

	int version;

	long id;

	String name;
	String description;
	int due = ClientPaymentTerms.DUE_NONE;
	int dueDays = ClientPaymentTerms.DUE_NONE;

	double discountPercent;
	int ifPaidWithIn;
	boolean isDefault;
	boolean isDateDriven;

	public ClientPaymentTerms() {

	}

	public ClientPaymentTerms(ClientCompany company, String name,
			String description, int ifPaidWithIn, double discountPercent,
			int due, int dueDays) {

		this.name = name;
		this.description = description;
		this.ifPaidWithIn = ifPaidWithIn;
		this.discountPercent = discountPercent;
		this.due = due;
		this.dueDays = dueDays;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the due
	 */
	public int getDue() {
		return due;
	}

	/**
	 * @param due
	 *            the due to set
	 */
	public void setDue(int due) {
		this.due = due;
	}

	/**
	 * @return the dueDays
	 */
	public int getDueDays() {
		return dueDays;
	}

	/**
	 * @param dueDays
	 *            the dueDays to set
	 */
	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	/**
	 * @return the ifPaidWithIn
	 */
	public int getIfPaidWithIn() {
		return ifPaidWithIn;
	}

	/**
	 * @param ifPaidWithIn
	 *            the ifPaidWithIn to set
	 */
	public void setIfPaidWithIn(int ifPaidWithIn) {
		this.ifPaidWithIn = ifPaidWithIn;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.PAYMENT_TERM;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isDateDriven() {
		return isDateDriven;
	}

	public void setDateDriven(boolean isDateDriven) {
		this.isDateDriven = isDateDriven;
	}

	public ClientPaymentTerms clone() {
		ClientPaymentTerms paymentTerms = (ClientPaymentTerms) this.clone();
		return paymentTerms;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientPaymentTerms) {
			ClientPaymentTerms paymentTerms = (ClientPaymentTerms) obj;
			return this.getID() == paymentTerms.getID() ? true : false;
		}
		return false;
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
