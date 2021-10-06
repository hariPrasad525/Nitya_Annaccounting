package com.vimukti.accounter.web.client.core;

public class ClientTransactionCreditsAndPayments implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	long id;

	long date;

	String memo;

	double amountToUse;

//	ClientTransactionReceivePayment transactionReceivePayment;

//	ClientTransactionPayBill transactionPayBill;

	long creditsAndPayments;

	boolean isVoid = false;

	int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public double getAmountToUse() {
		return amountToUse;
	}

	public void setAmountToUse(double amountToUse) {
		this.amountToUse = amountToUse;
	}

//	public ClientTransactionReceivePayment getTransactionReceivePayment() {
//		return transactionReceivePayment;
//	}
//
//	public void setTransactionReceivePayment(
//			ClientTransactionReceivePayment transactionReceivePayment) {
//		this.transactionReceivePayment = transactionReceivePayment;
//	}
//
//	public ClientTransactionPayBill getTransactionPayBill() {
//		return transactionPayBill;
//	}
//
//	public void setTransactionPayBill(
//			ClientTransactionPayBill transactionPayBill) {
//		this.transactionPayBill = transactionPayBill;
//	}

	/**
	 * @return the creditsAndPayments
	 */
	public long getCreditsAndPayments() {
		return creditsAndPayments;
	}

	/**
	 * @param creditsAndPayments
	 *            the creditsAndPayments to set
	 */

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setCreditsAndPayments(
			long creditsAnsPayments) {
		this.creditsAndPayments = creditsAnsPayments;

	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return null;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	// private ClientTransaction getTransaction() {
	//
	// if (this.transactionReceivePaymentId != null) {
	// return this.transactionReceivePaymentId.getReceivePayment();
	// } else {
	//
	// return this.transactionPayBillId.getPayBill();
	// }
	//
	// }
	//
	// public void makeVoid() {
	// this.creditsAndPaymentsId.updateBalance(getTransaction(), -amountToUse);
	// this.setAmountToUse(0.0);
	// }

	public ClientTransactionCreditsAndPayments clone() {
		ClientTransactionCreditsAndPayments clientTransactionCreditsAndPaymentsClone = (ClientTransactionCreditsAndPayments) this
				.clone();
		// clientTransactionCreditsAndPaymentsClone.transactionReceivePayment =
		// this.transactionReceivePayment
		// .clone();
		// clientTransactionCreditsAndPaymentsClone.transactionPayBill =
		// this.transactionPayBill
		// .clone();
		clientTransactionCreditsAndPaymentsClone.creditsAndPayments = this.creditsAndPayments;
		return clientTransactionCreditsAndPaymentsClone;
	}
}
