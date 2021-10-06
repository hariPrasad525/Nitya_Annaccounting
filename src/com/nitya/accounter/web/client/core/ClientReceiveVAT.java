package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;

public class ClientReceiveVAT extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayFrom
	 */
	long depositIn;

	/**
	 * Bills Due On or Before.
	 */
	long returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	long vatAgency;

	double endingBalance;

	boolean isEdited = false;

	List<ClientTransactionReceiveVAT> transactionReceiveVAT;

	private String checkNumber;

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECEIVEVAT;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	/**
	 * @return the payFrom
	 */
	public long getDepositIn() {
		return depositIn;
	}

	/**
	 * @param DepositIn
	 *            the DepositIn to set
	 */

	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public long getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(long returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
	}

	/**
	 * @return the vatAgency
	 */
	public long getVatAgency() {
		return vatAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setVatAgency(long vatAgency) {
		this.vatAgency = vatAgency;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @param endingBalance
	 *            the endingBalance to set
	 */
	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	/**
	 * @return the isEdited
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @return the clientTransactionPayVAT
	 */
	public List<ClientTransactionReceiveVAT> getClientTransactionReceiveVAT() {
		return transactionReceiveVAT;
	}

	/**
	 * @param clientTransactionReceiveVAT
	 *            the clientTransactionReceiveVAT to set
	 */
	public void setClientTransactionReceiveVAT(
			List<ClientTransactionReceiveVAT> transactionReceiveVATList) {
		this.transactionReceiveVAT = transactionReceiveVATList;
	}

	public ClientReceiveVAT clone() {
		ClientReceiveVAT receiveVAT = (ClientReceiveVAT) this.clone();

		List<ClientTransactionIssuePayment> transactionIssuePayments = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayments.add(clientTransactionIssuePayment.clone());
		}
		receiveVAT.transactionIssuePayment = transactionIssuePayments;

		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		receiveVAT.transactionItems = transactionItems;

		List<ClientTransactionPayBill> transactionPayBills = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBills.add(clientTransactionPayBill.clone());
		}
		receiveVAT.transactionPayBill = transactionPayBills;

		List<ClientTransactionPayTAX> transactionPaySalesTaxs = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTaxs.add(clientTransactionPaySalesTax.clone());
		}
		receiveVAT.transactionPaySalesTax = transactionPaySalesTaxs;

		List<ClientTransactionReceivePayment> transactionReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePayments.add(clientTransactionReceivePayment
					.clone());
		}
		receiveVAT.transactionReceivePayment = transactionReceivePayments;

		List<ClientTransactionReceiveVAT> transactionReceiveVATs = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientTransactionReceiveVAT clientTransactionReceiveVAT : this.transactionReceiveVAT) {
			transactionReceiveVATs.add(clientTransactionReceiveVAT.clone());
		}
		receiveVAT.transactionReceiveVAT = transactionReceiveVATs;

		return receiveVAT;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public String getName() {
		return Global.get().messages().receiveTAX();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}