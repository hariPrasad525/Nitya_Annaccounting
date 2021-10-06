package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class EmployeeAttendance extends CreatableObject implements IAccounterServerCore {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private ClientFinanceDate payrollDate;
	private long companyId;
	private long employeeId;
	private String milesPerHour;
	private String advances;
	private String foodAllowence;
	private String otherAllowances;
	public long succeedRequests;
	public long failureRequests;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getAdvances() {
		return advances;
	}

	public void setAdvances(String advances) {
		this.advances = advances;
	}


	
	public ClientFinanceDate getPayrollDate() {
		return payrollDate;
	}

	public void setPayrollDate(ClientFinanceDate payrollDate) {
		this.payrollDate = payrollDate;
	}

	public String getMileshours() {
		return milesPerHour;
	}

	public void setMileshours(String milesPerHour) {
		this.milesPerHour = milesPerHour;
	}

	public String getFoodAllowances() {
		return foodAllowence;
	}

	public void setFoodAllowances(String foodAllowence) {
		this.foodAllowence = foodAllowence;
	}

	public String getOtherAllowances() {
		return otherAllowances;
	}

	public void setOtherAllowances(String otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub
		
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	
	


}
