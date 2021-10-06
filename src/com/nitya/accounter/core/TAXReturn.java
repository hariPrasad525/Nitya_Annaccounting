package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;

public class TAXReturn extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int VAT_RETURN_UK_VAT = 1;
	public static final int VAT_RETURN_IRELAND = 2;
	public static final int VAT_RETURN_NONE = 3;

	/** The Start date of this TAX Return */
	FinanceDate periodStartDate;

	/** The End date of this TAX Return */
	FinanceDate periodEndDate;

	/** TAX Returning Date */
	FinanceDate returningDate;

	/**
	 * The TAX Agency to which we are going to create this TAX Return
	 */
	TAXAgency taxAgency;

	/** TAX Return Balance */
	double balance;

	double totalTAXAmount;

	double salesTaxTotal;

	double purchaseTaxTotal;

	List<Box> boxes = new ArrayList<Box>();

	List<TAXReturnEntry> taxReturnEntries = new ArrayList<TAXReturnEntry>();

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	public FinanceDate getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(FinanceDate tperiodStartDate) {
		periodStartDate = tperiodStartDate;
	}

	public FinanceDate getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(FinanceDate tperiodEndDate) {
		periodEndDate = tperiodEndDate;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void updateBalance(double amountToPay) {
		this.balance += amountToPay;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Payee getInvolvedPayee() {
		return taxAgency;
	}

	/**
	 * @return the returningDate
	 */
	public FinanceDate getReturningDate() {
		return returningDate;
	}

	/**
	 * @param returningDate
	 *            the returningDate to set
	 */
	public void setReturningDate(FinanceDate returningDate) {
		this.returningDate = returningDate;
	}

	/**
	 * @return the totalTAXAmount
	 */
	public double getTotalTAXAmount() {
		return totalTAXAmount;
	}

	/**
	 * @param totalTAXAmount
	 *            the totalTAXAmount to set
	 */
	public void setTotalTAXAmount(double totalTAXAmount) {
		this.totalTAXAmount = totalTAXAmount;
	}

	@Override
	public String toString() {
		return taxAgency.getName() + ":" + periodStartDate + "-"
				+ periodEndDate + "Balance:" + balance;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		calculateTAXAmounts();
		this.total = this.totalTAXAmount;
		this.balance = this.total;
		this.type = Transaction.TYPE_TAX_RETURN;

		taxAgency.setLastTAXReturnDate(this.periodEndDate);
		taxAgency.onUpdate(session);
		session.saveOrUpdate(taxAgency);

		Query query = session.getNamedQuery("getTaxAdjustment.by.dates")
				.setParameter("fromDate", this.periodStartDate)
				.setParameter("toDate", this.periodEndDate)
				.setEntity("company", getCompany());

		List<TAXAdjustment> vadj = query.list();
		if (vadj != null) {
			for (TAXAdjustment va : vadj) {
				va.setIsFiled(true);
				session.update(va);
			}
		}

		query = session.getNamedQuery("getTaxrateCalc.by.taxitem.and.details")
				.setParameter("toDate", this.periodEndDate)
				.setParameter("vatAgency", taxAgency.getID())
				.setEntity("company", getCompany());

		List<TAXRateCalculation> vrc = query.list();
		for (TAXRateCalculation v : vrc) {
			v.taxReturn = this;
			session.update(v);
		}

		ChangeTracker.put(this);
		return super.onSave(session);

	}

	protected void calculateTAXAmounts() {
		for (TAXReturnEntry entry : taxReturnEntries) {
			if (entry.getTransaction() != null
					&& (entry.getTransaction() instanceof TAXAdjustment)) {
				TAXAdjustment taxAdjustment = (TAXAdjustment) entry
						.getTransaction();
				if ((taxAdjustment.getIncreaseVATLine() && taxAdjustment
						.isSales())
						|| (!taxAdjustment.getIncreaseVATLine() && !taxAdjustment
								.isSales())) {
					entry.setCategory(Transaction.CATEGORY_CUSTOMER);
				} else {
					entry.setCategory(Transaction.CATEGORY_VENDOR);
				}
			}
			if (entry.getCategory() == Transaction.CATEGORY_CUSTOMER) {
				salesTaxTotal += entry.getTaxAmount();
			} else if (entry.getCategory() == Transaction.CATEGORY_VENDOR) {
				purchaseTaxTotal += entry.getTaxAmount();
			}
		}
		totalTAXAmount = salesTaxTotal + purchaseTaxTotal;
		balance = totalTAXAmount;
	}

	/**
	 * @return the salesTaxTotal
	 */
	public double getSalesTaxTotal() {
		return salesTaxTotal;
	}

	/**
	 * @param salesTaxTotal
	 *            the salesTaxTotal to set
	 */
	public void setSalesTaxTotal(double salesTaxTotal) {
		this.salesTaxTotal = salesTaxTotal;
	}

	/**
	 * @return the purchaseTaxTotal
	 */
	public double getPurchaseTaxTotal() {
		return purchaseTaxTotal;
	}

	/**
	 * @param purchaseTaxTotal
	 *            the purchaseTaxTotal to set
	 */
	public void setPurchaseTaxTotal(double purchaseTaxTotal) {
		this.purchaseTaxTotal = purchaseTaxTotal;
	}

	/**
	 * @return the boxes
	 */
	public List<Box> getBoxes() {
		return boxes;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}

	/**
	 * @return the taxReturnEntries
	 */
	public List<TAXReturnEntry> getTaxReturnEntries() {
		return taxReturnEntries;
	}

	/**
	 * @param taxReturnEntries
	 *            the taxReturnEntries to set
	 */
	public void setTaxReturnEntries(List<TAXReturnEntry> taxReturnEntries) {
		this.taxReturnEntries = taxReturnEntries;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		doReverseEffect();
		return super.onDelete(session);
	}

	private void doReverseEffect() {
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("list.TAXReturn.orderByEndDate")
				.setEntity("company", getCompany());
		List<TAXReturn> list = query.list();
		if (list == null || list.size() < 2) {
			taxAgency.setLastTAXReturnDate(null);
		} else {
			taxAgency.setLastTAXReturnDate(list.get(list.size() - 2)
					.getPeriodEndDate());
		}

		query = session.getNamedQuery("getTaxAdjustment.by.dates")
				.setParameter("fromDate", this.periodStartDate)
				.setParameter("toDate", this.periodEndDate)
				.setEntity("company", getCompany());

		List<TAXAdjustment> vadj = query.list();
		if (vadj != null) {
			for (TAXAdjustment adjustment : vadj) {
				adjustment.setIsFiled(false);
				session.update(adjustment);
			}
		}

		query = session.getNamedQuery("getTaxrateCalc.of.TAXReturn")
				.setParameter("taxReturnId", this.getID());

		List<TAXRateCalculation> taxRateCalculations = query.list();
		for (TAXRateCalculation taxRateCalculation : taxRateCalculations) {
			taxRateCalculation.taxReturn = null;
			session.update(taxRateCalculation);
		}
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		double salesTaxTotal = 0, purchaseTaxTotal = 0, totalTAXAmount;
		for (TAXReturnEntry entry : getTaxReturnEntries()) {
			if (entry.getCategory() == Transaction.CATEGORY_CUSTOMER) {
				salesTaxTotal += entry.getTaxAmount();
			} else if (entry.getCategory() == Transaction.CATEGORY_VENDOR) {
				purchaseTaxTotal += entry.getTaxAmount();
			}
		}
		totalTAXAmount = salesTaxTotal + purchaseTaxTotal;
		e.add(getTaxAgency().getSalesLiabilityAccount(), -1 * salesTaxTotal);
		e.add(getTaxAgency().getPurchaseLiabilityAccount(), -1
				* purchaseTaxTotal);
		e.add(getTaxAgency().getFiledLiabilityAccount(), totalTAXAmount);
		e.add(getTaxAgency(), getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (taxAgency == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().taxAgency());
		}

		if (taxReturnEntries == null || taxReturnEntries.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_ITEM_NULL, Global
							.get().messages().transactionItem());
		}
	}
}
