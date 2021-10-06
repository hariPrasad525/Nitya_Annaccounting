package com.vimukti.accounter.web.client.core;

import java.util.List;

/**
 * 
 * @author vimukti10
 * 
 */

public class ClientStatement implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private ClientFinanceDate importtedDate;

	private ClientFinanceDate startDate;

	private ClientFinanceDate endDate;

	private double startBalance;

	private double closingBalance;

	private long account;

	private boolean isReconciled;

	private int version;

	private long companyId;

	private List<ClientStatementRecord> statementRecords;

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
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.STATEMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public ClientFinanceDate getImporttedDate() {
		return importtedDate;
	}

	public void setImporttedDate(ClientFinanceDate importtedDate) {
		this.importtedDate = importtedDate;
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public double getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public List<ClientStatementRecord> getStatementList() {
		return statementRecords;
	}

	public void setStatementList(List<ClientStatementRecord> statementList) {
		this.statementRecords = statementList;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public boolean isReconciled() {
		return isReconciled;
	}

	public void setReconciled(boolean isReconciled) {
		this.isReconciled = isReconciled;
	}

}
