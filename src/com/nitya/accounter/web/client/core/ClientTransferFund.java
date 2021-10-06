package com.nitya.accounter.web.client.core;

public class ClientTransferFund extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long depositIn;

	long depositFrom;

	long cashBackAccount;

	String cashBackMemo;

	double cashBackAmount;

	/**
	 * @return the cashBackAccount
	 */
	public long getCashBackAccount() {
		return cashBackAccount;
	}

	/**
	 * @param cashBackAccount
	 *            the cashBackAccount to set
	 */
	public void setCashBackAccount(long cashBackAccount) {
		this.cashBackAccount = cashBackAccount;
	}

	/**
	 * @return the depositIn
	 */
	public long getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the cashBackMemo
	 */
	public String getCashBackMemo() {
		return cashBackMemo;
	}

	/**
	 * @param cashBackMemo
	 *            the cashBackMemo to set
	 */
	public void setCashBackMemo(String cashBackMemo) {
		this.cashBackMemo = cashBackMemo;
	}

	/**
	 * @return the cashBackAmount
	 */
	public double getCashBackAmount() {
		return cashBackAmount;
	}

	/**
	 * @param cashBackAmount
	 *            the cashBackAmount to set
	 */
	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public long getDepositFrom() {
		return depositFrom;
	}

	public void setDepositFrom(long depositFrom) {
		this.depositFrom = depositFrom;
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
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSFERFUND;
	}

	public ClientTransferFund clone() {
		ClientTransferFund clientMakeDepositClone = (ClientTransferFund) this
				.clone();

		return clientMakeDepositClone;
	}
}
