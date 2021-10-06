package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class AttendanceManagementItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;

	private List<AttendanceOrProductionItem> attendanceOrProductionItems = new ArrayList<AttendanceOrProductionItem>();

	private List<UserDefinedPayheadItem> userDefinedPayheads = new ArrayList<UserDefinedPayheadItem>();

	private double abscentDays;

	private int version;

	private long id;
	
	private int employeePayType;

	public int getEmployeePayType() {
		return employeePayType;
	}

	public void setEmployeePayType(int employeePayType) {
		this.employeePayType = employeePayType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

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

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	public double getAbscentDays() {
		return abscentDays;
	}

	public void setAbscentDays(double abscentDays) {
		this.abscentDays = abscentDays;
	}

	public List<AttendanceOrProductionItem> getAttendanceOrProductionItems() {
		return attendanceOrProductionItems;
	}

	public void setAttendanceOrProductionItems(
			List<AttendanceOrProductionItem> attendanceOrProductionItems) {
		this.attendanceOrProductionItems = attendanceOrProductionItems;
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
	public long getID() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<UserDefinedPayheadItem> getUserDefinedPayheads() {
		return userDefinedPayheads;
	}

	public void setUserDefinedPayheads(
			List<UserDefinedPayheadItem> userDefinedPayheads) {
		this.userDefinedPayheads = userDefinedPayheads;
	}
}
