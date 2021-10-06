package com.vimukti.accounter.web.client.core;

public class ClientMessageOrTask implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_MESSAGE = 1;
	public static final int TYPE_TASK = 2;
	public static final int TYPE_WARNING = 3;

	public static final int TYPE_RECURRING_REMINDER = 1;
	public static final int TYPE_AUTOMATIC_TRANSACTIONS = 2;

	public static final int TO_USER_TYPE_ALL = 0;

	private long id;

	private int version;

	private long toUser;
	/**
	 * To store the type of object, It may be message, task or warning.
	 */
	private int type;

	private long date;

	/**
	 * Some messages and tasks need to be created by system. So that this field
	 * stores whether this object created by system or by user.
	 */
	private boolean systemCreated;

	private String content;

	/**
	 * To remind the user about recurring templates we need to create one task
	 * only, should not create a task every time. For these type of cases,
	 * created this field.
	 */
	private int contentType;

	/**
	 * For system created messages or tasks we may have default action. This
	 * field stores the history token of specific action.
	 */
	private String actionToken;

	private long createdBy;

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
		return getContent();
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.MESSAGE_OR_TASK;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public boolean isSystemCreated() {
		return systemCreated;
	}

	public void setSystemCreated(boolean systemCreated) {
		this.systemCreated = systemCreated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getActionToken() {
		return actionToken;
	}

	public void setActionToken(String actionToken) {
		this.actionToken = actionToken;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public long getToUser() {
		return toUser;
	}

	public void setToUser(long toUser) {
		this.toUser = toUser;
	}

}
