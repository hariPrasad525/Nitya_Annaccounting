package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class ClientAttendanceOrProductionType implements IAccounterCore {
	
	public static final String DEFAULT_MILE="Per Mile";
	public static final String DEFAULT_HOUR="Per Hour";
	public static final String DEFAULT_WORK="Per Work";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_LEAVE_WITH_PAY = 4;
	public static final int TYPE_LEAVE_WITHOUT_PAY = 3;
	public static final int TYPE_PRODUCTION = 1;
	public static final int TYPE_USER_DEFINED_CALENDAR = 2;

	private int type;

	/**
	 * Name of the AttendanceOrProductionType
	 */
	private String name;

	/** Will use for Type Production */
	private long unit;

	/** Use for all AttendanceOrProductionType except Production */
	private int periodType;
	private long id;
	private int version;

	private ClientPayrollUnit clientPayrollUnit;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getUnit() {
		return unit;
	}

	public void setUnit(long unit) {
		this.unit = unit;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		return this.name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PRODUCTION_TYPE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static String getTypeName(int type) {
		AccounterMessages messages = Global.get().messages();
		switch (type) {
		case TYPE_LEAVE_WITH_PAY:
			return messages.leaveWithPay();

		case TYPE_LEAVE_WITHOUT_PAY:
			return messages.leaveWithoutPay();

		case TYPE_PRODUCTION:
			return messages.productionType();

		case TYPE_USER_DEFINED_CALENDAR:
			return messages.userDefinedCalendar();

		default:
			return null;
		}
	}

	public void setClientPayrollUnit(ClientPayrollUnit clientUnit) {
		this.clientPayrollUnit = clientUnit;
	}

	/**
	 * @return the clientPayrollUnit
	 */
	public ClientPayrollUnit getClientPayrollUnit() {
		return clientPayrollUnit;
	}
}
