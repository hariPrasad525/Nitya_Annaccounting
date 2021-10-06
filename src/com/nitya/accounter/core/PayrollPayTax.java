package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class PayrollPayTax extends Transaction {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private Account payAccount;

	private List<PayrollTransactionPayTax> transactionPayEmployee = new ArrayList<PayrollTransactionPayTax>();


	private List<PayrollTransactionPayTax> transactionPayTax;

	public PayrollPayTax() {
		super();
		setType(TYPE_PAY_TAX);
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
		return AccounterServerConstants.TYPE_PAY_TAX;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	public void getEffects(ITransactionEffects e) {

		for (PayrollTransactionPayTax bill : getTransactionPayTax()) {

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

	public void setEmployee(Employee Employee) {
		this.employee = Employee;
	}

	public EmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(EmployeeGroup EmployeeGroup) {
		this.employeeGroup = EmployeeGroup;
	}

	public Account getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(Account payAccount) {
		this.payAccount = payAccount;
	}

	public List<PayrollTransactionPayTax> getTransactionPayTax() {
		return transactionPayTax;
	}

	public void setTransactionPayTax(
			List<PayrollTransactionPayTax> transactionPayTax) {
		this.transactionPayTax = transactionPayTax;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, PayrollPayTax payTax) {
		payTax.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		for (PayrollTransactionPayTax transactionPayTax : this.transactionPayTax) {
			transactionPayTax.setIsVoid(true);
			transactionPayTax.onUpdate(session);
			session.update(transactionPayTax);
		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		PayrollPayTax payTax = (PayrollPayTax) clientObject;

		if (this.isVoidBefore && payTax.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}

		super.canEdit(clientObject, goingToBeEdit);
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		setType(Transaction.TYPE_PAY_TAX);
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (isDraft()) {
			super.onSave(session);
			return false;
		}

		if (this.getID() == 0l) {

			if (this.transactionPayTax != null) {
				for (PayrollTransactionPayTax tpb : this.transactionPayTax) {
					tpb.setPayTax(this);
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
		PayrollPayTax payTax = (PayrollPayTax) clonedObject;

		if (this.transactionPayTax != null) {
			for (PayrollTransactionPayTax tpb : this.transactionPayTax) {
				tpb.setPayTax(this);
			}
		}

		if (isDraftOrTemplate()) {
			super.onEdit(payTax);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !payTax.isVoid()) {
			doVoidEffect(session, this);
		}

		super.onEdit(payTax);
	}

	
	}



