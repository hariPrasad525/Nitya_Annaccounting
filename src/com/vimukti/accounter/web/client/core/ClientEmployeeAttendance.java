package com.vimukti.accounter.web.client.core;


public class ClientEmployeeAttendance implements IAccounterCore {
	
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

	public void setPayrollDate(ClientFinanceDate value) {
		this.payrollDate = value;
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
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
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
