package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class FixedAssetHistory extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ACTION_TYPE_NONE = "NONE";
	public static final String ACTION_TYPE_STATUS = "Status";
	public static final String ACTION_TYPE_ROLLBACK = "Rollback";
	public static final String ACTION_TYPE_CREATED = "Created";
	public static final String ACTION_TYPE_DELETED = "Deleted";
	public static final String ACTION_TYPE_REGISTERED = "Registered";
	public static final String ACTION_TYPE_SOLD = "Sold";
	public static final String ACTION_TYPE_DISPOSED = "Disposed";
	public static final String ACTION_TYPE_DISPOSAL_REVERSED = "Disposal reversed";
	public static final String ACTION_TYPE_DEPRECIATED = "Depreciation";
	public static final String ACTION_TYPE_NOTE = "Note";

	String actionType;
	FinanceDate actionDate;
	String details;

	String user;
	JournalEntry postedJournalEntry;
	FixedAsset fixedAsset;

	public FixedAssetHistory() {

	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public FinanceDate getActionDate() {
		return actionDate;
	}

	public void setActionDate(FinanceDate actionDate) {
		this.actionDate = actionDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public JournalEntry getPostedJournalEntry() {
		return postedJournalEntry;
	}

	public void setPostedJournalEntry(JournalEntry postedJournalEntry) {
		this.postedJournalEntry = postedJournalEntry;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// NOTHING TO DO
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		// NOTHING TO DO
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.fixedAssetHistory()).gap();
	}

	public FixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(FixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

}
