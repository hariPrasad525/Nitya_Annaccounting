package com.nitya.accounter.web.client.core;

public class ClientVATCode implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long id;
	String name;
	String description;
	boolean isTaxable;
	boolean isActive;
	String VATItemGrpForPurchases;
	String VATItemGrpForSales;

	boolean isECSalesEntry;
	boolean isDefault;

	private int version;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getVATItemGrpForPurchases() {
		return VATItemGrpForPurchases;
	}

	public void setVATItemGrpForPurchases(String vATItemGrpForPurchases) {
		VATItemGrpForPurchases = vATItemGrpForPurchases;
	}

	public String getVATItemGrpForSales() {
		return VATItemGrpForSales;
	}

	public void setVATItemGrpForSales(String vATItemGrpForSales) {
		VATItemGrpForSales = vATItemGrpForSales;
	}

	/**
	 * @return the isECSalesEntry
	 */
	public boolean isECSalesEntry() {
		return isECSalesEntry;
	}

	/**
	 * @param isECSalesEntry
	 *            the isECSalesEntry to set
	 */
	public void setECSalesEntry(boolean isECSalesEntry) {
		this.isECSalesEntry = isECSalesEntry;
	}


	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAX_CODE;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public ClientVATCode clone() {
		ClientVATCode clientVATCodeClone = (ClientVATCode) this.clone();
		return clientVATCodeClone;
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
