package com.nitya.accounter.web.client.core;

public class ClientPriceLevel implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;

	long id;

	String name;
	double percentage;

	boolean isPriceLevelDecreaseByThisPercentage;

	boolean isDefault;

	public ClientPriceLevel() {

	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

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

	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the isPriceLevelDecreaseByThisPercentage
	 */
	public boolean isPriceLevelDecreaseByThisPercentage() {
		return isPriceLevelDecreaseByThisPercentage;
	}

	/**
	 * @param isPriceLevelDecreaseByThisPercentage
	 *            the isPriceLevelDecreaseByThisPercentage to set
	 */
	public void setPriceLevelDecreaseByThisPercentage(
			boolean isPriceLevelDecreaseByThisPercentage) {
		this.isPriceLevelDecreaseByThisPercentage = isPriceLevelDecreaseByThisPercentage;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.PRICE_LEVEL;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientPriceLevel clone() {
		ClientPriceLevel priceLevel = (ClientPriceLevel) this.clone();
		return priceLevel;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientPriceLevel) {
			ClientPriceLevel priceLevel = (ClientPriceLevel) obj;
			return this.getID() == priceLevel.getID() ? true : false;
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
