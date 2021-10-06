package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Cash Sale</b><br>
 * <br>
 *         <b><i>Effect on Transaction Item</i></b><br>
 * <br>
 *         ===============================>Item <br>
 * 
 *         If Item's IncomeAccount isIncrease true then Increasae the current
 *         and total balance by line total otherwise Decrease.<br>
 * <br>
 *         ===============================>Account <br>
 * 
 *         If Account isIncrease true then Increasae the current and total
 *         balance by line total otherwise Decrease.<br>
 * <br>
 *         ===============================>Sales Tax <br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Increase the current and total balance by line total
 *         otherwise decrease. Specified TaxCode's all Tax Agency's Balance will
 *         Increase by line total . <br>
 * <br>
 *         <b><i>Effect on Tax Group</i></b><br>
 * <br>
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Increase the current and total balance by sales
 *         tax collected for each taxcode otherwise decrease. Specified
 *         TaxGroup's all TaxCode's Tax Agency's Balance will Increase by sales
 *         tax collected for each taxcode. <br>
 * <br>
 *         <b><i> Effect on Deposited In Account</i></b><br>
 * 
 *         If Specified Deposit In Account(cash|Bank|Credit Card) isIncrease
 *         true then Decrease the current and total balance by CashSale total
 *         otherwise Increase
 * 
 * 
 */
