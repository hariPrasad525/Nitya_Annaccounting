package com.vimukti.accounter.web.client.core;

public class ClientTAXCode implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	String name;
	String description;
	boolean isTaxable;
	boolean isActive;
	long TAXItemGrpForPurchases;
	long TAXItemGrpForSales;

	boolean isDefault;
	private long taxAgency;

	private int version;

	private double salesTaxRate;
	private double purchaseTaxRate;

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

	public long getTAXItemGrpForPurchases() {
		return TAXItemGrpForPurchases;
	}

	public void setTAXItemGrpForPurchases(long vATItemGrpForPurchases) {
		TAXItemGrpForPurchases = vATItemGrpForPurchases;
	}

	public long getTAXItemGrpForSales() {
		return TAXItemGrpForSales;
	}

	public void setTAXItemGrpForSales(long taxItemGroup) {
		TAXItemGrpForSales = taxItemGroup;
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

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public ClientTAXCode clone() {
		ClientTAXCode taxCode = (ClientTAXCode) this.clone();
		return taxCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientTAXCode) {
			ClientTAXCode taxCode = (ClientTAXCode) obj;
			return this.getID() == taxCode.getID() ? true : false;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public double getSalesTaxRate() {
		return salesTaxRate;
	}

	public void setSalesTaxRate(double salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}

	public double getPurchaseTaxRate() {
		return purchaseTaxRate;
	}

	public void setPurchaseTaxRate(double purchaseTaxRate) {
		this.purchaseTaxRate = purchaseTaxRate;
	}
}
