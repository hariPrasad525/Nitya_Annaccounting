package com.nitya.accounter.core;

import java.util.List;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class AttendanceManagement extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AttendanceManagementItem> items;

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public List<AttendanceManagementItem> getItems() {
		return items;
	}

	public void setItems(List<AttendanceManagementItem> items) {
		this.items = items;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
