package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * Salary Details is used to define the Pay Structure for an Employee or an
 * Employee Group. To speed up the entry of individual Employee's Pay Structure
 * you can define the Pay Structure for the Employee Group with those Pay Heads
 * which applies for most of the employees and copy the same to individual
 * employees.
 * 
 * If required, a Pay Head component or its value may be added, deleted or
 * altered at Individual Employee level. To define Salary Details for Employees
 * refer Creating Salary Details for an Employee.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class PayStructure extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private List<PayStructureItem> items = new ArrayList<PayStructureItem>();

	/**
	 * @return the items
	 */
	public List<PayStructureItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<PayStructureItem> items) {
		this.items = items;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.employee(), this.employee != null ? this.getEmployee()
				.getName() : "");
		w.put(messages.employeeGroup(), this.employeeGroup != null ? this
				.getEmployeeGroup().getName() : "");
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(EmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.PAY_STRUCTURE;
	}
}
