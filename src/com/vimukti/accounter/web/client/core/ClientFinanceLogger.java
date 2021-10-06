package com.vimukti.accounter.web.client.core;

public class ClientFinanceLogger implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public ClientFinanceLogger() {

	}

	long id;
	long createdDate;
	String createdBy;
	String description;
	String logMessge;
	private int version;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogMessge() {
		return logMessge;
	}

	public void setLogMessge(String logMessge) {
		this.logMessge = logMessge;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
		return AccounterCoreType.FINANCELOG;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientFinanceLogger clone() {
		ClientFinanceLogger financeLogger = (ClientFinanceLogger) this.clone();
		return financeLogger;

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
