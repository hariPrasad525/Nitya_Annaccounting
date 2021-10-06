package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaybillTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CreatePayBillCommand extends AbstractTransactionCommand {
	ClientPayBill paybill;

	@Override
	protected String getWelcomeMessage() {
		return paybill.getID() == 0 ? getMessages().create(
				getMessages().payBill()) : getMessages().updating(
				getMessages().payBill());
	}

	@Override
	protected String getDetailsMessage() {
		return paybill.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payBill()) : getMessages().readyToUpdate(
				getMessages().payBill());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_PAY_BILL, context.getCompany()));

		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(FILTER_BY_DUE_ON_BEFORE).setDefaultValue(new ClientFinanceDate());

	}

	@Override
	public String getSuccessMessage() {
		return paybill.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payBill()) : getMessages().updateSuccessfully(
				getMessages().payBill());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().Vendor(), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreatePayBillCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
							CreatePayBillCommand.this.vendorSelected(value);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Vendor vendor = (Vendor) CreatePayBillCommand.this.get(VENDOR)
						.getValue();
				return vendor.getCurrency();
			}

		});
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount", getMessages()
						.create(getMessages().otherCurrentAsset()),
						getMessages().otherCurrentAsset()));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_BANK || e
											.getType() == Account.TYPE_OTHER_ASSET)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().bankAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getMessages().cash(), getMessages().creditCard(),
						getMessages().check(), getMessages().directDebit(),
						getMessages().masterCard(),
						getMessages().onlineBanking(),
						getMessages().standingOrder(),
						getMessages().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(FILTER_BY_DUE_ON_BEFORE, getMessages()
				.pleaseEnter(getMessages().filterByBilldueonorbefore()),
				getMessages().filterByBilldueonorbefore(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new PaybillTableRequirement(BILLS_DUE, getMessages()
				.pleaseSelect(getMessages().billsToPay()), getMessages()
				.billsToPay()) {

			@Override
			protected List<ClientTransactionPayBill> getList() {
				return getTransactionBills();
			}

			@Override
			protected Payee getPayee() {
				return (Vendor) CreatePayBillCommand.this.get(VENDOR)
						.getValue();
			}

			@Override
			public long getTransactionId() {
				return paybill.getID();
			}

			@Override
			protected List<ClientCreditsAndPayments> getCreditsPayments() {
				return vendorCreditsAndPayments;
			}

		});

	}

	private List<ClientTransactionPayBill> getTransactionBills() {
		return payBills;
	}

	List<ClientTransactionPayBill> payBills = new ArrayList<ClientTransactionPayBill>();
	List<ClientCreditsAndPayments> vendorCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();

	private void vendorSelected(Vendor value) {
		payBills = getPayBills();
		vendorCreditsAndPayments = getVendorCreditsAndPayments();
		((PaybillTableRequirement) get(BILLS_DUE)).vendorSelected();
	}

	private List<ClientTransactionPayBill> getPayBills() {
		ArrayList<ClientTransactionPayBill> payBills = new ArrayList<ClientTransactionPayBill>();
		try {
			ArrayList<PayBillTransactionList> transactionPayBills = new FinanceTool()
					.getVendorManager()
					.getTransactionPayBills(
							((Vendor) CreatePayBillCommand.this.get(VENDOR)
									.getValue()).getID(),
							getCompanyId(),
							new FinanceDate(
									(ClientFinanceDate) CreatePayBillCommand.this
											.get(DATE).getValue()));
			return getBills(transactionPayBills);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return payBills;
	}

	private List<ClientTransactionPayBill> getBills(
			List<PayBillTransactionList> list) {
		List<PayBillTransactionList> filterList = list;
		ClientFinanceDate dueDateOnOrBefore = get(FILTER_BY_DUE_ON_BEFORE)
				.getValue();
		Vendor vendor = get(VENDOR).getValue();
		List<PayBillTransactionList> tempList = new ArrayList<PayBillTransactionList>();
		if (dueDateOnOrBefore != null) {
			for (PayBillTransactionList cont : filterList) {
				if (cont.getDueDate().before(dueDateOnOrBefore)
						|| cont.getDueDate().equals(dueDateOnOrBefore))
					tempList.add(cont);

			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		if (vendor != null) {

			for (PayBillTransactionList cont : filterList) {
				if (vendor.getName().toString()
						.equalsIgnoreCase(cont.getVendorName().toString())) {
					tempList.add(cont);
				}
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();

		for (PayBillTransactionList cont : filterList) {
			ClientTransactionPayBill record = getTransactionPayBillByTransaction(
					cont.getType(), cont.getTransactionId());
			double amountDue = 0.00D;
			if (record == null) {
				record = new ClientTransactionPayBill();
				if (cont.getType() == ClientTransaction.TYPE_ENTER_BILL) {
					record.setEnterBill(cont.getTransactionId());
				} else if (cont.getType() == ClientTransaction.TYPE_TRANSFER_FUND) {
					record.setTransactionMakeDeposit(cont.getTransactionId());
				} else if (cont.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
					record.setJournalEntry(cont.getTransactionId());
				}
			} else {
				paybill.getTransactionPayBill().remove(record);
				amountDue = record.getPayment() + record.getAppliedCredits()
						+ record.getCashDiscount();
			}
			amountDue += cont.getAmountDue();
			record.setAmountDue(amountDue);
			record.setDummyDue(amountDue);

			record.setBillNumber(cont.getBillNumber());
			record.setCashDiscount(record.getCashDiscount()
					+ cont.getCashDiscount());

			record.setAppliedCredits(cont.getCredits(), false);

			record.setDiscountDate(cont.getDiscountDate() != null ? cont
					.getDiscountDate().getDate() : 0);

			record.setDueDate(cont.getDueDate() != null ? cont.getDueDate()
					.getDate() : 0);

			record.setOriginalAmount(cont.getOriginalAmount());
			record.setPayment(cont.getPayment());
			record.setDiscountAccount(getCompany().getCashDiscountAccount() != null ? getCompany()
					.getCashDiscountAccount().getID() : 0);

			record.setTdsAmount(0.00D);
			record.setPayment(0.00D);

			records.add(record);
		}
		for (ClientTransactionPayBill bill : paybill.getTransactionPayBill()) {
			bill.setAmountDue(bill.getPayment() + bill.getAppliedCredits());
			bill.setPayment(0.00D);
			bill.setAppliedCredits(0.00D, false);
			bill.setCashDiscount(0);
			bill.setDiscountAccount(0);

			records.add(bill);
		}

		return records;

	}

	private List<ClientCreditsAndPayments> getVendorCreditsAndPayments() {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {
			Vendor vendor = get(VENDOR).getValue();
			serverCreditsAndPayments = new FinanceTool().getVendorManager()
					.getCreditsAndPayments(vendor.getID(), paybill.getID(),
							getCompanyId());
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPaymentsList.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
			return clientCreditsAndPaymentsList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(
				clientCreditsAndPaymentsList);

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isKeepTrackofBills()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().payBill()));
				return "billsAndExpensesList";
			}
			paybill = getTransaction(string, AccounterCoreType.PAYBILL, context);

			if (paybill == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().payBill()));
				return "billsAndExpensesList " + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			paybill = new ClientPayBill();
		}
		setTransaction(paybill);
		return null;
	}

	private void setValues(Context context) {
		Vendor vendor = (Vendor) CommandUtils.getServerObjectById(
				paybill.getVendor(), AccounterCoreType.VENDOR);
		get(VENDOR).setValue(vendor);
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(paybill.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						paybill.getPaymentMethodForCommands(), getMessages()));
		get(FILTER_BY_DUE_ON_BEFORE).setValue(
				new ClientFinanceDate(paybill.getBillDueOnOrBefore()));
		get(NUMBER).setValue(paybill.getNumber());
		get(DATE).setValue(paybill.getDate());
		get(MEMO).setValue(paybill.getMemo());
		List<ClientTransactionPayBill> transactionPayBill = paybill
				.getTransactionPayBill();
		get(BILLS_DUE).setValue(transactionPayBill);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setPayBillType(ClientPayBill.TYPE_PAYBILL);
		Vendor vendor = get(VENDOR).getValue();
		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientFinanceDate dueDate = get(FILTER_BY_DUE_ON_BEFORE).getValue();
		String number = get(NUMBER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		paybill.setVendor(vendor.getID());
		paybill.setPayFrom(payFrom.getID());
		paybill.setPaymentMethod(paymentMethod);

		paybill.setBillDueOnOrBefore(dueDate);
		paybill.setNumber(number);
		paybill.setDate(date.getDate());
		String memo = get(MEMO).getValue();
		paybill.setMemo(memo);
		paybill.setCurrency(vendor.getCurrency().getID());
		paybill.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());

		if (context.getPreferences().isTDSEnabled()) {

			TAXItem taxItem = vendor.getTAXItem();
			if (taxItem != null) {
				paybill.setTdsTaxItem(taxItem.getID());
			}
		}
		paybill.setCurrency(vendor.getCurrency().getID());
		paybill.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		updateCreditsAndTotals();

		create(paybill, context);

		return null;
	}

	private void updateCreditsAndTotals() {
		List<ClientTransactionPayBill> transactionPayBills = getTransactionPayBills();
		paybill.setTransactionPayBill(transactionPayBills);
		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : vendorCreditsAndPayments) {
			totalCredits += credit.getBalance();
		}
		paybill.setUnUsedCredits(totalCredits);

		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : transactionPayBills) {
			toBeSetAmount += rec.getPayment();
		}
		paybill.setTotal(toBeSetAmount);
		Account payFromAccount = get(PAY_FROM).getValue();
		if (get(VENDOR).getValue() != null && payFromAccount != null) {
			double toBeSetEndingBalance = 0.0;
			if (payFromAccount.isIncrease())
				toBeSetEndingBalance = payFromAccount.getTotalBalance()
						+ paybill.getTotal();
			else
				toBeSetEndingBalance = payFromAccount.getTotalBalance()
						- paybill.getTotal();
			paybill.setEndingBalance(toBeSetEndingBalance);
		}
	}

	private List<ClientTransactionPayBill> getTransactionPayBills() {
		List<ClientTransactionPayBill> selectedRecords = get(BILLS_DUE)
				.getValue();
		List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill tpbRecord : selectedRecords) {
			tpbRecord.setID(0);
			transactionPayBill.add(tpbRecord);
		}

		return transactionPayBill;
	}

	private ClientTransactionPayBill getTransactionPayBillByTransaction(
			int transactionType, long transactionId) {
		for (ClientTransactionPayBill bill : paybill.getTransactionPayBill()) {
			if ((transactionType == ClientTransaction.TYPE_ENTER_BILL && bill
					.getEnterBill() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_TRANSFER_FUND && bill
							.getTransactionMakeDeposit() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_JOURNAL_ENTRY && bill
							.getJournalEntry() == transactionId)) {
				return bill;
			}
		}
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		updateCreditsAndTotals();
		Currency currency = getCurrency();
		String formalName = getPreferences().getPrimaryCurrency()
				.getFormalName();
		if (!currency.getFormalName().equalsIgnoreCase(formalName))
			makeResult.add(getMessages().total() + "(" + formalName + ")"
					+ ": " + (paybill.getTotal() * getCurrencyFactor()));
		makeResult.add(getMessages().total() + "(" + currency.getFormalName()
				+ ")" + paybill.getTotal());
		makeResult.add(getMessages().unusedCredits()
				+ paybill.getUnUsedCredits());
	}

	@Override
	protected Currency getCurrency() {
		return ((Vendor) CreatePayBillCommand.this.get(VENDOR).getValue())
				.getCurrency();
	}

}