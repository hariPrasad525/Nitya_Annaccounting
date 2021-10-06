package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class LiveTaxRate implements IAccounterServerCore, Lifecycle {
	
	private static final long serialVersionUID = 1L;
	

	public static final String FILING_STATUS_SINGLE = "Single";
	public static final String FILING_STATUS_MARRIED = "Married";
	public static final String FILING_STATUS_MARRIED_HEADOFHOUSE = "HeadOfHouse";
	public static final String FILING_STATUS_MARRIED_SEPARATE = "MarriedSeparate";
	
	public static final String TAX_TYPE_FEDERAL = "Federal";
	public static final String TAX_TYPE_STATE = "State";
	public static final String TAX_TYPE_SSN = "Social Security";
	public static final String TAX_TYPE_MEDICARE = "Medicare";
	public static final String TAX_TYPE_DISABILITY = "Disablity";
	public static final String TAX_TYPE_TRAINING = "Training";
	public static final String TAX_TYPE_UNEMPLOYEMENT = "Unemployement";

	String name;
	String taxFilingStatus;
	String taxType;
	String country;
	String year;

	long id;
	long standardDeductions;

	List<LiveTaxRateRange> rates;
	
	transient private boolean isOnSaveProccessed;

	private int version;

	public LiveTaxRate() {

	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	@Override
	public boolean onDelete(Session s) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {

	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return false;
	}

	@Override
	public long getID() {
		return id;
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
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();
		w.put(messages.type(), this.name).gap();
		w.put(messages.name(), this.taxFilingStatus).gap();
		w.put(messages.email(), this.country);
	}

	public String getTaxFilingStatus() {
		return taxFilingStatus;
	}

	public void setTaxFilingStatus(String taxFilingStatus) {
		this.taxFilingStatus = taxFilingStatus;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<LiveTaxRateRange> getRates() {
		return rates;
	}

	public void setRates(List<LiveTaxRateRange> rates) {
		this.rates = rates;
	}

	public long getStandardDeductions() {
		return standardDeductions;
	}

	public void setStandardDeductions(long standardDeductions) {
		this.standardDeductions = standardDeductions;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public void selfValidate() {
		
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		return true;
	}

}
