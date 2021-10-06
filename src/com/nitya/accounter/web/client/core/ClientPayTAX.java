package com.nitya.accounter.web.client.core;

import java.util.List;

public class ClientPayTAX extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long payFrom;

	long billsDueOnOrBefore;

	long taxAgency;

	// double amount;

	// boolean isVoid = false;

	boolean isEdited = false;

	List<ClientTransactionPayTAX> transactionPayTAX;

	private String checkNumber;

	public ClientPayTAX() {

	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public long getPayFrom() {
		return this.payFrom;
	}

	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	public long getBillsDueOnOrBefore() {
		return billsDueOnOrBefore;
	}

	public void setBillsDueOnOrBefore(long billsDueOnOrBefore) {
		this.billsDueOnOrBefore = billsDueOnOrBefore;
	}

	public long getTaxAgency() {
		return this.taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	public List<ClientTransactionPayTAX> getTransactionPayTax() {
		return transactionPayTAX;
	}

	public void setTransactionPayTax(
			List<ClientTransactionPayTAX> transactionPaySalesTax) {
		this.transactionPayTAX = transactionPaySalesTax;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_TAX;
	}

	public ClientPayTAX clone() {
		ClientPayTAX clientPaySalesTaxClone = (ClientPayTAX) this.clone();
		return clientPaySalesTaxClone;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
}