package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientAttendanceManagement implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ClientAttendanceManagementItem> items = new ArrayList<ClientAttendanceManagementItem>();

	long id;

	int version;

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
		return "Attendance Management";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_MANAGEMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public List<ClientAttendanceManagementItem> getItems() {
		return items;
	}

	public void setItems(List<ClientAttendanceManagementItem> items) {
		this.items = items;
	}
}
