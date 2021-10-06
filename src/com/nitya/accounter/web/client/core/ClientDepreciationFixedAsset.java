package com.nitya.accounter.web.client.core;

public class ClientDepreciationFixedAsset implements IAccounterCore {

	/**
	 * 
	 */
	private int version;
	private static final long serialVersionUID = 1L;
	long id;
	String accountName;
	double amount;
	String accumulatedDepreciationAccount;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccumulatedDepreciationAccount() {
		return accumulatedDepreciationAccount;
	}

	public void setAccumulatedDepreciationAccount(
			String accumulatedDepreciationAccount) {
		this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
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
		return AccounterCoreType.COMPANY_PREFERENCES;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientDepreciationFixedAsset clone() {
		ClientDepreciationFixedAsset depreciationFixedAsset = (ClientDepreciationFixedAsset) this
				.clone();
		return depreciationFixedAsset;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}
