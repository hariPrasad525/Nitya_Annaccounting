package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

public class MakeDeposit extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Account depositTo;

	List<TransactionDepositItem> transactionDepositItems = new ArrayList<TransactionDepositItem>();

	private Set<Estimate> estimates = new HashSet<Estimate>();

	public MakeDeposit() {
		super();
		setType(Transaction.TYPE_MAKE_DEPOSIT);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		if (this.depositTo != null) {
			w.put(messages.depositTo(), this.depositTo.name);
		}
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_BANKING;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_MAKE_DEPOSIT;
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {

		MakeDeposit clone = (MakeDeposit) super.clone();

		List<TransactionDepositItem> items = new ArrayList<TransactionDepositItem>();
		if (transactionDepositItems != null) {
			for (TransactionDepositItem transactionDepositItem : transactionDepositItems) {
				items.add(transactionDepositItem.clone());
			}
		}
		clone.setTransactionDepositItems(items);
		clone.estimates = new HashSet<Estimate>();

		return clone;

	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (isDraftOrTemplate()) {
			return false;
		}

		if (getCompany().getPreferences()
				.isProductandSerivesTrackingByCustomerEnabled()
				&& getCompany().getPreferences()
						.isBillableExpsesEnbldForProductandServices()) {
			createAndSaveEstimates(this.transactionDepositItems, session);
		}

		return false;
	}

	private void createAndSaveEstimates(
			List<TransactionDepositItem> transactionDepositItems,
			Session session) {
		this.getEstimates().clear();

		Set<Estimate> estimates = new HashSet<Estimate>();
		for (TransactionDepositItem transactionItem : transactionDepositItems) {
			if (transactionItem.isBillable()
					&& transactionItem.getCustomer() != null) {
				TransactionItem newTransactionItem = new TransactionItem();
				newTransactionItem.setId(0);
				newTransactionItem.setOnSaveProccessed(false);
				newTransactionItem.setLineTotal(transactionItem.getTotal()
						* getCurrencyFactor());
				newTransactionItem.setDiscount(0.00D);
				newTransactionItem.setVATfraction(0.00D);
				newTransactionItem.setUnitPrice(transactionItem.getTotal()
						* getCurrencyFactor());
				newTransactionItem.setBillable(transactionItem.isBillable());
				newTransactionItem.setAccounterClass(transactionItem
						.getAccounterClass());
				newTransactionItem.setTransaction(transactionItem
						.getTransaction());
				newTransactionItem.setDescription(transactionItem
						.getDescription());
				newTransactionItem.setAccount(transactionItem.getAccount());
				newTransactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				newTransactionItem.setCustomer(transactionItem.getCustomer());
				newTransactionItem.setJob(transactionItem.getJob());
				Estimate estimate = getCustomerEstimate(estimates,
						newTransactionItem.getCustomer().getID());
				if (estimate == null) {
					estimate = new Estimate();
					estimate.setCompany(getCompany());
					estimate.setCustomer(newTransactionItem.getCustomer());
					estimate.setJob(newTransactionItem.getJob());
					estimate.setTransactionItems(new ArrayList<TransactionItem>());
					estimate.setEstimateType(Estimate.DEPOSIT_EXPENSES);
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
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return super.onUpdate(session);
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

		if (!UserUtils.canDoThis(MakeDeposit.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		if (!this.getReconciliationItems().isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_EDITING_TRANSACTION_RECONCILIED);
		}

		for (Estimate estimate : this.getEstimates()) {
			if (estimate.getUsedInvoice() != null) {
				throw new AccounterException(AccounterException.USED_IN_INVOICE);
			}
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	public void validateTransactionDepositItems() throws AccounterException {
		for (TransactionDepositItem transactionItem : transactionDepositItems) {
			if (transactionItem.getAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_SELECT, Global.get()
								.messages().depositFrom());
			}

			if (transactionItem.isBillable()) {
				if (transactionItem.getCustomer() == null) {
					throw new AccounterException(
							AccounterException.ERROR_MUST_SELECT_CUSTOMER_FOR_BILLABLE);

				}
			}
		}
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		MakeDeposit deposit = (MakeDeposit) clonedObject;

		if (isDraftOrTemplate()) {
			super.onEdit(deposit);
			return;
		}
		if (this.isVoid() && !deposit.isVoid()) {

		} else {
			for (Estimate estimate : this.estimates) {
				session.delete(estimate);
			}
			this.estimates.clear();
			this.createAndSaveEstimates(this.transactionDepositItems, session);
		}
		super.onEdit(deposit);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {

		}
		for (Estimate estimate : this.getEstimates()) {
			session.delete(estimate);
		}
		this.estimates.clear();
		return super.onDelete(session);
	}

	public List<TransactionDepositItem> getTransactionDepositItems() {
		return transactionDepositItems;
	}

	public void setTransactionDepositItems(
			List<TransactionDepositItem> transactionDepositItems) {
		this.transactionDepositItems = transactionDepositItems;
	}

	public Set<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(Set<Estimate> estimates) {
		this.estimates = estimates;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		// TODO Auto-generated method stub
		super.onLoad(session, arg1);
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		e.add(depositTo, -getTotal());
		for (TransactionDepositItem dItem : getTransactionDepositItems()) {
			e.add(dItem.getAccount(), dItem.getTotal());
		}
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(depositTo, Global.get().messages().depositTo());

		if (transactionDepositItems.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_MAKE_DEPOSIT_NULL);
		}

		if (!transactionDepositItems.isEmpty()) {
			for (TransactionDepositItem item : transactionDepositItems) {
				item.selfValidate();
				Account fromAcc = item.getAccount();
				if (fromAcc.getID() == depositTo.getID()) {
					throw new AccounterException(
							AccounterException.ERROR_DEPOSIT_AND_TRANSFER_SHOULD_DIFF);
				}

				if (fromAcc == null) {
					throw new AccounterException(
							AccounterException.ERROR_PLEASE_ENTER, Global.get()
									.messages().depositFrom());

				}

				Payee payee = item.getReceivedFrom();
				if (depositTo.getCurrency().getID() != fromAcc.getCurrency()
						.getID()
						|| (payee != null && depositTo.getCurrency().getID() != payee
								.getCurrency().getID())) {
					throw new AccounterException(
							AccounterException.ERROR_CURRENCY_MUST_BE_SAME);
				}

				if (item.getTotal() != null) {
					if (item.getTotal() <= 0) {
						throw new AccounterException(
								AccounterException.ERROR_AMOUNT_ZERO, Global
										.get().messages().amount());
					}
				} else {
					throw new AccounterException(
							AccounterException.ERROR_AMOUNT_ZERO, Global.get()
									.messages().amount());
				}
			}

		}
		validateTransactionDepositItems();

		checkNetAmountNegative();

	}

	public Account getDepositTo() {
		return depositTo;
	}

	public void setDepositTo(Account depositTo) {
		this.depositTo = depositTo;
	}

}
