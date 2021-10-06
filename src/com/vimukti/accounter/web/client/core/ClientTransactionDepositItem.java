package com.vimukti.accounter.web.client.core;

public class ClientTransactionDepositItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long receivedFrom;
	private long account;
	private Double total;
	private int version;
	private String name;
	private long id;
	private boolean isBillable;
	private long customer;
	private String description;
	private String paymentMethod;
	private long accounterClass;
	private long job;

	private ClientTransaction transaction;

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
		return this.name;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public void setAccount(long id) {
		this.account = id;
	}

	public long getAccount() {
		return this.account;
	}

	public boolean isEmpty() {
		if (this.account == 0 && this.total == null) {
			return true;
		}
		return false;
	}

	public boolean isBillable() {
		return this.isBillable;
	}

	public void setIsBillable(boolean value) {
		this.isBillable = value;
	}

	public long getCustomer() {
		return this.customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public long getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(long receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public long getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(long accounterClass) {
		this.accounterClass = accounterClass;
	}

	public ClientTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	public long getJob() {
		return job;
	}

	public void setJob(long job) {
		this.job = job;
	}

}
