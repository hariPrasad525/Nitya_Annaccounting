package com.vimukti.accounter.web.client.core.reports;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class BaseReport implements IsSerializable {
	
	private ClientFinanceDate startDate;
	
	private ClientFinanceDate endDate;
	
	private String dateRange;

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param dateRange the dateRange to set
	 */
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	/**
	 * @return the dateRange
	 */
	public String getDateRange() {
		return dateRange;
	}

}
