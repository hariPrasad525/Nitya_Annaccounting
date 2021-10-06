
package com.nitya.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class PaymentTerms extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1857020372404584259L;
	public static final int DUE_NONE = 0;
	public static final int DUE_CURRENT_MONTH = 1;
	public static final int DUE_CURRENT_QUARTER = 2;
	public static final int DUE_CURRENT_HALF_YEAR = 3;
	public static final int DUE_CURRENT_YEAR = 4;
	public static final int DUE_CURRENT_SIXTY = 5;

	/**
	 * Payment Term Name
	 */
	String name;

	/**
	 * Payment Term Description
	 */
	String description;

	/**
	 * Due type of the payment term
	 */
	int due = PaymentTerms.DUE_NONE;
	int dueDays = PaymentTerms.DUE_NONE;

	double discountPercent;
	int ifPaidWithIn;
	boolean isDefault;

	private boolean isDateDriven;

	public PaymentTerms() {

	}

	public PaymentTerms(Company company, String name, String description,
			int ifPaidWithIn, double discountPercent, int due, int dueDays,
			boolean isDefault, boolean isDateDriven) {
		setCompany(company);
		this.name = name;
		this.description = description;
		this.ifPaidWithIn = ifPaidWithIn;
		this.discountPercent = discountPercent;
		this.due = due;
		this.dueDays = dueDays;
		this.isDefault = isDefault;
		this.isDateDriven = isDateDriven;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the due
	 */
	public int getDue() {
		return due;
	}

	/**
	 * @return the dueDays
	 */
	public int getDueDays() {
		return dueDays;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	/**
	 * @return the ifPaidWithIn
	 */
	public int getIfPaidWithIn() {
		return ifPaidWithIn;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.PAYMENT_TERM);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		return false;
	}

	private void checkNameConflictsOrNull() throws AccounterException {
		if (name.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL);
		}
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(PaymentTerms.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		PaymentTerms paymentTerms = (PaymentTerms) clientObject;
		Query query = session
				.getNamedQuery("getPaymentTerms.by.Name")
				.setParameter("name", paymentTerms.name,
						EncryptedStringType.INSTANCE)
				.setEntity("company", paymentTerms.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			PaymentTerms newPaymentTerms = (PaymentTerms) list.get(0);
			if (paymentTerms.getID() != newPaymentTerms.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "PaymentTerms already exists with this name");
			}
		}
		if (!goingToBeEdit) {
			checkNameConflictsOrNull();
		}
		return true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.PAYMENT_TERMS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.paymentTerms()).gap();
		w.put(messages.name(), this.name);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null && name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().paymentTerm());
		}
	}

	public boolean isDateDriven() {
		return isDateDriven;
	}

	public void setDateDriven(boolean isDateDriven) {
		this.isDateDriven = isDateDriven;
	}
}
