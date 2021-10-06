package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientJournalEntry extends ClientTransaction implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_NORMAL_JOURNAL_ENTRY = 1;
	public static final int TYPE_CASH_BASIS_JOURNAL_ENTRY = 2;

	double debitTotal = 0D;

	double creditTotal = 0D;

	int journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;

	double balanceDue = 0d;

	long involvedPayee;
	
	long involvedAccount;

	Set<ClientTransactionReceivePayment> transactionReceivePayments = new HashSet<ClientTransactionReceivePayment>();

	Set<ClientTransactionPayBill> transactionPayBills = new HashSet<ClientTransactionPayBill>();

	public ClientJournalEntry(ClientCustomer customer) {
		/*
		 * this.type = ClientTransaction.TYPE_JOURNAL_ENTRY; this.number =
		 * NumberUtils.getNextTransactionNumber(); this.date =
		 * customer.date.getTime(); this.memo = "Opening Balance";
		 * 
		 * List<ClientEntry> entries = new ArrayList<ClientEntry>(); ClientEntry
		 * entry1 = new ClientEntry();
		 * 
		 * long voucherNumber = NumberUtils.getNextVoucherNumber();
		 * entry1.setVoucherNumber(voucherNumber);
		 * entry1.setType(ClientEntry.TYPE_CUSTOMER);
		 * entry1.setAccount(company.getOpeningBalancesAccount());
		 * entry1.setMemo("Opening Balance"); entry1.setDebit(0D);
		 * entry1.setCredit(customer.getOpeningBalance());
		 * entry1.setJournalEntry(this);
		 * 
		 * ClientEntry entry2 = new ClientEntry();
		 * entry2.setVoucherNumber(voucherNumber);
		 * entry2.setType(ClientEntry.TYPE_CUSTOMER);
		 * entry2.setCustomer(customer); entry2.setMemo(customer.getName());
		 * entry2.setDebit(customer.getOpeningBalance()); entry2.setCredit(0D);
		 * entry2.setJournalEntry(this);
		 * 
		 * entries.add(entry1); entries.add(entry2); this.setEntry(entries);
		 * this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		 * this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
		 */
	}

	public ClientJournalEntry(ClientVendor vendor) {/*
													 * this.company =
													 * vendor.company; this.type
													 * =ClientTransaction.
													 * TYPE_JOURNAL_ENTRY;
													 * this.number =
													 * NumberUtils.
													 * getNextTransactionNumber
													 * (); this.date =
													 * vendor.date; this.memo =
													 * "Opening Balance";
													 * 
													 * List<ClientEntry> entries
													 * = new
													 * ArrayList<ClientEntry>();
													 * long voucherNumber =
													 * NumberUtils
													 * .getNextVoucherNumber();
													 * ClientEntry entry1 = new
													 * ClientEntry();
													 * entry1.setVoucherNumber
													 * (voucherNumber);
													 * entry1.setType
													 * (ClientEntry
													 * .TYPE_VENDOR);
													 * entry1.setAccount
													 * (vendor.company
													 * .getOpeningBalancesAccount
													 * ());entry1.setMemo(
													 * "Opening Balance");
													 * entry1.setDebit(vendor.
													 * getOpeningBalance());
													 * entry1.setCredit(0D);
													 * entry1
													 * .setJournalEntry(this);
													 * 
													 * ClientEntry entry2 = new
													 * ClientEntry();
													 * entry2.setVoucherNumber
													 * (voucherNumber);
													 * entry2.setType
													 * (ClientEntry
													 * .TYPE_VENDOR);
													 * entry2.setVendor(vendor);
													 * entry2
													 * .setMemo(vendor.getName
													 * ()); entry2.setDebit(0D);
													 * entry2.setCredit(vendor.
													 * getOpeningBalance());
													 * entry2
													 * .setJournalEntry(this);
													 * 
													 * entries.add(entry1);
													 * entries.add(entry2);
													 * this.setEntry(entries);
													 * this
													 * .setDebitTotal(entry1.
													 * getDebit() +
													 * entry2.getDebit());
													 * this.setCreditTotal
													 * (entry1.getCredit() +
													 * entry2.getCredit());
													 */
	}

	public ClientJournalEntry(ClientAccount account) {
		/*
		 * this.company = account.company; this.type =
		 * ClientTransaction.TYPE_JOURNAL_ENTRY; this.number =
		 * NumberUtils.getNextTransactionNumber(); this.date = account.asOf;
		 * 
		 * this.memo = "Opening Balance";
		 * 
		 * List<ClientEntry> entries = new ArrayList<ClientEntry>(); long
		 * voucherNumber = NumberUtils.getNextVoucherNumber(); ClientEntry
		 * entry1 = new ClientEntry(); entry1.setVoucherNumber(voucherNumber);
		 * entry1.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
		 * entry1.setAccount(account.company.getOpeningBalancesAccount());
		 * entry1.setMemo("Opening Balance"); entry1.setJournalEntry(this);
		 * 
		 * ClientEntry entry2 = new ClientEntry();
		 * entry2.setVoucherNumber(voucherNumber);
		 * entry2.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
		 * entry2.setAccount(account); entry2.setMemo(account.getName());
		 * entry2.setJournalEntry(this);
		 * 
		 * if (account.isIncrease()) {
		 * 
		 * entry2.setDebit(0D); entry2.setCredit(account.getOpeningBalance());
		 * entry1.setDebit(account.getOpeningBalance()); entry1.setCredit(0D); }
		 * else {
		 * 
		 * entry2.setDebit(account.getOpeningBalance()); entry2.setCredit(0D);
		 * entry1.setDebit(0D); entry1.setCredit(account.getOpeningBalance());
		 * 
		 * }
		 * 
		 * entries.add(entry1); entries.add(entry2); this.setEntry(entries);
		 * this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		 * this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
		 */

	}

	public ClientJournalEntry() {
	}

	public ClientJournalEntry(int journalEntryType) {
		this.journalEntryType = journalEntryType;
	}

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public double getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.JOURNALENTRY;
	}

	public Set<ClientTransactionReceivePayment> getTransactionReceivePayments() {
		return transactionReceivePayments;
	}

	public void setTransactionReceivePayments(
			Set<ClientTransactionReceivePayment> transactionReceivePayments) {
		this.transactionReceivePayments = transactionReceivePayments;
	}

	public Set<ClientTransactionPayBill> getTransactionPayBills() {
		return transactionPayBills;
	}

	public void setTransactionPayBills(
			Set<ClientTransactionPayBill> transactionPayBills) {
		this.transactionPayBills = transactionPayBills;
	}

	public ClientJournalEntry clone() {
		ClientJournalEntry clientJournalEntryClone = (ClientJournalEntry) this
				.clone();
		Set<ClientTransactionReceivePayment> transactionReceivePaymentsSet = new HashSet<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment ClientTransactionReceivePayment : this.transactionReceivePayments) {
			transactionReceivePaymentsSet.add(ClientTransactionReceivePayment
					.clone());

		}
		clientJournalEntryClone.transactionReceivePayments = transactionReceivePaymentsSet;

		Set<ClientTransactionPayBill> transactionPayBillsSet = new HashSet<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBillsSet.add(clientTransactionPayBill.clone());
		}
		clientJournalEntryClone.transactionPayBills = transactionPayBillsSet;

		return clientJournalEntryClone;
	}

	public long getInvolvedPayee() {
		return involvedPayee;
	}

	public void setInvolvedPayee(long involvedPayee) {
		this.involvedPayee = involvedPayee;
	}
	
	public long getInvolvedAccount() {
		return involvedAccount;
	}
	
	public void setInvolvedAccount(long involvedAccount){
		this.involvedAccount = involvedAccount;
	}
}
