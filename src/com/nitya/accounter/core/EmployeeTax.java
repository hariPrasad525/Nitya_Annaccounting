package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;

public class EmployeeTax extends CreatableObject implements IAccounterServerCore {
	
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
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTaxResidencyType() {
		return taxResidencyType;
	}

	public void setTaxResidencyType(int taxResidencyType) {
		this.taxResidencyType = taxResidencyType;
	}

	public int getTaxType() {
		return taxType;
	}

	public void setTaxType(int taxType) {
		this.taxType = taxType;
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
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages2 messages = Global.get().messages2();
		w.put(Global.get().messages().taxType(), this.taxType);
		w.put(messages.additionalAmount(), this.additionalAmount);
		w.put(messages.isSSNTaxable(), this.isSSNTaxable);
		w.put(messages.taxAllowances(), this.taxallowences);
		w.put(messages.taxFilingStatus(), this.taxFilingStatus);
		w.put(messages.taxResidencyType(), this.taxResidencyType);
		w.put(messages.taxFilingStatus(), this.taxFilingStatus);
		w.put(messages.taxUnemployement(), this.taxUnemployement);
	}

	@Override
	public void selfValidate() throws AccounterException {

	}

}
