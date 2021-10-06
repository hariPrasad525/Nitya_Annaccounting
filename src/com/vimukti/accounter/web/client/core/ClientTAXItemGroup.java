/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author vimukti5
 * 
 *         Parent Class for ClientVatItem and ClientVatGroup
 * 
 */
public class ClientTAXItemGroup implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	/**
	 * Name of the Tax Group which is unique for every TaxGroup
	 */
	String name;

	/**
	 * Description about the VAT item (About its VAT codes, rates, VAT agncies
	 * etc).
	 */
	String description;

	boolean isActive;
	boolean isSalesType;
	boolean isPercentage;
	boolean isDefault;

	private int version;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.TAX_ITEM_GROUP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {

		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;

	}

	/**
	 * @return the isSalesType
	 */
	public boolean isSalesType() {
		return isSalesType;
	}

	/**
	 * @param isSalesType
	 *            the isSalesType to set
	 */
	public void setSalesType(boolean isSalesType) {
		this.isSalesType = isSalesType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isPercentage
	 */
	public boolean isPercentage() {
		return isPercentage;
	}

	/**
	 * @param isPercentage
	 *            the isPercentage to set
	 */
	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public ClientTAXItemGroup clone() {
		ClientTAXItemGroup taxItemGroup = (ClientTAXItemGroup) this.clone();
		return taxItemGroup;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientTAXItemGroup) {
			ClientTAXItemGroup taxItemGroup = (ClientTAXItemGroup) obj;
			return this.getID() == taxItemGroup.getID() ? true : false;
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
