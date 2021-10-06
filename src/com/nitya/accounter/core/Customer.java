package com.nitya.accounter.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
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

public class Customer extends Payee implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String number;

	// as we are not implementing Employee section, it does not have any
	// functionality.
	SalesPerson salesPerson;

	double creditLimit = 0D;
	/**
	 * The default Price Level which can be applied on the Items sales price
	 * when ever a customer being used in any Transaction.
	 * 
	 * @see PriceLevel
	 */
	PriceLevel priceLevel;

	CreditRating creditRating;

	Set<Job> jobs = new HashSet<Job>();

	/**
	 * The default Shipping method through which a Transaction should done.
	 * 
	 * @see ShippingMethod
	 */
	ShippingMethod shippingMethod;
	/**
	 * The default Payment Term which can be applied on any Transaction when
	 * ever a Customer being selected.
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * The Customer Group through which this customer is categorised.
	 */
	CustomerGroup customerGroup;

	/**
	 * The default Tax which should be applied on a Transaction immediately
	 * after selecting a Customer.
	 * 
	 * @see TaxGroup
	 * @see TaxCode
	 * @see TaxAgency
	 */
	TAXGroup taxGroup;

	/*
	 * ========================================================================
	 * The following fields are not used yet. Because we have not implemented
	 * all tabs of the Customer creation in MS Accounting.
	 * ===========================================================
	 */
	// Balance due fields
	double current = 0D;
	double overDueOneToThirtyDays = 0D;
	double overDueThirtyOneToSixtyDays = 0D;
	double overDueSixtyOneToNintyDays = 0D;
	double overDueOverNintyDays = 0D;
	double overDueTotalBalance = 0D;

	// Payment Information

	int averageDaysToPay;
	int averageDaysToPayYTD;

	// Sales Information

	double monthToDate = 0D;
	double yearToDate = 0D;
	double lastYear = 0D;
	double lifeTimeSales = 0D;

	private boolean willDeductTDS;

	/*
	 * =================================================================
	 */
	// transient Customer previousCustomer;

	public Customer() {
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setCreditRating(CreditRating creditRating) {
		this.creditRating = creditRating;
	}

	public void setPriceLevel(PriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the creditRating
	 */
	public CreditRating getCreditRating() {
		return creditRating;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 
	 * @param shippingMethod
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the customerGroup
	 */
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	/**
	 * @return the taxGroup
	 */
	public TAXGroup getTaxGroup() {
		return taxGroup;
	}

	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}

	/**
	 * @return the overDueOneToThirtyDays
	 */
	public double getOverDueOneToThirtyDays() {
		return overDueOneToThirtyDays;
	}

	/**
	 * @return the overDueThirtyOneToSixtyDays
	 */
	public double getOverDueThirtyOneToSixtyDays() {
		return overDueThirtyOneToSixtyDays;
	}

	/**
	 * @return the overDueSixtyOneToNintyDays
	 */
	public double getOverDueSixtyOneToNintyDays() {
		return overDueSixtyOneToNintyDays;
	}

	/**
	 * @return the overDueOverNintyDays
	 */
	public double getOverDueOverNintyDays() {
		return overDueOverNintyDays;
	}

	/**
	 * @return the overDueTotalBalance
	 */
	public double getOverDueTotalBalance() {
		return overDueTotalBalance;
	}

	/**
	 * @return the averageDaysToPay
	 */
	public int getAverageDaysToPay() {
		return averageDaysToPay;
	}

	/**
	 * @return the averageDaysToPayYTD
	 */
	public int getAverageDaysToPayYTD() {
		return averageDaysToPayYTD;
	}

	/**
	 * @return the monthToDate
	 */
	public double getMonthToDate() {
		return monthToDate;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @return the lastYear
	 */
	public double getLastYear() {
		return lastYear;
	}

	/**
	 * @return the lifeTimeSales
	 */
	public double getLifeTimeSales() {
		return lifeTimeSales;
	}

	/**
	 * @param openingBalanceAccount
	 *            the openingBalanceAccount to set
	 */
	// public void setOpeningBalanceAccount(Account openingBalanceAccount) {
	// this.openingBalanceAccount = openingBalanceAccount;
	// }
	public Contact getPrimaryContact() {

		Contact primaryContact = null;

		if (this.contacts != null) {
			for (Contact contact : contacts) {
				if (contact.isPrimary())
					primaryContact = contact;
			}
		}

		return primaryContact;

	}

	public Set<String> getContactsPhoneList() {

		Set<String> phnos = new HashSet<String>();

		if (this.contacts != null) {

			for (Contact contact : contacts) {

				phnos.add(contact.getBusinessPhone());
			}

		}

		return phnos;

	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.CUSTOMER);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(session);
		isOnSaveProccessed = true;
		setType(TYPE_CUSTOMER);
		// SessionUtils.updateReferenceCount(null, this, session, true);

		// Logging the Customer info.
		if (this.number == null || this.number.trim().isEmpty()) {
			this.number = NumberUtils.getNextAutoCustomerNumber(getCompany());
		}
		return onUpdate(session);
	}

	@Override
	public Account getAccount() {
		return getCurrency().getAccountsReceivable();
	}

	/**
	 * long id;
	 * 
	 * String name;
	 * 
	 * String fileAs;
	 * 
	 * Set<Address> address;
	 * 
	 * Set<Phone> phoneNumbers;
	 * 
	 * Set<Fax> faxNumbers;
	 * 
	 * Set<Email> emails;
	 * 
	 * String webPageAddress;
	 * 
	 * boolean isActive = Boolean.TRUE;
	 * 
	 * Date customerSince;
	 * 
	 * Date balanceAsOf;
	 * 
	 * Set<Contact> contacts;
	 * 
	 * String memo;
	 * 
	 * SalesPerson salesPerson;
	 * 
	 * double creditLimit = 0D;
	 * 
	 * PriceLevel priceLevel;
	 * 
	 * CreditRating creditRating;
	 * 
	 * ShippingMethod shippingMethod;
	 * 
	 * PaymentMethod paymentMethod;
	 * 
	 * PaymentTerms paymentTerm;
	 * 
	 * CustomerGroup customerGroup;
	 * 
	 * TaxGroup taxGroup;
	 * 
	 * // Balance due fields
	 * 
	 * double current = 0D;
	 * 
	 * double overDueOneToThirtyDays = 0D;
	 * 
	 * double overDueThirtyOneToSixtyDays = 0D;
	 * 
	 * double overDueSixtyOneToNintyDays = 0D;
	 * 
	 * double overDueOverNintyDays = 0D;
	 * 
	 * double overDueTotalBalance = 0D;
	 * 
	 * 
	 * 
	 * // Payment Information
	 * 
	 * int averageDaysToPay;
	 * 
	 * int averageDaysToPayYTD;
	 * 
	 * 
	 * // Sales Information
	 * 
	 * double monthToDate = 0D;
	 * 
	 * double yearToDate = 0D;
	 * 
	 * double lastYear = 0D;
	 * 
	 * double lifeTimeSales = 0D;
	 */

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		super.canEdit(clientObject, goingToBeEdit);
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(Customer.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Customer customer = (Customer) clientObject;
		// if (this.name.equals(customer.name)
		// && this.number.equals(customer.number)
		// && this.id == customer.id)
		// return true;
		//
		// else {

		// Query query = session
		// .createQuery(
		// "from com.nitya.accounter.core.Customer C where C.number=?")
		// .setParameter(0, this.number);
		//
		// List list = query.list();
		//
		// if (list != null && list.size() > 0) {
		//
		// for (int i = 0; i < list.size(); i++) {
		// Customer newCustomer = (Customer) list.get(i);
		// if ((this.name.equals(newCustomer.name) ||
		// this.number.equals(newCustomer.number))
		// && this.id != newCustomer.id) {
		// throw new InvalidOperationException(
		// "Customer name or number is already in use Please enter Unique");
		// }
		// }
		// }
		FlushMode flushMode = session.getFlushMode();

		session.setFlushMode(FlushMode.COMMIT);
		try {
			Query query = session
					.getNamedQuery("getCustomers")
					.setParameter("name", this.name,
							EncryptedStringType.INSTANCE)
					.setParameter(
							"number",
							this.number == null || this.number.isEmpty() ? null
									: this.number, EncryptedStringType.INSTANCE)
					.setLong("id", this.getID())
					.setParameter("companyId", customer.getCompany().getID());

			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();

				if (this.name.equalsIgnoreCase((String) object[0])) {
					Iterator it2 = list.iterator();
					while (it2.hasNext()) {
						Object[] object2 = (Object[]) it2.next();
						if (number != null && this.number.equals(object2[1])) {
							throw new AccounterException(
									AccounterException.ERROR_NAME_CONFLICT);
							// "A Customer already exists with this name and number");
						}
					}
					throw new AccounterException(
							AccounterException.ERROR_NAME_CONFLICT);
					// "A Customer already exists with this name");
				} else if (this.number.equals(object[1])) {
					Iterator it2 = list.iterator();
					while (it2.hasNext()) {
						Object[] object2 = (Object[]) it2.next();
						if (this.name.equalsIgnoreCase((String) object2[0])) {
							throw new AccounterException(
									AccounterException.ERROR_NUMBER_CONFLICT);
							// "A Customer already exists with this name and number");
						}
					}
					throw new AccounterException(
							AccounterException.ERROR_NUMBER_CONFLICT);
					// "A Customer already exists with this number");
				}
			}
		} finally {
			session.setFlushMode(flushMode);
		}
		return true;
	}

	@Override
	public String getPayeeName() {
		return Global.get().customer();
	}

	@Override
	public int getObjType() {
		return IAccounterCore.CUSTOMER;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.customer()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.name(), this.name);
		w.put(messages.currency(), this.currencyFactor).gap();

		if (this.paymentMethod != null)
			w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo);

		if (this.priceLevel != null)
			w.put(messages.priceLevel(), this.priceLevel.getName());

		if (this.creditRating != null)
			w.put(messages.creditRating(), this.creditRating.getName());

		if (this.shippingMethod != null)
			w.put(messages.shippingMethod(), this.shippingMethod.getName());

		if (this.paymentTerm != null)
			w.put(messages.paymentTerm(), this.paymentTerm.getName());

		if (this.customerGroup != null)
			w.put(messages.customergroup(), this.customerGroup.getName());

		if (this.taxGroup != null)
			w.put(messages.taxGroup(), this.taxGroup.getName());

	}

	/**
	 * @return the willDeductTDS
	 */
	public boolean isWillDeductTDS() {
		return willDeductTDS;
	}

	/**
	 * @param willDeductTDS
	 *            the willDeductTDS to set
	 */
	public void setWillDeductTDS(boolean willDeductTDS) {
		this.willDeductTDS = willDeductTDS;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
	}

}
