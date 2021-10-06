package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientEmployeeTax implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int FILING_STATUS_SINGLE = 1;
	public static final int FILING_STATUS_MARRIED = 2;
	public static final int FILING_STATUS_MARRIED_JOINTLY = 3;
	public static final int FILING_STATUS_MARRIED_SEPARATE = 4;
	
	public static final int TAX_RESIDENCY_NON = 1;
	public static final int TAX_RESIDENCY = 2;

	public static final int TAX_TYPE_FEDERAL = 1;
	public static final int TAX_TYPE_STATE = 2;

	private int taxType;

	private int taxFilingStatus;
	private int taxallowences;
	private boolean isSSNTaxable;
	private boolean medicareTaxable;
	private boolean taxUnemployement;
	private double additionalAmount;
	private int taxResidencyType;

	int version;
	private long id;
	
	
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTaxType() {
		return taxType;
	}

	public void setTaxType(int taxType) {
		this.taxType = taxType;
	}

	public int getTaxResidencyType() {
		return taxResidencyType;
	}

	public void setTaxResidencyType(int taxResidencyType) {
		this.taxResidencyType = taxResidencyType;
	}

	public int getTaxFilingStatus() {
		return taxFilingStatus;
	}

	public void setTaxFilingStatus(int taxFilingStatus) {
		this.taxFilingStatus = taxFilingStatus;
	}

	public int getTaxallowences() {
		return taxallowences;
	}

	public void setTaxallowences(int taxallowences) {
		this.taxallowences = taxallowences;
	}

	public boolean isSSNTaxable() {
		return isSSNTaxable;
	}

	public void setIsSSNTaxable(boolean isSSNTaxable) {
		this.isSSNTaxable = isSSNTaxable;
	}

	public boolean isMedicareTaxable() {
		return medicareTaxable;
	}

	public void setMedicareTaxable(boolean medicareTaxable) {
		this.medicareTaxable = medicareTaxable;
	}

	public boolean isTaxUnemployement() {
		return taxUnemployement;
	}

	public void setTaxUnemployement(boolean taxUnemployement) {
		this.taxUnemployement = taxUnemployement;
	}

	public double getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(double additionalAmount) {
		this.additionalAmount = additionalAmount;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
      this.version = version;		
	}

	@Override
	public String getName() {
		return Global.get().messages2().employeeTax();
	}

	@Override
	public String getDisplayName() {
		return Global.get().messages2().employeeTax();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_TAX;
	}

	@Override
	public void setID(long id) {
      this.id = id;		
	}

	@Override
	public long getID() {
		return id;
	}

}
