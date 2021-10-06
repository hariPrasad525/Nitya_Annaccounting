package com.vimukti.accounter.web.client.core;

public class ClientFiscalYear implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final int STATUS_OPEN = 1;
	public static final int STATUS_CLOSE = 2;

	long id;

	long startDate;

	long endDate;

	int status = STATUS_OPEN;

	boolean isCurrentFiscalYear = Boolean.FALSE;

	long previousStartDate;

	int version;

	boolean isDefault;

	public ClientFiscalYear() {
	}

	/**
	 * @return the startDate
	 */
	public ClientFinanceDate getStartDate() {
		return new ClientFinanceDate(startDate);
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public ClientFinanceDate getEndDate() {
		return new ClientFinanceDate(endDate);
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the isCurrentFiscalYear
	 */
	public boolean getIsCurrentFiscalYear() {
		return isCurrentFiscalYear;
	}

	/**
	 * @param isCurrentFiscalYear
	 *            the isCurrentFiscalYear to set
	 */
	public void setIsCurrentFiscalYear(boolean isCurrentFiscalYear) {
		this.isCurrentFiscalYear = isCurrentFiscalYear;
	}

	/**
	 * @return the previousStartDate
	 */
	public ClientFinanceDate getPreviousStartDate() {
		return new ClientFinanceDate(previousStartDate);
	}

	/**
	 * @param previousStartDate
	 *            the previousStartDate to set
	 */
	public void setPreviousStartDate(long previousStartDate) {
		this.previousStartDate = previousStartDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

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

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FISCALYEAR;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientFiscalYear clone() {
		ClientFiscalYear fiscalYear = (ClientFiscalYear) this.clone();
		return fiscalYear;

	}

}
