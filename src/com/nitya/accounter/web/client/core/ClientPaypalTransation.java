package com.nitya.accounter.web.client.core;


public class ClientPaypalTransation implements IAccounterCore {

	/**
	 * 
	 */

	private long id;

	private static final long serialVersionUID = 1L;

	private String date;

	private String timeZone;

	private String type;

	private String email;

	private String buyerName;

	private String transactionID;

	private String transactionStatus;

	private String grossAmount;

	private String paypalFees;

	private String netAmount;
	
	private String currencyCode;
	
	private long accountID;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}

	public String getPaypalFees() {
		return paypalFees;
	}

	public void setPaypalFees(String paypalFees) {
		this.paypalFees = paypalFees;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "ClientPaypalTransation";
	}

	@Override
	public String getDisplayName() {
		return "ClientPaypalTransation";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAYPALTRANSACTION;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}



}
