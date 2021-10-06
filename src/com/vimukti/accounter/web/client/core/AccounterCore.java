package com.vimukti.accounter.web.client.core;

public class AccounterCore implements IAccounterCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long id;
	private AccounterCoreType type;

	private int version;

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
		return this.type;
	}

	public void setObjectType(AccounterCoreType type) {
		this.type = type;
	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public AccounterCore clone() {
		AccounterCore accounterCore = (AccounterCore) this.clone();
		return accounterCore;

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
