package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class EmployeeCompsensation extends CreatableObject implements IAccounterServerCore {
	
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
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages2 messages = Global.get().messages2();
		w.put(messages.compType(), this.compType);
		w.put(messages.additionalAmountForCmp(), this.additionalAmount);
		w.put(messages.payFrequency(), this.payFrequency);
		w.put(messages.salary(), this.salary);
		w.put(messages.payType(), this.payType);
	}

	@Override
	public void selfValidate() throws AccounterException {

	}
	

}
