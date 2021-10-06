package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class PayEmployee extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private Account payAccount;

	private List<TransactionPayEmployee> transactionPayEmployee = new ArrayList<TransactionPayEmployee>();

	public PayEmployee() {
		super();
		setType(TYPE_PAY_EMPLOYEE);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_EMPLOYEE;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_EMPLOYEE;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	public void getEffects(ITransactionEffects e) {

		for (TransactionPayEmployee bill : getTransactionPayEmployee()) {

			PayRun payRun = bill.getPayRun();
			if (payRun == null) {
				continue;
			}

			double totalDiff = 0.00D;
			// Update Employee Balance and update it's account individually
			for (EmployeePaymentDetails epc : payRun.getPayEmployee()) {
				// Get Employee Payment
				Employee employee = epc.getEmployee();
				double employeePayment = epc.getEmployeePayment();
				// Update Employee
				e.add(employee, employeePayment);

				// UPDATE CURRENCY LOSSES OR GAINS
				double amountDue = employeePayment * payRun.currencyFactor;
				double payment = employeePayment * currencyFactor;
				// Diff will be zero when both transactions are same currency
				double diff = payment - amountDue;
				totalDiff += diff;
				e.add(employee.getAccount(), diff, 1);
			}
			e.add(getCompany().getExchangeLossOrGainAccount(), -totalDiff, 1);
		}

		e.add(getPayAccount(), getTotal());
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

	public Account getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(Account payAccount) {
		this.payAccount = payAccount;
	}

	public List<TransactionPayEmployee> getTransactionPayEmployee() {
		return transactionPayEmployee;
	}

	public void setTransactionPayEmployee(
			List<TransactionPayEmployee> transactionPayEmployee) {
		this.transactionPayEmployee = transactionPayEmployee;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, PayEmployee payEmployee) {
		payEmployee.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		for (TransactionPayEmployee transactionPayEmployee : this.transactionPayEmployee) {
			transactionPayEmployee.setIsVoid(true);
			transactionPayEmployee.onUpdate(session);
			session.update(transactionPayEmployee);
		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		PayEmployee payEmployee = (PayEmployee) clientObject;

		if (this.isVoidBefore && payEmployee.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}

		super.canEdit(clientObject, goingToBeEdit);
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		setType(Transaction.TYPE_PAY_EMPLOYEE);
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (isDraft()) {
			super.onSave(session);
			return false;
		}

		if (this.getID() == 0l) {

			if (this.transactionPayEmployee != null) {
				for (TransactionPayEmployee tpb : this.transactionPayEmployee) {
					tpb.setPayEmployee(this);
				}
			}

			this.subTotal = this.total;

			super.onSave(session);

			if (isDraftOrTemplate()) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		PayEmployee payEmployee = (PayEmployee) clonedObject;

		if (this.transactionPayEmployee != null) {
			for (TransactionPayEmployee tpb : this.transactionPayEmployee) {
				tpb.setPayEmployee(this);
			}
		}

		if (isDraftOrTemplate()) {
			super.onEdit(payEmployee);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !payEmployee.isVoid()) {
			doVoidEffect(session, this);
		}

		super.onEdit(payEmployee);
	}
}
