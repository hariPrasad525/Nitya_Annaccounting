package com.vimukti.accounter.web.client.core;

public class ClientTAXItem extends ClientTAXItemGroup {

	// public static final int TAX_TYPE_TDS = 1;
	// public static final int TAX_TYPE_VAT = 2;
	// public static final int TAX_TYPE_TCS = 3;
	// public static final int TAX_TYPE_SALES = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long taxAgency;

	double taxRate;

	long vatReturnBox;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;

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

	/**
	 * @return the vatAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the vatRate
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate
	 *            the vatRate to set
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * @return the vatReturnBox
	 */
	public long getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(long vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.TAXITEM;
	}

	public ClientTAXItem clone() {
		ClientTAXItem taxItem = (ClientTAXItem) this.clone();
		return taxItem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientTAXItem) {
			ClientTAXItem taxItem = (ClientTAXItem) obj;
			return this.getID() == taxItem.getID() ? true : false;
		}
		return false;
	}
}
