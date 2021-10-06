package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

public class ClientEmployeePaymentDetails implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientEmployeePaymentDetails() {
	}

	private long payRun;

	private long employee;

	private Set<ClientEmployeePayHeadComponent> payHeadComponents = new HashSet<ClientEmployeePayHeadComponent>();

	private long id;

	private int version;

	/**
	 * @return the payHeadComponents
	 */
	public Set<ClientEmployeePayHeadComponent> getPayHeadComponents() {
		return payHeadComponents;
	}

	/**
	 * @param payHeadComponents
	 *            the payHeadComponents to set
	 */
	public void setPayHeadComponents(
			Set<ClientEmployeePayHeadComponent> payHeadComponents) {
		this.payHeadComponents = payHeadComponents;
	}

	/**
	 * @return the payRun
	 */
	public long getPayRun() {
		return payRun;
	}

	/**
	 * @param payRun
	 *            the payRun to set
	 */
	public void setPayRun(long payRun) {
		this.payRun = payRun;
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
		return "Employee Payment Details";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_PAYMENT_DETAILS;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public long getEmployee() {
		return employee;
	}

	public void setEmployee(long employee) {
		this.employee = employee;
	}
}
