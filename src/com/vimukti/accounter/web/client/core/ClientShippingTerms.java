package com.vimukti.accounter.web.client.core;

public class ClientShippingTerms implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;

	long id;

	String name;
	String description;

	boolean isDefault;

	public ClientShippingTerms() {

	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param ame
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDisplayName() {

		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.SHIPPING_TERM;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientShippingTerms clone() {
		ClientShippingTerms shippingTerms = (ClientShippingTerms) this.clone();
		return shippingTerms;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientShippingTerms) {
			ClientShippingTerms shippingTerms = (ClientShippingTerms) obj;
			return this.getID() == shippingTerms.getID() ? true : false;
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
