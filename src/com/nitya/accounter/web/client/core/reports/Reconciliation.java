package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class Reconciliation extends BaseReport implements IsSerializable,
		Serializable {

	private static final long serialVersionUID = 1L;
	private long accountId;
	private String accountName;
	private int accountType;
	private ClientFinanceDate reconcilationdate;
	private ClientFinanceDate statementdate;

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public ClientFinanceDate getReconcilationdate() {
		return reconcilationdate;
	}

	public void setReconcilationdate(ClientFinanceDate reconcilationdate) {
		this.reconcilationdate = reconcilationdate;
	}

	public ClientFinanceDate getStatementdate() {
		return statementdate;
	}

	public void setStatementdate(ClientFinanceDate statementdate) {
		this.statementdate = statementdate;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

}
