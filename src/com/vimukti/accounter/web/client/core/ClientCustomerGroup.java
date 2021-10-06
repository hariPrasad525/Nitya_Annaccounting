package com.vimukti.accounter.web.client.core;

public class ClientCustomerGroup implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String name;
	boolean isDefault;
	private int version;
	public ClientCustomerGroup() {

	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

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
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMER_GROUP;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientCustomerGroup clone() {
		ClientCustomerGroup customerGroup = (ClientCustomerGroup) this.clone();
		return customerGroup;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientCustomerGroup) {
			ClientCustomerGroup customerGroup = (ClientCustomerGroup) obj;
			return this.getID() == customerGroup.getID() ? true : false;
		}
		return false;
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
