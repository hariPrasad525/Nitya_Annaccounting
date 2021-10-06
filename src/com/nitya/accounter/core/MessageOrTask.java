package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author vimukti-M3
 * 
 */
public class MessageOrTask extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_MESSAGE = 1;
	public static final int TYPE_TASK = 2;
	public static final int TYPE_WARNING = 3;

	public static final int CONTENT_TYPE_RECURRING_REMINDER = 1;
	public static final int CONTENT_TYPE_AUTOMATIC_TRANSACTIONS = 2;
	public static final int CONTENT_TYPE_PURCHASE_ORDER = 3;

	/**
	 * To store the type of object, It may be message, task or warning.
	 */
	private int type;

	private FinanceDate date;

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

	private long toUser;

	public MessageOrTask() {

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public FinanceDate getDate() {
		return date;
	}

	public void setDate(FinanceDate date) {
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

	public long getToUser() {
		return toUser;
	}

	public void setToUser(long toUser) {
		this.toUser = toUser;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.content == null || this.content.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().message());
		}

	}

}
