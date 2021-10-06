package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */

public class Statement extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FinanceDate importtedDate;

	private FinanceDate startDate;

	private FinanceDate endDate;

	private double startBalance;

	private double closingBalance;

	private Account account;

	private Reconciliation reconciliation;

	private boolean isReconciled;

	private List<StatementRecord> statementRecords = new ArrayList<StatementRecord>();

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANK_STATEMENT;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

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

	public FinanceDate getImporttedDate() {
		return importtedDate;
	}

	public void setImporttedDate(FinanceDate importtedDate) {
		this.importtedDate = importtedDate;
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

	public List<StatementRecord> getStatementRecord() {
		return statementRecords;
	}

	public void setStatementRecord(List<StatementRecord> statementRecord) {
		statementRecords = statementRecord;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		for (StatementRecord statementRecord : getStatementRecord()) {
			statementRecord.setStatement(this);
		}
		account.setStatementBalance(getClosingBalance());
		account.setStatementLastDate(getImporttedDate());
		session.saveOrUpdate(account);
		return super.onSave(session);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Reconciliation getReconciliation() {
		return reconciliation;
	}

	public void setReconciliation(Reconciliation reconciliation) {
		this.reconciliation = reconciliation;
	}

	public boolean isReconciled() {
		return isReconciled;
	}

	public void setReconciled(boolean isReconciled) {
		this.isReconciled = isReconciled;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
