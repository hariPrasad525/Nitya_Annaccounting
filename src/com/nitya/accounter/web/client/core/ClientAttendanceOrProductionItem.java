package com.nitya.accounter.web.client.core;

public class ClientAttendanceOrProductionItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long attendanceOrProductionType;

	private double value;

	private int version;

	private long id;

	private ClientAttendanceOrProductionType clientAttendanceOrProductionType;

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "Attendance Or Production Item";
	}

	@Override
	public String getDisplayName() {
		return clientAttendanceOrProductionType.getDisplayName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PRODUCTION_ITEM;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public long getAttendanceOrProductionType() {
		return attendanceOrProductionType;
	}

	public void setAttendanceOrProductionType(long attendanceOrProductionType) {
		this.attendanceOrProductionType = attendanceOrProductionType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ClientAttendanceOrProductionType getClientAttendanceOrProductionType() {
		return clientAttendanceOrProductionType;
	}

	public void setClientAttendanceOrProductionType(
			ClientAttendanceOrProductionType clientAttendanceOrProductionType) {
		this.clientAttendanceOrProductionType = clientAttendanceOrProductionType;
	}

}
