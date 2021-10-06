package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author vimukti10
 * 
 */
public class ClientStatementRecord implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientFinanceDate statementDate;

	private long id;

	private String description;

	private String referenceNumber;

	private double spentAmount;

	private double receivedAmount;

	private double closingBalance;
	private double adjustmentAmount;
	private int version;
	private long companyId;
	private boolean isMatched;
	private List<ClientTransaction> transactionLists = new ArrayList<ClientTransaction>();
	/*
	 * Name of payee.
	 */
	private String payeeName;
	/*
	 * Description about the bankFees adjustment.
	 */
	private String bankDescription;
	/*
	 * Tax code for this transaction.
	 */
	private long taxCode;
	/*
	 * Amount which is used for reconcile.
	 */
	private double bankFeesAdjustmentAmt;

	private long bankFeesAdjustmentAcc;

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getbankDescription() {
		return bankDescription;
	}

	public void setbankDescription(String description) {
		this.bankDescription = description;
	}

	public long getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(long l) {
		this.taxCode = l;
	}

	public double getBankFeesAdjustmentAmt() {
		return bankFeesAdjustmentAmt;
	}

	public void setBankFeesAdjustmentAmt(double bankFeesAdjustmentAmt) {
		this.bankFeesAdjustmentAmt = bankFeesAdjustmentAmt;
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
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.STATEMENTRECORD;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public ClientFinanceDate getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(ClientFinanceDate statementDate) {
		this.statementDate = statementDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public double getSpentAmount() {
		return spentAmount;
	}

	public void setSpentAmount(double spentAmount) {
		this.spentAmount = spentAmount;
	}

	public double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public List<ClientTransaction> getTransactionsLists() {
		return transactionLists;
	}

	public void setTransactionsLists(List<ClientTransaction> transactionLists) {
		this.transactionLists = transactionLists;
	}

	public boolean isMatched() {
		return isMatched;
	}

	public void setMatched(boolean isMatched) {
		this.isMatched = isMatched;
	}

	public double getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public void setAdjustmentAmount(double adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}

	public long getBankFeesAdjustmentAcc() {
		return bankFeesAdjustmentAcc;
	}

	public void setBankFeesAdjustmentAcc(long bankFeesAdjustmentAcc) {
		this.bankFeesAdjustmentAcc = bankFeesAdjustmentAcc;
	}

}
