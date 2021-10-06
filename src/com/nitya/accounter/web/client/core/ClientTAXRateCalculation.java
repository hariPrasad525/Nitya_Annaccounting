package com.nitya.accounter.web.client.core;

public class ClientTAXRateCalculation implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long id;

	/**
	 * The rate by which the total amount of the transaction is laid on.
	 */
	double vatAmount;
	double lineTotal;
	String vatItem;
	long transactionDate;
	long transactionItem;
	boolean isVATGroupEntry;

	ClientTAXAgency taxAgency;
	double rate;
	ClientVATReturnBox vatReturnBox;
	ClientAccount purchaseLiabilityAccount;
	ClientAccount salesLiabilityAccount;

	ClientTAXReturn vatReturn;

	private int version;

	public ClientTAXRateCalculation() {

	}

	/**
	 * @return the clientVATReturn
	 */
	public ClientTAXReturn getClientVATReturn() {
		return vatReturn;
	}

	/**
	 * @param clientVATReturn
	 *            the clientVATReturn to set
	 */
	public void setClientVATReturn(ClientTAXReturn clientVATReturn) {
		this.vatReturn = clientVATReturn;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the vatAmount
	 */
	public double getVatAmount() {
		return vatAmount;
	}

	/**
	 * @param vatAmount
	 *            the vatAmount to set
	 */
	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	/**
	 * @return the vatItem id=223774 pd=dhanush@123
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

	/**
	 * @return the transactionDate
	 */
	public long getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionItem
	 */
	public long getTransactionItem() {
		return transactionItem;
	}

	/**
	 * @param transactionItem
	 *            the transactionItem to set
	 */
	public void setTransactionItem(long transactionItem) {
		this.transactionItem = transactionItem;
	}

	/**
	 * @return the isVATGroupEntry
	 */
	public boolean isVATGroupEntry() {
		return isVATGroupEntry;
	}

	/**
	 * @param isVATGroupEntry
	 *            the isVATGroupEntry to set
	 */
	public void setVATGroupEntry(boolean isVATGroupEntry) {
		this.isVATGroupEntry = isVATGroupEntry;
	}


	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public ClientTAXAgency getTaxAgency() {
		return taxAgency;
	}

	public void setVatAgency(ClientTAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public ClientVATReturnBox getVatReturnBox() {
		return vatReturnBox;
	}

	public void setVatReturnBox(ClientVATReturnBox vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
	}

	public ClientAccount getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	public void setPurchaseLiabilityAccount(
			ClientAccount purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	public ClientAccount getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(ClientAccount salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	// public ClientVATRateCalculation(String vatItem,
	// String transactonItem) {
	//
	// this.transactionItem = transactonItem;
	// this.vatItem = vatItem;
	// this.vatAmount = transactonItem.getVATfraction();
	// this.transactionDate = transactionItem.getTransaction().getDate();
	//
	// }

	public ClientTAXRateCalculation clone() {
		ClientTAXRateCalculation taxRateCalculation = (ClientTAXRateCalculation) this
				.clone();
		taxRateCalculation.purchaseLiabilityAccount = this.purchaseLiabilityAccount
				.clone();
		taxRateCalculation.salesLiabilityAccount = this.salesLiabilityAccount
				.clone();
		taxRateCalculation.taxAgency = this.taxAgency.clone();
		taxRateCalculation.vatReturn = this.vatReturn.clone();
		taxRateCalculation.vatReturnBox = this.vatReturnBox.clone();
		return taxRateCalculation;
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
