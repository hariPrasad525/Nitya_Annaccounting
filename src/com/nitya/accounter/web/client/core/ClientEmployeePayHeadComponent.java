package com.nitya.accounter.web.client.core;

public class ClientEmployeePayHeadComponent implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientEmployeePayHeadComponent() {

	}

	private long payHead;

	private ClientPayHead clientPayHead;

	private double rate;

	private long id;

	private String employee;

	private long empPaymentDetails;
	
	private double employeerDeductions;

	public double getEmployeerDeductions() {
		return employeerDeductions;
	}

	public void setEmployeerDeductions(double employeerDeductions) {
		this.employeerDeductions = employeerDeductions;
	}

	private int version;

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
		return employee;
	}

	@Override
	public String getDisplayName() {
		return employee;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_PAYHEAD_COMPONENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public long getPayHead() {
		return payHead;
	}

	public void setPayHead(long payHead) {
		this.payHead = payHead;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public long getEmpPaymentDetails() {
		return empPaymentDetails;
	}

	public void setEmpPaymentDetails(long empPaymentDetails) {
		this.empPaymentDetails = empPaymentDetails;
	}

	public ClientPayHead getClientPayHead() {
		return clientPayHead;
	}

	public void setClientPayHead(ClientPayHead clientPayHead) {
		this.clientPayHead = clientPayHead;
	}
}
