package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ETDsFilingData implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int formNo;
	private int quarter;
	private long fromDate;
	private long toDate;
	private int startYear;
	private int endYear;
	private String panList;
	private String codeList;
	private String remarkList;
	private String grossingUpList;

	public ETDsFilingData(int formNo, int quater, long fromDate, long toDate,
			int startYear, int endYear, String panList, String codeList,
			String remarkList, String grossingUpList) {
		setFormNo(formNo);
		setQuarter(quater);
		setFromDate(fromDate);
		setToDate(toDate);
		setStartYear(startYear);
		setEndYear(endYear);
		setPanList(panList);
		setCodeList(codeList);
		setRemarkList(remarkList);
		setGrossingUpList(grossingUpList);
	}

	public ETDsFilingData() {
	}

	public int getFormNo() {
		return formNo;
	}

	public void setFormNo(int formNo) {
		this.formNo = formNo;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public String getPanList() {
		return panList;
	}

	public void setPanList(String panList) {
		this.panList = panList;
	}

	public String getCodeList() {
		return codeList;
	}

	public void setCodeList(String codeList) {
		this.codeList = codeList;
	}

	public String getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(String remarkList) {
		this.remarkList = remarkList;
	}

	public String getGrossingUpList() {
		return grossingUpList;
	}

	public void setGrossingUpList(String grossingUpList) {
		this.grossingUpList = grossingUpList;
	}
}
