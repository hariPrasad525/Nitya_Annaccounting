package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TransactionPayEmployee extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ReffereredObject
	private PayRun payRun;

	double originalAmount = 0D;

	double amountDue = 0D;

	double payment = 0D;

	@ReffereredObject
	private PayEmployee payEmployee;

	private boolean isVoid = false;

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	public PayRun getPayRun() {
		return payRun;
	}

	public void setPayRun(PayRun payRun) {
		this.payRun = payRun;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		setCompany(payEmployee.getCompany());
		if (this.getID() == 0l && !payEmployee.isDraftOrTemplate()
				&& !payEmployee.isVoid()) {

			if (this.payRun != null) {
				this.payRun.setCanVoidOrEdit(Boolean.FALSE);
				session.saveOrUpdate(this.payRun);
			}

			double amount = this.payment;

			if (this.payRun != null) {
				setCompany(payRun.getCompany());
				this.payRun.updateBalance(amount);
			}

		}

		return false;

	}

	public PayEmployee getPayEmployee() {
		return payEmployee;
	}

	public void setPayEmployee(PayEmployee payEmployee) {
		this.payEmployee = payEmployee;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		if (!this.getPayEmployee().isVoid()) {
			doReverseEffect(true);
		}

		return false;
	}

	public void onVoidEffect(Session session) {
		session.saveOrUpdate(this.payEmployee);
		this.setPayment(0.0);
	}

	private void setPayment(double payment) {
		this.payment = payment;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if (payEmployee.isDraftOrTemplate()) {
			return false;
		}

		if (this.isVoid()) {
			doReverseEffect(false);
		}

		return false;
	}

	public void doReverseEffect(boolean isDeleting) {

		Session session = HibernateUtil.getCurrentSession();

		double amount = this.payment;

		if (this.payRun != null) {
			this.payRun.updateBalance(-amount);
			this.payRun = null;
		}

		session.saveOrUpdate(this);

	}

	public void makeVoid(Session session) {
		this.setPayment(0.0);
	}

	public void setIsVoid(boolean isVoid) {
		this.setVoid(isVoid);
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setPayEmployee(PayTAX payTax) {
		// TODO Auto-generated method stub
		
	}

}
