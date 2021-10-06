package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class AttendanceOrProductionItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AttendanceOrProductionType attendanceOrProductionType;

	private double value;

	private int version;

	private long id;

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
		return this.id;
	}

	public AttendanceOrProductionType getAttendanceOrProductionType() {
		return attendanceOrProductionType;
	}

	public void setAttendanceOrProductionType(
			AttendanceOrProductionType attendanceOrProductionType) {
		this.attendanceOrProductionType = attendanceOrProductionType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	@Override
	public void selfValidate() throws AccounterException {

	}

	public void setId(long id) {
		this.id = id;
	}
}
