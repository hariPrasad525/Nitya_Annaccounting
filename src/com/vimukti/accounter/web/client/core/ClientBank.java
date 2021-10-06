package com.vimukti.accounter.web.client.core;

public class ClientBank implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String name;

	int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * null;
	 * 
	 * @return the id
	 */

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

	@Override
	public String getDisplayName() {

		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.BANK;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientBank clone() {
		ClientBank clientBank = (ClientBank) this.clone();
		return clientBank;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClientBank) {
			ClientBank bank = (ClientBank) obj;
			if (this.getID() == bank.getID()
					&& this.getName().equals(bank.getName()))
				return true;
		} else {
			return false;
		}
		return false;
	}
}
