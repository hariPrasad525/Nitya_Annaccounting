package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A type of {@link Payee} which deals with the filing and paying of 'VAT' owed.
 * It contains two separate accounts; one for purchase transactions and the
 * other for sales.
 * 
 * @author Chandan
 * 
 */
public class TAXAgency extends Payee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3306854477062491595L;

	/**
	 * Payment Term for this VATAgency
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * Liability Account for this VATAgency
	 * 
	 * @see Account
	 */
	@ReffereredObject
	Account purchaseLiabilityAccount;

	@ReffereredObject
	Account salesLiabilityAccount;

	@ReffereredObject
	Account filedLiabilityAccount;

	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	public final static int TAX_RETURN_FREQUENCY_MONTHLY = 0;
	public final static int TAX_RETURN_FREQUENCY_QUARTERLY = 1;
	public final static int TAX_RETURN_FREQUENCY_HALF_YEARLY = 2;
	public final static int TAX_RETURN_FREQUENCY_YEARLY = 3;

	public final static int TAX_TYPE_SALESTAX = 1;
	public final static int TAX_TYPE_VAT = 2;
	public final static int TAX_TYPE_SERVICETAX = 3;
	public final static int TAX_TYPE_TDS = 4;
	public static final int TAX_TYPE_TCS = 5;
	public final static int TAX_TYPE_OTHER = 6;

	int VATReturn;
	int taxType;

	int taxFilingFrequency;

	private FinanceDate lastTAXReturnDate;

	private String otherField1;
	private String otherField2;
	private String otherField3;
	private String otherField4;
	private String otherField5;

	public int getTaxType() {
		return taxType;
	}

	public void setTaxType(int taxType) {
		this.taxType = taxType;
	}

	public TAXAgency() {
		setType(Payee.TYPE_TAX_AGENCY);
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public Account getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(Account purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public Account getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(Account salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	/**
	 * @return the vATReturn
	 */
	public int getVATReturn() {
		return VATReturn;
	}

	/**
	 * @param return1
	 *            the vATReturn to set
	 */
	public void setVATReturn(int return1) {
		VATReturn = return1;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.TAXAGENCY);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		setType(TYPE_TAX_AGENCY);
		this.filedLiabilityAccount = getCompany().getTAXFiledLiabilityAccount();
		return false;
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
	public Account getAccount() {

		return null;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(TAXAgency.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		TAXAgency taxAgency = (TAXAgency) clientObject;
		Query query = session
				.getNamedQuery("getTaxAgency.by.Name")
				.setParameter("name", taxAgency.name,
						EncryptedStringType.INSTANCE)
				.setEntity("company", taxAgency.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			TAXAgency newTaxAgency = (TAXAgency) list.get(0);
			if (taxAgency.getID() != newTaxAgency.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A TAXAgency already exists with this name");
			}
		}
		return true;
	}

	private void cheeckPaymentTermNull() throws AccounterException {
		if (getPaymentTerm() == null) {
			throw new AccounterException(AccounterException.ERROR_PLEASE_ENTER,
					Global.get().messages().paymentTerm());
		}
	}

	@Override
	protected String getPayeeName() {
		return Global.get().messages().taxAgency();
	}

	/**
	 * @return the tAXReturnFrequency
	 */
	public int getTAXFilingFrequency() {
		return taxFilingFrequency;
	}

	/**
	 * @param tAXReturnFrequency
	 *            the tAXReturnFrequency to set
	 */
	public void setTAXFilingFrequency(int tAXReturnFrequency) {
		this.taxFilingFrequency = tAXReturnFrequency;
	}

	/**
	 * @return the filedLiabilityAccount
	 */
	public Account getFiledLiabilityAccount() {
		return filedLiabilityAccount;
	}

	/**
	 * @param filedLiabilityAccount
	 *            the filedLiabilityAccount to set
	 */
	public void setFiledLiabilityAccount(Account filedLiabilityAccount) {
		this.filedLiabilityAccount = filedLiabilityAccount;
	}

	/**
	 * @return the lastTAXReturnDate
	 */
	public FinanceDate getLastTAXReturnDate() {
		return lastTAXReturnDate;
	}

	/**
	 * @param lastTAXReturnDate
	 *            the lastTAXReturnDate to set
	 */
	public void setLastTAXReturnDate(FinanceDate lastTAXReturnDate) {
		this.lastTAXReturnDate = lastTAXReturnDate;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TAXAGENCY;
	}

	public String getOtherField1() {
		return otherField1;
	}

	public void setOtherField1(String otherField1) {
		this.otherField1 = otherField1;
	}

	public String getOtherField2() {
		return otherField2;
	}

	public void setOtherField2(String otherField2) {
		this.otherField2 = otherField2;
	}

	public String getOtherField3() {
		return otherField3;
	}

	public void setOtherField3(String otherField3) {
		this.otherField3 = otherField3;
	}

	public String getOtherField4() {
		return otherField4;
	}

	public void setOtherField4(String otherField4) {
		this.otherField4 = otherField4;
	}

	public String getOtherField5() {
		return otherField5;
	}

	public void setOtherField5(String otherField5) {
		this.otherField5 = otherField5;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		ClientCompanyPreferences preferences = Global.get().preferences();
		super.selfValidate();
		cheeckPaymentTermNull();
		if (getTaxType() == 0) {
			throw new AccounterException(AccounterException.ERROR_PLEASE_ENTER,
					Global.get().messages().taxType());
		}

		switch (getTaxType()) {
		case TAXAgency.TAX_TYPE_SALESTAX:
			if (getSalesLiabilityAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER, Global.get()
								.messages().salesLiabilityAccount());
			}
			break;
		case TAXAgency.TAX_TYPE_VAT:
		case TAXAgency.TAX_TYPE_SERVICETAX:
			if (getSalesLiabilityAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER, Global.get()
								.messages().salesLiabilityAccount());
			}
			if (preferences.isTrackPaidTax()
					&& getPurchaseLiabilityAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER, Global.get()
								.messages().purchaseLiabilityAccount());
			}
			break;
		default:
			if (getSalesLiabilityAccount() == null
					&& (preferences.isTrackPaidTax() && getPurchaseLiabilityAccount() == null)) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_ENTER, Global.get()
								.messages().salesOrPurchaseLiabilityAcc());
			}
		}
		// Validations for duplicate contacts.
		checkDuplcateContacts();
	}

}
