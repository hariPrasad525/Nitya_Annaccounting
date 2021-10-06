package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TDSAcknowledgmentsReport extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ackNo;

	private int formType;

	private int quater;

	private int financialYearStart;

	private int financialYearEnd;

	private long date;

	public String getAckNo() {
		return ackNo;
	}

	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}

	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public int getQuater() {
		return quater;
	}

	public void setQuater(int quater) {
		this.quater = quater;
	}

	public int getFinancialYearStart() {
		return financialYearStart;
	}

	public void setFinancialYearStart(int financialYearStart) {
		this.financialYearStart = financialYearStart;
	}

	public int getFinancialYearEnd() {
		return financialYearEnd;
	}

	public void setFinancialYearEnd(int financialYearEnd) {
		this.financialYearEnd = financialYearEnd;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