public class CashSales extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The payee to whom the sales is being done.
	 */
	@ReffereredObject
	Customer customer;

	String checkNumber;
	/**
	 * The contact of the payee. (His alternate phone number, email, primary
	 * address etc are entered
	 */
	Contact contact;

	/**
	 * This is the address to where we have to send the bill for the sale. This
	 * can different from payee's primary address.
	 */
	Address billingAddress;

	/**
	 * This is the address to where we have to send the goods which he ordered
	 * in the sale. This can different from payee's primary address and billing
	 * address.
	 */
	Address shippingAdress;

	String phone;

	/**
	 * This can be used whenever the payee is assigned with a dedicated Sales
	 * person for all the sales. We can change this person as well.
	 */
	SalesPerson salesPerson;

	/**
	 * This account is used to deposit the cash-sale total.
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * We can specify any shipping term on the request of customer
	 */
	ShippingTerms shippingTerm;

	/**
	 * The method how the shipping of the goods is done to the customer.
	 */
	ShippingMethod shippingMethod;

	/**
	 * The date on/before which the goods has to be reached to the customer.
	 */
	FinanceDate deliverydate;

	/**
	 * For loyal dedicated customers we can mention a price level. By this price
	 * level the overall price of the cash sale can be reduced to a percentage
	 * mentioned in the price level.
	 */
	PriceLevel priceLevel;

	/**
	 * SalesOrder from which this CahsSale was Recreated
	 * 
	 * @see SalesOrder
	 */
	private List<Estimate> salesOrders = new ArrayList<Estimate>();

	double taxTotal = 0D;

	double discountTotal = 0D;

	double roundingTotal = 0D;

	/**
	 * Constructor of CashSales
	 */
	public CashSales() {

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
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public Address getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(Address shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	// public void setPhone(String phone) {
	// this.phone = phone;
	// }
	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the shippingTerm
	 */
	public ShippingTerms getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(ShippingTerms shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the delivery FinanceDate
	 */
	public FinanceDate getDeliverydate() {
		return deliverydate;
	}

	public void setDeliverydate(FinanceDate deliverydate) {
		this.deliverydate = deliverydate;
	}

	/**
	 * @param deliverydate
	 *            the delivery date to set
	 */
	// public void setDeliverydate(Date delivery date) {
	// this.deliverydate = delivery date;
	// }
	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(PriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	// public void setSalesTax(double salesTax) {
	// this.salesTax = salesTax;
	// }
	/**
	 * @return the allLineTotal
	 */
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @param allLineTotal
	 *            the allLineTotal to set
	 */
	// public void setAllLineTotal(double allLineTotal) {
	// this.allLineTotal = allLineTotal;
	// }
	/**
	 * @return the allTaxableLineTotal
	 */
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @param allTaxableLineTotal
	 *            the allTaxableLineTotal to set
	 */
	// public void setAllTaxableLineTotal(double allTaxableLineTotal) {
	// this.allTaxableLineTotal = allTaxableLineTotal;
	// }
	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the to set
	 */
	// public void setDiscountTotal(double discountTotal) {
	// this.discountTotal = discountTotal;
	// }

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	// public void setIsVoid(boolean isVoid) {
	// this.isVoid = isVoid;
	// }
	/**
	 * @return the isDeposited
	 */
	@Override
	public boolean getIsDeposited() {
		return isDeposited;
	}

	/**
	 * @param isDeposited
	 *            the isDeposited to set
	 */
	// public void setIsDeposited(boolean isDeposited) {
	// this.isDeposited = isDeposited;
	// }
	// @Override
	// public void setTransactionItems(List<TransactionItem> transactionItems) {
	// super.setTransactionItems(transactionItems);
	//
	// this.allLineTotal = getLineTotalSum();
	// this.allTaxableLineTotal = getTaxableLineTotalSum();
	// this.discountTotal = getDiscountTotalSum();
	//
	// }
	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		isOnSaveProccessed = true;
		if (isDraftOrTemplate()) {
			if (this.salesOrders != null) {
				this.salesOrders.clear();
			}
			super.onSave(session);
			return false;
		}

		addSalesOrdersTransactionItems(this, true);
		if (!(this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
				&& !(this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		return super.onSave(session);
	}

	private void addSalesOrdersTransactionItems(CashSales cashSales,
			boolean isCreated) {
		Session session = HibernateUtil.getCurrentSession();
		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}

		if (cashSales.getSalesOrders() == null)
			return;

		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}
		for (Estimate salesOrder : cashSales.getSalesOrders()) {
			salesOrder = (Estimate) session.get(Estimate.class,
					salesOrder.getID());

			// int executeUpdate = session
			// .getNamedQuery("delete.Estimate.from.drafts")
			// .setLong("estimateId", estimate.getID()).executeUpdate();

			if (salesOrder != null) {

				boolean isPartiallyInvoiced = false;

				if (cashSales.transactionItems != null
						&& cashSales.transactionItems.size() > 0) {
					isPartiallyInvoiced = updateReferringTransactionItems(
							cashSales, isCreated);
				}
				/**
				 * Updating the Status of the Sales Order involved in this
				 * Invoice depending on the above Analysis.
				 */
				if (!isPartiallyInvoiced) {
					double usdAmount = 0;
					for (TransactionItem orderTransactionItem : salesOrder.transactionItems) {
						// if (orderTransactionItem.getType() != 6)
						usdAmount += orderTransactionItem.usedamt;
					}
					// else
					// usdAmount += orderTransactionItem.lineTotal;
					if (DecimalUtil.isLessThan(usdAmount, salesOrder.netAmount))
						isPartiallyInvoiced = true;
				}
				if (isCreated) {

					if (!this.isVoid()) {
						salesOrder.setUsedCashSale(cashSales, session);
					}
				}
				salesOrder.onUpdate(session);
				session.saveOrUpdate(salesOrder);
			}
		}
	}

	private boolean updateReferringTransactionItems(CashSales cashSales,
			boolean isCreated) {
		Session session = HibernateUtil.getCurrentSession();
		boolean isPartiallyInvoiced = true;
		boolean flag = true;
		for (TransactionItem transactionItem : cashSales.transactionItems) {
			/**
			 * This is to know whether this transaction item is of new one or
			 * it's came from any Sales Order.
			 */

			if (transactionItem.getReferringTransactionItem() != null) {
				TransactionItem referringTransactionItem = (TransactionItem) session
						.get(TransactionItem.class, transactionItem
								.getReferringTransactionItem().getID());
				double amount = 0d;

				if (!isCreated) {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt -= transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt -= transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt -= transactionItem.lineTotal;

				} else {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt += transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt += transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt += transactionItem.lineTotal;
				}
				amount = referringTransactionItem.usedamt;
				/**
				 * This is to save changes to the invoiced amount of the
				 * referring transaction item to this transaction item.
				 */
				session.update(referringTransactionItem);

				if (flag
						&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
								.getQuantity().compareTo(
										referringTransactionItem.getQuantity()) < 0)))) {
					if (isCreated ? DecimalUtil.isLessThan(amount,
							referringTransactionItem.lineTotal) : DecimalUtil
							.isGreaterThan(amount, 0)) {
						isPartiallyInvoiced = true;
						flag = false;
					}
				}
				// if (id != 0l && !invoice.isVoid())
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;

			}

		}
		return isPartiallyInvoiced;
	}

	@Override
	public boolean isDebitTransaction() {
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
		//
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		// }
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public String toString() {

		return AccounterServerConstants.TYPE_CASH_SALES;
	}

	/**
	 * Comparison of two cash sales
	 * 
	 * @param CashSale
	 * 
	 * @return True if both objects are equal else false
	 */
	// public boolean equals(CashSales cs) {
	//
	// if (this.customer.equals(cs.customer)
	// && this.contact.equals(cs.contact)
	// && this.billingAddress.equals(billingAddress)
	// && this.shippingAdress.equals(shippingAdress)
	// && this.phone.equals(cs.phone)
	// && this.salesPerson.equals(cs.salesPerson)
	// && this.paymentMethod.equals(cs.paymentMethod)
	// && this.depositIn.equals(cs.depositIn)
	// && this.shippingTerm.equals(cs.shippingTerm)
	// && this.shippingMethod.equals(cs.shippingMethod)
	// && this.deliverydate.equals(cs.deliverydate)
	// && this.memo.equals(cs.memo)
	// && this.reference.equals(cs.reference)
	// && this.priceLevel.equals(cs.priceLevel)
	// && this.salesTax == cs.salesTax
	// // && this.allLineTotal == cs.allLineTotal
	// // && this.allTaxableLineTotal == cs.allTaxableLineTotal
	// && this.discountTotal == cs.discountTotal)
	// return true;
	// return false;
	// }

	public void setTaxTotal(double salesTax) {
		this.taxTotal = salesTax;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public Payee getInvolvedPayee() {
		return this.customer;
	}

	/**
	 * This method is called when we save in the edit mode
	 * 
	 * @throws AccounterException
	 */

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		CashSales cashSales = (CashSales) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			super.onEdit(cashSales);
			return;
		}
		/**
		 * 
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then only it will entered into the loop
		 */
		if (this.isVoid() && !cashSales.isVoid()) {
			doVoidEffect(session, this);
		} else {

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

			doUpdateEffectEstiamtes(this, cashSales, session);

		}

		super.onEdit(clonedObject);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, CashSales cashSale) {

		cashSale.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		for (Estimate estimate : cashSale.getSalesOrders()) {
			Estimate est = (Estimate) session.get(Estimate.class,
					estimate.getID());
			est.setUsedCashSale(null, session);
			session.saveOrUpdate(est);
		}
	}

	private void doUpdateEffectEstiamtes(CashSales cashSale,
			CashSales oldCashSale, Session session) {
		List<Estimate> estimatesExistsInOldInvoice = new ArrayList<Estimate>();
		for (Estimate oldEstiamte : oldCashSale.getSalesOrders()) {
			Estimate est = null;
			for (Estimate newEstimate : cashSale.getSalesOrders()) {
				if (oldEstiamte.getID() == newEstimate.getID()) {
					est = newEstimate;
					estimatesExistsInOldInvoice.add(newEstimate);
					break;
				}
			}
			if (est != null && !this.isVoid()) {
				est.setUsedCashSale(cashSale, session);
			} else {
				est = (Estimate) session.get(Estimate.class,
						oldEstiamte.getID());
				est.setUsedCashSale(null, session);
			}
			if (est != null) {
				session.saveOrUpdate(est);
			}
		}

		for (Estimate est : cashSale.getSalesOrders()) {
			if (!estimatesExistsInOldInvoice.contains(est) && !this.isVoid()) {
				est.setUsedCashSale(cashSale, session);
				session.saveOrUpdate(est);
			}
		}
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

		if (!UserUtils.canDoThis(CashSales.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (isBecameVoid()) {
			for (TransactionItem item : getTransactionItems()) {
				if (item.referringTransactionItem != null) {
					throw new AccounterException(
							AccounterException.ERROR_INVOICE_USED_IN_ESTIMATES);
				}
			}
		}

		return super.canEdit(clientObject, goingToBeEdit);

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.cashSale()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total).gap();
		if (this.paymentMethod != null)
			w.put(messages.paymentMethod(), this.paymentMethod);

		if (this.customer != null)
			w.put(messages.customer(), this.customer.getName()).gap();

		if (this.contact != null)
			w.put(messages.contactName(), this.contact.getName());

		w.put(messages.total(), this.total).gap();

		if (this.paymentMethod != null)
			w.put(messages.paymentMethod(), this.paymentMethod);

		if (this.billingAddress != null)
			w.put(messages.billingAddress(), this.billingAddress.toString())
					.gap();
		if (this.shippingAdress != null)
			w.put(messages.shippingAddress(), this.shippingAdress.toString());

		w.put(messages.phone(), this.phone).gap();

		if (this.salesPerson != null)
			w.put(messages.salesPersonName(), this.salesPerson.getName());

		if (this.depositIn != null)
			w.put(messages.depositAccount(), this.depositIn.getName()).gap();
		if (this.shippingTerm != null)
			w.put(messages.shippingTerm(), this.shippingTerm.getName());

		w.put(messages.paymentMethod(), this.checkNumber).gap();
		if (this.shippingMethod != null)
			w.put(messages.shippingMethod(), this.shippingMethod.getName())
					.gap();
		w.put(messages.deliveryDate(), this.deliverydate.toString());

		if (this.priceLevel != null)
			w.put(messages.priceLevel(), this.priceLevel.toString()).gap();

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (depositIn == null) {
			valid = false;
		} else if (paymentMethod == null || paymentMethod.isEmpty()) {
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

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public List<Estimate> getSalesOrders() {
		return salesOrders;
	}

	public void setSalesOrders(List<Estimate> salesOrders) {
		this.salesOrders = salesOrders;
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		CashSales sale = (CashSales) super.clone();
		sale.salesOrders = new ArrayList<Estimate>();
		return sale;

	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem tItem : getTransactionItems()) {
			boolean isTaxable = tItem.isTaxable() && tItem.getTaxCode() != null;
			double amount = isTaxable && tItem.isAmountIncludeTAX() ? tItem
					.getLineTotal() - tItem.getVATfraction() : tItem
					.getLineTotal();
			switch (tItem.getType()) {
			case TransactionItem.TYPE_ACCOUNT:
				e.add(tItem.getAccount(), amount);
				break;
			case TransactionItem.TYPE_ITEM:
				Item item = tItem.getItem();
				e.add(item.getIncomeAccount(), amount);
				if (item.isInventory()) {
					e.add(item, tItem.getQuantity().reverse(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
					tItem.addPurchasesEffects(e);
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
		e.add(getCompany().getPreferences().getRoundingAccount(), roundingTotal);
		e.add(getDepositIn(), -getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkPaymentMethodNull();
		checkAccountNull(depositIn, Global.get().messages().depositIn());
		if (this.getSalesOrders().isEmpty()) {
			checkTransactionItemsNull();
		} else {
			if (getID() == 0) {
				Set<String> features = getCompany().getFeatures();
				boolean salesOrder = features.contains(Features.SALSE_ORDER);
				if (!this.getSalesOrders().isEmpty()) {
					if (!salesOrder) {
						throw new AccounterException(
								AccounterException.ERROR_PERMISSION_DENIED,
								"You can't use salese order");
					}
				}

				boolean creditsCharges = features
						.contains(Features.CREDITS_CHARGES);
				if (this.creditsAndPayments != null && creditsCharges) {
					if (!salesOrder) {
						throw new AccounterException(
								AccounterException.ERROR_PERMISSION_DENIED,
								"You can't use credits and charges");
					}
				}
				boolean billableExcepence = features
						.contains(Features.BILLABLE_EXPENSE);

			}
		}

		if (!(this.transactionItems.isEmpty())) {
			checkTransactionItemsNull();
		}
		checkNetAmountNegative();

	}

	public double getRoundingTotal() {
		return roundingTotal;
	}

	public void setRoundingTotal(double roundingTotal) {
		this.roundingTotal = roundingTotal;
	}
}
