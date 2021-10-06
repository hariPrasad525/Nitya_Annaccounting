package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientEmployeeCompsensation implements IAccounterCore { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int PAY_TYPE_HOURLY = 1;
	public static final int PAY_TYPE_PER_PERIOD = 2;
	public static final int PAY_TYPE_ANNUAL = 3;
	public static final int PAY_TYPE_DAILY = 4;
	public static final int PAY_TYPE_MILES = 5;
	public static final int PAY_TYPE_PER_WORK = 6;
	
	public static final int COMP_TYPE_EARNINGS = 1;
	public static final int COMP_TYPE_DEDUCTIONS = 2;
	
	public static final int PAY_FREQUENCY_WEEKLY = 1;
	public static final int PAY_FREQUENCY_BI_WEEKLY = 2;
	public static final int PAY_FREQUENCY_SEMI_MONTHLY = 3;
	public static final int PAY_FREQUENCY_MONTHLY = 4;
	
	int version;
	private long id;
	
	private int compType;
	private double salary;
	private int payType;
	private int payFrequency;
	private double additionalAmount;

	public int getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(int payFrequency) {
		this.payFrequency = payFrequency;
	}

	public int getCompType() {
		return compType;
	}

	public void setCompType(int compType) {
		this.compType = compType;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public double getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(double additionalAmount) {
		this.additionalAmount = additionalAmount;
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
		return Global.get().messages2().employeeCompensation();
	}

	@Override
	public String getDisplayName() {
		return Global.get().messages2().employeeCompensation();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_COMPENSATION;
	}

	@Override
	public void setID(long id) {
      this.id = id;		
	}

	@Override
	public long getID() {
		return id;
	}

}
