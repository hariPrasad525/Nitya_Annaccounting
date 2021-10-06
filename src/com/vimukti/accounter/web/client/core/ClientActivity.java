package com.vimukti.accounter.web.client.core;

public class ClientActivity implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CREATED = 2;

	public static final int EDITED = 3;

	public static final int VOIDED = 4;

	public static final int NOTE = 6;

	public static final long LOGIN_LOGOUT = 0x1L;

	public static final long TRNASACTIONS = 0x2L;

	public static final long RECURRING_TRNASACTIONS = 0x4L;

	public static final long PREFERENCES = 0x8L;

	public static final long RECONCILIATIONS = 0x10L;

	public static final long BUDGETS = 0x20L;

	// public static final long SALES_CUSTOMIZATION = 0x40L;

	// public static final long DATA_EXCHANGE = 0x100L;

	// public static final long LISTS = 0x4L;

	// public static final long STATEMENTS = 0x8L;

	private long id;

	private ClientUser clientUser;

	private long time;

	private String dataType;

	private long objectID;

	private String name;

	private String userName;

	private ClientFinanceDate transactionDate;

	private Double amount;

	private int activityType;

	private int objType;

	private String description;

	private String currency;

	private String auditHistory;

	private int objStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getObjectID() {
		return objectID;
	}

	public void setObjectID(long objectID) {
		this.objectID = objectID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ClientUser getClientUser() {
		return clientUser;
	}

	public void setClientUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	public ClientFinanceDate getDate() {
		return transactionDate;
	}

	public void setDate(ClientFinanceDate date) {
		this.transactionDate = date;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public String getDisplayName() {
		return "ClientActivity";
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ACTIVITY;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAuditHistory() {
		return auditHistory;
	}

	public void setAuditHistory(String auditHistory) {
		this.auditHistory = auditHistory;
	}

	public int getObjStatus() {
		return objStatus;
	}

	public void setObjStatus(int objStatus) {
		this.objStatus = objStatus;
	}

}
