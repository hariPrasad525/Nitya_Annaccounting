package com.nitya.accounter.web.client.core;


public class ClientStockAdjustment extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long wareHouse;

	private long adjustmentAccount;

	public ClientStockAdjustment() {
		super();
		setType(TYPE_STOCK_ADJUSTMENT);
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.STOCK_ADJUSTMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(long wareHouse) {
		this.wareHouse = wareHouse;
	}

	public void setAdjustmentAccount(long accountId) {
		this.adjustmentAccount = accountId;
	}

	/**
	 * @return the adjustmentAccount
	 */
	public long getAdjustmentAccount() {
		return adjustmentAccount;
	}

}
