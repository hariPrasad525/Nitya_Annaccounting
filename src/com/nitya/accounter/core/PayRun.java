package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class PayRun extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<EmployeePaymentDetails> payEmployee = new HashSet<EmployeePaymentDetails>();

	private FinanceDate payPeriodStartDate;

	private FinanceDate payPeriodEndDate;

	private double deductionAmount = 0.0;

	private double earningsAmount = 0.0;

	private List<AttendanceManagementItem> attendanceItems = new ArrayList<AttendanceManagementItem>();

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private Double noOfWorkingDays = 0.0D;

	/**
	 * This is to specify how much of this Bill is paid.
	 */
	double payments = 0D;

	/**
	 * This will specify the balance due
	 */
	double balanceDue = 0D;

	public PayRun() {
		super();
		setType(Transaction.TYPE_PAY_RUN);
	}

	/**
	 * @return the payEmployee
	 */
	public Set<EmployeePaymentDetails> getPayEmployee() {
		return payEmployee;
	}

	/**
	 * @param payEmployee
	 *            the payEmployee to set
	 */
	public void setPayEmployee(Set<EmployeePaymentDetails> payEmployee) {
		this.payEmployee = payEmployee;
	}

	/**
	 * @return the payPeriodStartDate
	 */
	public FinanceDate getPayPeriodStartDate() {
		return payPeriodStartDate;
	}

	/**
	 * @param payPeriodStartDate
	 *            the payPeriodStartDate to set
	 */
	public void setPayPeriodStartDate(FinanceDate payPeriodStartDate) {
		this.payPeriodStartDate = payPeriodStartDate;
	}

	/**
	 * @return the payPeriodEndDate
	 */
	public FinanceDate getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	/**
	 * @param payPeriodEndDate
	 *            the payPeriodEndDate to set
	 */
	public void setPayPeriodEndDate(FinanceDate payPeriodEndDate) {
		this.payPeriodEndDate = payPeriodEndDate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		// Running Payment of Each Employee
		setType(Transaction.TYPE_PAY_RUN);
		doPayRunCreateEffect();
		this.balanceDue = this.total;
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		PayRun payRun = (PayRun) clientObject;
		if (payRun.getSaveStatus() == STATUS_VOID) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
		}

		if (payRun.getSaveStatus() == STATUS_DRAFT
				|| payRun.getSaveStatus() == STATUS_TEMPLATE) {
			throw new AccounterException(
					AccounterException.ERROR_CANT_CREATE_PAYRUN_DRAFT_OR_TEMPLATE);
		}
		if (this.status == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED
				|| this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// "You have already paid  amount for this Invoice, You can't Edit  it and Void it.");
		}

		if (!goingToBeEdit) {
			if (payRun.getPayEmployee() == null
					|| payRun.getPayEmployee().isEmpty()) {
				throw new AccounterException(
						AccounterException.ERROR_TRANSACTION_ITEM_NULL, Global
								.get().messages().transactionItem());
			}

			for (EmployeePaymentDetails detail : payRun.getPayEmployee()) {
				detail.canEdit(detail, goingToBeEdit);
			}
		}
		return true;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		PayRun payRun = (PayRun) clonedObject;

		doPayRunDeleteEffect(payRun);

		if (!this.isVoid() && !payRun.isVoid()) {
			doPayRunCreateEffect();
		}

		this.balanceDue = this.total - payments;

		super.onEdit(clonedObject);
	}

	private void doPayRunCreateEffect() {
		for (EmployeePaymentDetails detail : this.getPayEmployee()) {
			detail.setPayRun(this);
			detail.runPayment();
		}

		double totalAmount = earningsAmount - deductionAmount;
		setTotal(totalAmount);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.noOfWorkingDays(), this.noOfWorkingDays);
		w.put(messages.employee(), this.employee != null ? this.getEmployee()
				.getName() : "");
		w.put(messages.employeeGroup(), this.employeeGroup != null ? this
				.getEmployeeGroup().getName() : "");
		w.put(messages.startDate(), this.payPeriodStartDate.toString());
		w.put(messages.endDate(), this.payPeriodEndDate.toString());
	}

	public void addDeductions(double deduction) {
		this.deductionAmount += deduction;
	}

	public double getDeductionAmount() {
		return deductionAmount;
	}

	public void addEarnings(double earnings) {
		this.earningsAmount += earnings;
	}

	public double getEarningsAmount() {
		return earningsAmount;
	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_EMPLOYEE;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_RUN;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	public List<AttendanceManagementItem> getAttendanceItems() {
		return attendanceItems;
	}

	public void setAttendanceItems(
			List<AttendanceManagementItem> attendanceItems) {
		this.attendanceItems = attendanceItems;
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
	public boolean onDelete(Session session) throws CallbackException {
		doPayRunDeleteEffect(this);
		this.payEmployee.clear();
		return super.onDelete(session);
	}

	private void doPayRunDeleteEffect(PayRun payRun) {
		// for (EmployeePaymentDetails detail : payRun.getPayEmployee()) {
		// for (EmployeePayHeadComponent component : detail
		// .getPayHeadComponents()) {
		// double rate = component.getRate();
		// Account account = component.getPayHead().getAccount();
		// if (component.isDeduction()) {
		// account.updateCurrentBalance(payRun, -rate, 1);
		// } else if (component.isEarning()) {
		// account.updateCurrentBalance(payRun, rate, 1);
		// }
		// }
		// }
		//
		// Account salariesPayableAccount = getCompany()
		// .getSalariesPayableAccount();
		// salariesPayableAccount.updateCurrentBalance(payRun,
		// -payRun.getTotal(),
		// 1);

		payRun.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		payRun.payments = payRun.total;
		payRun.balanceDue = 0.0;
	}

	@Override
	public void getEffects(ITransactionEffects e) {

		for (EmployeePaymentDetails detail : this.getPayEmployee()) {
			double empPayment = 0.00D;
			for (EmployeePayHeadComponent component : detail
					.getPayHeadComponents()) {
				double rate = component.getRateToUpdate();
				PayHead payHead = component.getPayHead();
				Account account = null;
				int payHeadType = payHead.getType();
				if (payHeadType == PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
						|| payHead.getType() == PayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS) {
					account = payHead.getLiabilityAccount();
				} else if (payHeadType == PayHead.TYPE_LOANS_AND_ADVANCES) {
					account = payHead.getAssetAccount();
				} else {
					account = payHead.getAccount();
				}
				e.add(account, rate, 1);
				if (!payHead.isEffectsPayment()) {
					e.add(payHead.getAccount(), -rate, 1);
					continue;
				}
				empPayment += rate;
			}
			e.add(detail.getEmployee(), -1 * empPayment);
		}

	}

	public void setNoOfWorkingDays(Double noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}

	public Double getNoOfWorkingDays() {
		return noOfWorkingDays;
	}

	public void updateStatus() {
		if (DecimalUtil.isGreaterThan(this.balanceDue, 0)
				&& DecimalUtil.isLessThan(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		} else if (DecimalUtil.isEquals(this.balanceDue, 0.0)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (DecimalUtil.isEquals(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}
	}

	public void updateBalance(double amount) {
		this.payments += amount;
		this.balanceDue -= amount;
		updateStatus();
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		PayRun bill = (PayRun) super.clone();
		bill.balanceDue = bill.getTotal();
		bill.payments = 0;
		bill.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		return bill;
	}

	public void updateUnUsedAmount(Session session, double payment) {
		// TODO Auto-generated method stub

	}
}
