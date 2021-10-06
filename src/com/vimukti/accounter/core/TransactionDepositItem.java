package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class TransactionDepositItem implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ReffereredObject
	private Payee receivedFrom;

	@ReffereredObject
	private Account account;
	private Double total;
	private int version;
	private String refNo;
	private long id;
	private boolean isBillable;

	@ReffereredObject
	private Customer customer;
	private String description;
	private String paymentMethod;
	private AccounterClass accounterClass;

	@ReffereredObject
	private Job job;

	@ReffereredObject
	private Transaction transaction;

	private boolean isOnSaveProccessed;

	public Payee getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(Payee receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getName() {
		return refNo;
	}

	public void setName(String name) {
		this.refNo = name;
	}

	public boolean isBillable() {
		return isBillable;
	}

	public void setBillable(boolean isBillable) {
		this.isBillable = isBillable;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public AccounterClass getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(AccounterClass accounterClass) {
		this.accounterClass = accounterClass;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
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
	public long getID() {
		return getId();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (this.account == null) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_ITEM_NULL);
		}
		if (this.total < 0) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_ITEM_TOTAL_0);
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		if (this.account != null) {
			w.put(messages.account(), this.account.name);
		}

		if (this.receivedFrom != null) {
			w.put(messages.receivedFrom(), this.receivedFrom.name);
		}

		w.put(messages.description(), this.description);

		w.put(messages.total(), this.total);

		if (this.refNo != null) {
			w.put(messages.referenceNo(), this.refNo);
		}

		if (this.customer != null) {
			w.put(messages.customer(), this.customer.name);
		}

		if (this.job != null) {
			w.put(messages.job(), this.job.getName());
		}

		if (this.transaction.getLocation() != null) {
			w.put(messages.location(), this.transaction.getLocation().getName());
		}

		w.put(messages.isBillable(), this.isBillable);

		if (this.paymentMethod != null) {
			w.put(messages.paymentMethod(), this.paymentMethod);
		}

	}

	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed())
			return true;

		this.setOnSaveProccessed(true);

		if (this.transaction.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& ((CashPurchase) this.transaction).expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return false;
		return false;
	}

	@Override
	protected TransactionDepositItem clone() throws CloneNotSupportedException {
		TransactionDepositItem item = (TransactionDepositItem) super.clone();
		item.setId(0);
		item.setVersion(0);
		return item;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.total == null) {
			this.setTotal(new Double(0));
		}
		if(account==null){
				throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
						Global.get().messages().account());
		}
	}

}
