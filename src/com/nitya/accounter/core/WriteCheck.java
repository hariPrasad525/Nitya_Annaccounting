package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

/**
 * A transaction which let us to write the checks to the payees. Payee involved
 * four types. {@link Customer},{@link Vendor},{@link TaxAgency} or
 * {@link TAXAgency}, {@link SalesPerson}. It also involves a payFrom account
 * which works a debit account for this transaction.
 * 
 * @author Chandan
 * 
 */
public class WriteCheck extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -672683575159440449L;

	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_TAX_AGENCY = 4;
	public static final int TYPE_EMPLOYEE = 3;

	public static final String IS_TO_BE_PRINTED = null;

	private Set<Estimate> estimates = new HashSet<Estimate>();

	/**
	 * The person to whom we are creating this write check
	 */
	int payToType;

	/**
	 * Through which account we are going to write Check
	 */
	@ReffereredObject
	Account bankAccount;

	/**
	 * The Customer to whom we are writing this check.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The Vendor to whom we are writing this check.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The TaxAgency to whom we are writing this check.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * Address of the Payee to whom we are writing this Check
	 */
	Address address;

	double amount;

	/**
	 * This will decide whether the Check is Issued or not
	 */
	boolean toBePrinted;

	SalesPerson salesPerson;

	/**
	 * String representation of the Check amount in words.
	 */
	String inWords;

	/**
	 * this is the Auto generated number to the check
	 */
	String checkNumber = WriteCheck.IS_TO_BE_PRINTED;

	String inFavourOf;

	//

	/**
	 * @return the id
	 */
	public WriteCheck() {
	}

	/**
	 * @return the payToType
	 */
	public int getPayToType() {
		return payToType;
	}

	/**
	 * @param payToType
	 *            the payToType to set
	 */
	public void setPayToType(int payToType) {
		this.payToType = payToType;
	}

	/**
	 * @return the bankAccount
	 */
	public Account getBankAccount() {
		return bankAccount;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the taxAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @return the inWords
	 */
	public String getInWords() {
		return inWords;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_WRITE_CHECK;
	}

	public void setBankAccount(Account bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;

	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int getTransactionCategory() {
		// if (this.getCustomer() != null)
		// return Transaction.CATEGORY_CUSTOMER;
		// else
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public Payee getInvolvedPayee() {
		if (customer != null)
			return this.customer;
		else if (vendor != null)
			return this.vendor;
		else
			return this.taxAgency;

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.paymentMethod = AccounterServerConstants.PAYMENT_METHOD_CHECK;
		if (!isToBePrinted()) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		setType(Transaction.TYPE_WRITE_CHECK);

		if (isDraftOrTemplate()) {
			this.estimates.clear();
			super.onSave(session);
			return false;
		}

		if (getCompany().getPreferences()
				.isProductandSerivesTrackingByCustomerEnabled()
				&& getCompany().getPreferences()
						.isBillableExpsesEnbldForProductandServices()) {

			createAndSaveEstimates(this.transactionItems, session);
		}

		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		WriteCheck writeCheck = (WriteCheck) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isBecameVoid()) {

			doVoidEffect(session, this);

		} else {

			for (Estimate estimate : writeCheck.getEstimates()) {
				session.delete(estimate);
			}
			writeCheck.getEstimates().clear();
			this.createAndSaveEstimates(this.transactionItems, session);
		}
		super.onEdit(writeCheck);

	}

	private void doVoidEffect(Session session, WriteCheck writecheck) {

		writecheck.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		for (Estimate estimate : writecheck.getEstimates()) {
			session.delete(estimate);
		}
		writecheck.estimates.clear();

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void createAndSaveEstimates(List<TransactionItem> transactionItems,
			Session session) {
		this.getEstimates().clear();

		Set<Estimate> estimates = new HashSet<Estimate>();
		for (TransactionItem transactionItem : transactionItems) {
			if (transactionItem.isBillable()
					&& transactionItem.getCustomer() != null) {
				TransactionItem newTransactionItem = new CloneUtil<TransactionItem>(
						TransactionItem.class).clone(null, transactionItem,
						false);
				newTransactionItem.setQuantity(transactionItem.getQuantity());
				newTransactionItem.setId(0);
				newTransactionItem.setTaxCode(null);
				newTransactionItem.setOnSaveProccessed(false);
				newTransactionItem.setLineTotal(newTransactionItem
						.getLineTotal() * getCurrencyFactor());
				newTransactionItem.setDiscount(newTransactionItem.getDiscount()
						* getCurrencyFactor());
				newTransactionItem.setUnitPrice(newTransactionItem
						.getUnitPrice() * getCurrencyFactor());
				newTransactionItem.setVATfraction(0.0D);
				Estimate estimate = getCustomerEstimate(estimates,
						newTransactionItem.getCustomer().getID());
				if (estimate == null) {
					estimate = new Estimate();
					estimate.setRefferingTransactionType(Transaction.TYPE_WRITE_CHECK);
					estimate.setCompany(getCompany());
					estimate.setCustomer(newTransactionItem.getCustomer());
					estimate.setJob(newTransactionItem.getJob());
					estimate.setTransactionItems(new ArrayList<TransactionItem>());
					estimate.setEstimateType(Estimate.BILLABLEEXAPENSES);
					estimate.setType(Transaction.TYPE_ESTIMATE);
					estimate.setDate(new FinanceDate());
					estimate.setExpirationDate(new FinanceDate());
					estimate.setDeliveryDate(new FinanceDate());
					estimate.setNumber(NumberUtils.getNextTransactionNumber(
							Transaction.TYPE_ESTIMATE, getCompany()));
				}
				List<TransactionItem> transactionItems2 = estimate
						.getTransactionItems();
				transactionItems2.add(newTransactionItem);
				estimate.setTransactionItems(transactionItems2);
				estimates.add(estimate);
			}
		}

		for (Estimate estimate : estimates) {
			session.save(estimate);
		}

		this.setEstimates(estimates);

	}

	private Estimate getCustomerEstimate(Set<Estimate> estimates, long customer) {
		for (Estimate clientEstimate : estimates) {
			if (clientEstimate.getCustomer().getID() == customer) {
				return clientEstimate;
			}
		}
		return null;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		super.canEdit(clientObject, goingToBeEdit);
		for (Estimate estimate : this.getEstimates()) {
			if (estimate.getUsedInvoice() != null) {
				throw new AccounterException(AccounterException.USED_IN_INVOICE);
			}
		}
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(WriteCheck.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		if (isDraft()) {
			return true;
		}
		if ((this.bankAccount.equals("Un Deposited Funds"))
				&& this.transactionMakeDepositEntries != null) {
			throw new AccounterException(
					AccounterException.ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS);
			// "You can't void or edit because it has been deposited from Undeposited Funds");
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.writeCheck()).gap();

		w.put(messages.payTo(), this.payToType).gap();

		if (this.bankAccount != null)
			w.put(messages.bankAccount(), this.bankAccount.getName());

		if (this.customer != null)
			w.put(messages.customer(), this.customer.getName()).gap();

		if (this.vendor != null)
			w.put(messages.Vendor(), this.vendor.getName());

		if (this.taxAgency != null)
			w.put(messages.taxAgencie(), this.taxAgency.getName()).gap();

		if (this.address != null)
			w.put(messages.address(), this.address.toString());

		w.put(messages.amount(), this.amount).gap();

		if (this.salesPerson != null)
			w.put(messages.salesPerson(), this.salesPerson.getName());

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (bankAccount == null) {
			valid = false;
		} else if (transactionItems != null && !transactionItems.isEmpty()) {
			for (TransactionItem item : transactionItems) {
				if (!item.isValid()) {
					valid = false;
					break;
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

	public String getInFavourOf() {
		return inFavourOf;
	}

	public void setInFavourOf(String inFavourOf) {
		this.inFavourOf = inFavourOf;
	}

	public Set<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(Set<Estimate> estimates) {
		this.estimates = estimates;
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		WriteCheck bill = (WriteCheck) super.clone();
		bill.estimates = new HashSet<Estimate>();
		bill.status = 0;
		if (!isToBePrinted()) {
			bill.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		return bill;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem tItem : getTransactionItems()) {
			boolean isTaxable = tItem.isTaxable() && tItem.getTaxCode() != null;
			double amount = isTaxable && tItem.isAmountIncludeTAX() ? tItem
					.getLineTotal() - tItem.getVATfraction() : tItem
					.getLineTotal();
			// This is Not Positive Transaction
			amount = -amount;
			switch (tItem.getType()) {
			case TransactionItem.TYPE_ACCOUNT:
				e.add(tItem.getAccount(), amount);
				break;
			case TransactionItem.TYPE_ITEM:
				Item item = tItem.getItem();
				if (item.isInventory()) {
					e.add(item, tItem.getQuantity(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
					double calculatePrice = tItem.getQuantity().calculatePrice(
							tItem.getUnitPriceInBaseCurrency());
					e.add(item.getAssestsAccount(), -calculatePrice, 1);

					// ADDING INVENTORY HISTORY
					e.addInventoryHistory(item, tItem.getQuantity(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
				} else {
					e.add(item.getExpenseAccount(), amount);
				}
				break;
			default:
				break;
			}
			if (isTaxable) {
				TAXItemGroup taxItemGroup = tItem.getTaxCode()
						.getTAXItemGrpForPurchases();
				e.add(taxItemGroup, amount);
			}
		}
		e.add(bankAccount, getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(bankAccount, Global.get().messages().bankAccount());

		if (inFavourOf == null || inFavourOf.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().inFavourOf());
		}
		checkTransactionItemsNull();
	}
}
