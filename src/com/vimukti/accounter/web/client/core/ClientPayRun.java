package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientPayRun extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<ClientEmployeePaymentDetails> payEmployee = new HashSet<ClientEmployeePaymentDetails>();

	private long payPeriodStartDate;

	private long payPeriodEndDate;

	private long employee;

	private long employeeGroup;

	private List<ClientAttendanceManagementItem> attendanceItems = new ArrayList<ClientAttendanceManagementItem>();

	private Double noOfWorkingDays = 0.0D;

	public ClientPayRun() {
		super();
		setType(TYPE_PAY_RUN);
	}

	/**
	 * @return the payEmployee
	 */
	public Set<ClientEmployeePaymentDetails> getPayEmployee() {
		return payEmployee;
	}

	/**
	 * @param payEmployee
	 *            the payEmployee to set
	 */
	public void setPayEmployee(Set<ClientEmployeePaymentDetails> payEmployee) {
		this.payEmployee = payEmployee;
	}

	/**
	 * @return the payPeriodStartDate
	 */
	public long getPayPeriodStartDate() {
		return payPeriodStartDate;
	}

	/**
	 * @param payPeriodStartDate
	 *            the payPeriodStartDate to set
	 */
	public void setPayPeriodStartDate(long payPeriodStartDate) {
		this.payPeriodStartDate = payPeriodStartDate;
	}

	/**
	 * @return the payPeriodEndDate
	 */
	public long getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	/**
	 * @param payPeriodEndDate
	 *            the payPeriodEndDate to set
	 */
	public void setPayPeriodEndDate(long payPeriodEndDate) {
		this.payPeriodEndDate = payPeriodEndDate;
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_RUN;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public void setAttendanceItems(List<ClientAttendanceManagementItem> allRows) {
		this.attendanceItems = allRows;
	}

	public List<ClientAttendanceManagementItem> getAttendanceItems() {
		return this.attendanceItems;
	}

	public long getEmployee() {
		return employee;
	}

	public void setEmployee(long employee) {
		this.employee = employee;
	}

	public long getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(long employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	public void setNoOfWorkingDays(Double noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}

	public Double getNoOfWorkingDays() {
		return noOfWorkingDays;
	}
}
