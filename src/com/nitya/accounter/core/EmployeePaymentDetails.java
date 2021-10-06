package com.nitya.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;

public class EmployeePaymentDetails implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeePaymentDetails() {
	}

	/**
	 * Pay Run
	 */
	private PayRun payRun;

	private Employee employee;

	private Set<EmployeePayHeadComponent> payHeadComponents = new HashSet<EmployeePayHeadComponent>();

	private int version;

	private long id;

	/**
	 * @return the payHeadComponents
	 */
	public Set<EmployeePayHeadComponent> getPayHeadComponents() {
		return payHeadComponents;
	}

	/**
	 * @param payHeadComponents
	 *            the payHeadComponents to set
	 */
	public void setPayHeadComponents(
			Set<EmployeePayHeadComponent> payHeadComponents) {
		this.payHeadComponents = payHeadComponents;
	}

	/**
	 * @return the payRun
	 */
	public PayRun getPayRun() {
		return payRun;
	}

	/**
	 * @param payRun
	 *            the payRun to set
	 */
	public void setPayRun(PayRun payRun) {
		this.payRun = payRun;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * Run the Payment of this Employee
	 */
	public void runPayment() {
		for (EmployeePayHeadComponent component : payHeadComponents) {
			component.setEmpPaymentDetails(this);
			PayHead payHead = component.getPayHead();
			if(!payHead.isEffectsPayment()){
				continue;
			}
				
			double rate = component.getRate();
			if (component.isDeduction()) {
				payRun.addDeductions(rate);
			} else if (component.isEarning()) {
				payRun.addEarnings(rate);
			}
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		EmployeePaymentDetails details = (EmployeePaymentDetails) clientObject;
		if (details.getEmployee() == null) {
			throw new AccounterException(AccounterException.ERROR_EMPLOYEE_NULL);
		}

		if (details.getPayHeadComponents() == null
				|| details.getPayHeadComponents().isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_ITEM_NULL, Global
							.get().messages().transactionItem());
		}

		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
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

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	public double getEmployeePayment() {
		double empTotal = 0.00D;
		for (EmployeePayHeadComponent component : getPayHeadComponents()) {
			double rate = component.getRateToUpdate();
			PayHead payHead = component.getPayHead();
			if (payHead.getType() == PayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS) {
				continue;
			}
			empTotal += rate;
		}
		return empTotal;
	}

}
