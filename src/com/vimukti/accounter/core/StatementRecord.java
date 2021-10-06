package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class StatementRecord extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FinanceDate statementDate;

	private String description;

	private String referenceNumber;

	private double spentAmount;

	private double receivedAmount;

	private double closingBalance;

	private Statement statement;

	private boolean isMatched;

	private double adjustmentAmount;

	private List<Transaction> transactionLists = new ArrayList<Transaction>();

	private Account bankFeesAdjustmentAcc;

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
	private TAXCode taxCode;
	/*
	 * Amount which is used for reconcile.
	 */
	private double bankFeesAdjustmentAmt;

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

	public TAXCode getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(TAXCode l) {
		this.taxCode = l;
	}

	public double getBankFeesAdjustmentAmt() {
		return bankFeesAdjustmentAmt;
	}

	public void setBankFeesAdjustmentAmt(double bankFeesAdjustmentAmt) {
		this.bankFeesAdjustmentAmt = bankFeesAdjustmentAmt;
	}

	private long bankFeeAcc;

	private double adjustAmt;

	public FinanceDate getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(FinanceDate statementDate) {
		this.statementDate = statementDate;
	}

	public String getDescription() {
		return bankDescription;
	}

	public void setDescription(String description) {
		this.bankDescription = description;
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

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANKSTATEMENT_RECORD;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public List<Transaction> getTransactionsLists() {
		return transactionLists;
	}

	public void setTransactionsLists(List<Transaction> transactionLists) {
		this.transactionLists = transactionLists;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		for (Transaction transaction : getTransactionsLists()) {
			transaction.setStatementRecord(this);

		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		for (Transaction transaction : getTransactionsLists()) {
			transaction.setStatementRecord(this);
		}
		this.setMatched(true);
		if (bankFeesAdjustmentAmt != 0) {
			adjustAmt = getBankFeesAdjustmentAmt();
			createJournalEntryForAdjustBankFeesAmount();

		}
		if (adjustmentAmount != 0) {
			adjustAmt = getAdjustmentAmount();
			JournalEntry journelEntry = createJournelEntry();
			session.save(journelEntry);
			this.transactionLists.add(journelEntry);
		}
		return super.onUpdate(session);
	}

	private void createJournalEntryForAdjustBankFeesAmount() {
		Account bankAcc = statement.getAccount();
		Account serverAccount = getBankFeesAdjustmentAcc();

		// Create journal entry
		JournalEntry jEntry = new JournalEntry();
		try {

			String number = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_JOURNAL_ENTRY, statement.getCompany());

			jEntry.setCompany(bankAcc.getCompany());
			jEntry.number = number;
			jEntry.transactionDate = statementDate;
			jEntry.memo = Global.get().messages().adjustmentTransaction();
			jEntry.setCurrency(bankAcc.getCurrency());
			jEntry.currencyFactor = bankAcc.getCurrencyFactor();

			List<TransactionItem> items = new ArrayList<TransactionItem>();
			TransactionItem item1 = new TransactionItem();
			item1.setAccount(bankAcc);
			item1.setType(TransactionItem.TYPE_ACCOUNT);
			item1.setDescription(bankAcc.getName());
			items.add(item1);

			TransactionItem item2 = new TransactionItem();
			item2.setAccount(serverAccount);
			item2.setType(TransactionItem.TYPE_ACCOUNT);
			item2.setDescription(serverAccount.getName());
			items.add(item2);

			jEntry.setTransactionItems(items);

			setTransactionTotal(jEntry);

			jEntry.setSaveStatus(Transaction.STATUS_APPROVE);

			transactionLists.add(jEntry);
			System.out.println("");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creating the journal entry object
	 */
	private JournalEntry createJournelEntry() {
		Account account = statement.getAccount();
		String number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_JOURNAL_ENTRY, statement.getCompany());

		JournalEntry journalEntry = new JournalEntry();
		journalEntry.setCompany(account.getCompany());
		journalEntry.number = number;
		journalEntry.transactionDate = statementDate;
		journalEntry.memo = Global.get().messages().adjustmentTransaction();
		journalEntry.setCurrency(account.getCurrency());
		journalEntry.currencyFactor = account.getCurrencyFactor();

		List<TransactionItem> items = new ArrayList<TransactionItem>();
		TransactionItem item1 = new TransactionItem();
		item1.setAccount(account);
		item1.setType(TransactionItem.TYPE_ACCOUNT);
		item1.setDescription(account.getName());
		items.add(item1);

		TransactionItem item2 = new TransactionItem();
		item2.setAccount(statement.getCompany().getRoundingAccount());
		item2.setType(TransactionItem.TYPE_ACCOUNT);
		item2.setDescription(AccounterServerConstants.ROUNDING);
		items.add(item2);

		journalEntry.setTransactionItems(items);

		setTransactionTotal(journalEntry);

		journalEntry.setSaveStatus(Transaction.STATUS_APPROVE);

		return journalEntry;
	}

	/**
	 * Creating the transactionItems for Journal entry depending debit or credit
	 * 
	 * 
	 * @param obj
	 */
	private void setTransactionTotal(JournalEntry obj) {
		List<TransactionItem> items = obj.getTransactionItems();
		TransactionItem item1 = items.get(0);
		TransactionItem item2 = items.get(1);
		double selectedTransactionsTotal = getSelectedTransactionsTotal();

		// checking the selected transactions
		if (selectedTransactionsTotal != 0) {
			// Checking the spent or received statement record.
			if (spentAmount > 0) {
				if (spentAmount > selectedTransactionsTotal) {
					item1.setLineTotal(-1 * adjustAmt);
					item2.setLineTotal(adjustAmt);
					obj.setDebitTotal(item2.getLineTotal());
					obj.setCreditTotal(item1.getLineTotal());
				} else {
					item1.setLineTotal(adjustAmt);
					item2.setLineTotal(-1 * adjustAmt);
					obj.setDebitTotal(item1.getLineTotal());
					obj.setCreditTotal(item2.getLineTotal());
				}
			} else {
				if (spentAmount > selectedTransactionsTotal) {
					item1.setLineTotal(adjustAmt);
					item2.setLineTotal(-1 * adjustAmt);
					obj.setDebitTotal(item1.getLineTotal());
					obj.setCreditTotal(item2.getLineTotal());
				} else {
					item1.setLineTotal(-1 * adjustAmt);
					item2.setLineTotal(adjustAmt);
					obj.setDebitTotal(item2.getLineTotal());
					obj.setCreditTotal(item1.getLineTotal());
				}
			}
		} else {
			if (spentAmount > 0) {
				item1.setLineTotal(adjustAmt);
				item2.setLineTotal(-1 * adjustAmt);
				obj.setDebitTotal(item1.getLineTotal());
				obj.setCreditTotal(item2.getLineTotal());
			} else {
				item1.setLineTotal(-1 * adjustAmt);
				item2.setLineTotal(adjustAmt);
				obj.setDebitTotal(item2.getLineTotal());
				obj.setCreditTotal(item1.getLineTotal());
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private double getSelectedTransactionsTotal() {
		double total = 0;
		if (!transactionLists.isEmpty()) {
			for (Transaction transaction : transactionLists) {
				total += transaction.getTotal();
			}
			return total;
		}
		return total;
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

	public Account getBankFeesAdjustmentAcc() {
		return bankFeesAdjustmentAcc;
	}

	public void setBankFeesAdjustmentAcc(Account bankFeesAdjustmentAcc) {
		this.bankFeesAdjustmentAcc = bankFeesAdjustmentAcc;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
