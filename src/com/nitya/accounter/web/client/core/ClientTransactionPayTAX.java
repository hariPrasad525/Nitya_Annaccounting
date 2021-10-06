package com.nitya.accounter.web.client.core;

public class ClientTransactionPayTAX implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long id;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	long taxAgency;

	/**
	 * The amount of Tax which we still have to pay.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are paying presently.
	 */
	double amountToPay;

	long taxReturn;

	ClientPayTAX payTAX;

	int version;

	ClientFinanceDate filedDate;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	public ClientFinanceDate getFiledDate() {
		return filedDate;
	}

	public void setFiledDate(ClientFinanceDate filedDate) {
		this.filedDate = filedDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the vatAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the taxDue
	 */
	public double getTaxDue() {
		return taxDue;
	}

	/**
	 * @param taxDue
	 *            the taxDue to set
	 */
	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	/**
	 * @return the amountToPay
	 */
	public double getAmountToPay() {
		return amountToPay;
	}

	/**
	 * @param amountToPay
	 *            the amountToPay to set
	 */
	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}

	/**
	 * @return the vatReturn
	 */
	public long getTAXReturn() {
		return taxReturn;
	}

	/**
	 * @param taxReturn
	 *            the vatReturn to set
	 */
	public void setTAXReturn(long taxReturn) {
		this.taxReturn = taxReturn;
	}

	/**
	 * @return the payVAT
	 */
	public ClientPayTAX getPayTAX() {
		return payTAX;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayTAX(ClientPayTAX payVAT) {
		this.payTAX = payVAT;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}


	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientTransactionPayTAX clone() {
		ClientTransactionPayTAX clientTransactionPayVATClone = (ClientTransactionPayTAX) this
				.clone();
		clientTransactionPayVATClone.payTAX = this.payTAX.clone();

		return clientTransactionPayVATClone;
	}
}