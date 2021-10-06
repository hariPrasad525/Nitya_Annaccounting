package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientAttendanceManagementItem implements IAccounterCore {
	
	public ClientAttendanceManagementItem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long employee;

	private List<ClientAttendanceOrProductionItem> attendanceOrProductionItems = new ArrayList<ClientAttendanceOrProductionItem>();
	
	private List<ClientUserDefinedPayheadItem> userDefinedPayheads = new ArrayList<ClientUserDefinedPayheadItem>();
	
	private long id;

	private int version;

	private double abscentDays;
	
	private int employeePayType;

	public int getEmployeePayType() {
		return employeePayType;
	}

	public void setEmployeePayType(int employeePayType) {
		this.employeePayType = employeePayType;
	}

	public long getEmployee() {
		return employee;
	}

	public void setEmployee(long employee) {
		this.employee = employee;
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
		return "Attendance Management Item";
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

	public void setAbscentDays(double abscentDays) {
		this.abscentDays = abscentDays;
	}

	public double getAbscentDays() {
		return abscentDays;
	}

	public List<ClientAttendanceOrProductionItem> getAttendanceOrProductionItems() {
		return attendanceOrProductionItems;
	}

	public void setAttendanceOrProductionItems(
			List<ClientAttendanceOrProductionItem> attendanceOrProductionItems) {
		this.attendanceOrProductionItems = attendanceOrProductionItems;
	}

	public List<ClientUserDefinedPayheadItem> getUserDefinedPayheads() {
		return userDefinedPayheads;
	}

	public void setUserDefinedPayheads(
			List<ClientUserDefinedPayheadItem> userDefinedPayheads) {
		this.userDefinedPayheads = userDefinedPayheads;
	}
}