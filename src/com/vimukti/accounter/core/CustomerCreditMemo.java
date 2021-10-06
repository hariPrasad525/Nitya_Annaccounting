package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Customer Credit Memo </b><br>
 * <br>
 *         <b><i>Effect on Transaction Item </i></b><br>
 * <br>
 *         ===============================>Item <br>
 * 
 *         If Item's IncomeAccount isIncrease true then Decrease the current and
 *         total balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         ===============================>Account <br>
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         ===============================>Sales Tax <br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Decrease the current and total balance by line total
 *         otherwise Increase. Specified TaxCode's all Tax Agency's Balance will
 *         Decrease by line total .<br>
 * <br>
 *         <b><i>Effect on Tax Group</i></b><br>
 * 
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Decrease the current and total balance by sales
 *         tax collected for each taxcode otherwise Increase. Specified
 *         TaxGroup's TaxCode's all Tax Agency's Balance will Decrease by by
 *         sales tax collected for each taxcode.<br>
 * <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Receivable account current and total balance will Decrease
 *         by the CCM total. Customer balance should Decrease by the CCM total.<br>
 * 
 * <br>
 *         <b><i>Effect on CreditsAndPayments:</i></b><br>
 * 
 *         New Credit and Payment is created for this customer with Credit
 *         Amount and Balance as CCM total and Memo as transaction number
 *         followed byCustomerCreditMemo
 * 
 * 
 */

/**
 * @author vimukti16
 * 
 */
public class CustomerCreditMemo extends Transaction implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2197852881832579241L;

	/**
	 * The Customer to whom we are crediting this amount.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * This is the one of the chosen {@link Contact} of the {@link Customer}
	 */
	Contact contact;

	/**
	 * This is the chosen {@link Customer} Address among all the address of
	 * Customer
	 * 
	 * @see Address
	 * 
	 */
	Address billingAddress;

	/**
	 * This defaults to the chosen Customer's primary contact Business Phone
	 * number.
	 * 
	 * @see Customer
	 * @see Contact
	 */
	String phone;

	SalesPerson salesPerson;
	@ReffereredObject
	Account account;

	/**
	 * This will Decrease or Increase the Price of the Item selected in this
	 * Transaction. The default value of this Price level is default's to the
	 * chosen {@link Customer} User can choose any Price Level later
	 * 
	 * @see Item
	 */
	PriceLevel priceLevel;

	/**
	 * The total Sales Tax collected on the whole Transaction.
	 */
	double taxTotal = 0D;

	/**
	 * The total of the discounts applied on Each {@link TransactionItem}
	 */
	double discountTotal = 0D;

	double balanceDue = 0D;

	//

	public CustomerCreditMemo() {
	}

	public CustomerCreditMemo(Session session,
			ClientCustomerCreditMemo customercreditmemo) {
		this.type = Transaction.TYPE_CUSTOMER_CREDIT_MEMO;

	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @return the allLineTotal
	 */
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @return the allTaxableLineTotal
	 */
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	@Override
	public void setTransactionItems(List<TransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		// this.allLineTotal = getLineTotalSum();
		// this.allTaxableLineTotal = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);

		// Inserting this Customer Credit Memo entry in to Credits And Payments
		// table.
		if (!isDraftOrTemplate()
				&& DecimalUtil.isGreaterThan(this.getTotal(), 0.0)) {
			this.balanceDue = this.total;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}

		return false;

	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		//
		// if (isBecameVoid()) {
		// // this.creditsAndPayments.setTransaction(null);
		// // session.delete(this.creditsAndPayments);
		// this.creditsAndPayments = null;
		// this.balanceDue = 0.0;
		// }
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
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setTaxTotal(double salesTax) {
		this.taxTotal = salesTax;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;

	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CUSTOMER_CREDIT_MEMO;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	// @Override
	// public boolean canEdit(IAccounterServerCore clientObject)
	// throws InvalidOperationException {
	// //
	// return false;
	// }

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		CustomerCreditMemo customerCreditMemo = (CustomerCreditMemo) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			super.onEdit(customerCreditMemo);
			return;
		}
		// Customer cust = (Customer) session.get(Customer.class,
		// customerCreditMemo.customer.id);
		// cust.updateBalance(session, clonedObject, -customerCreditMemo.total);
		// session.saveOrUpdate(cust);

		this.cleanTransactionitems(this);

		/**
		 * Checking that whether two customers are same are not. If they are not
		 * same then update clonedObject customer and New customer balances same
		 * customers.
		 */

		if (this.customer.getID() != customerCreditMemo.getCustomer().getID()) {

			voidCreditsAndPayments(customerCreditMemo);

			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);

			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);

		}

		if ((this.customer.getID() == customerCreditMemo.customer.getID())
				&& (!DecimalUtil.isEquals(this.total, customerCreditMemo.total))) {
			this.creditsAndPayments.updateCreditPayments(this.total);
		}

		super.onEdit(clonedObject);

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(CustomerCreditMemo.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.customerCreditNote(messages.customer()))
				.gap();

		w.put(messages.no(), this.number);

		if (this.transactionDate != null)
			w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();

		w.put(messages.memo(), this.memo);

		if (this.transactionItems != null)
			w.put(messages.details(), this.transactionItems);

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (customer == null) {
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
				e.add(item.getIncomeAccount(), amount);
				if (item.isInventory()) {
					// Doing PurchaseEffect for CustomerCreditMemo
					e.add(item, tItem.getQuantity().reverse(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
					double purchaseCost = tItem.getQuantity().calculate(
							tItem.getUnitPriceInBaseCurrency());
					double disc = tItem.getDiscount();
					purchaseCost = DecimalUtil.isGreaterThan(disc, 0) ? (purchaseCost - (purchaseCost
							* disc / 100))
							: purchaseCost;
					e.add(item.getAssestsAccount(), -purchaseCost, 1);
					e.add(item.getExpenseAccount(), purchaseCost, 1);

					// ADDING INVENTORY HISTORY
					e.addInventoryHistory(item, tItem.getQuantity(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
				}
				break;
			default:
				break;
			}
			if (isTaxable) {
				TAXItemGroup taxItemGroup = tItem.getTaxCode()
						.getTAXItemGrpForSales();
				e.add(taxItemGroup, amount);
			}

		}
		e.add(getCustomer(), getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingCustomerNull(customer, Global.get().customer());
		checkTransactionItemsNull();
		checkNetAmountNegative();

	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
