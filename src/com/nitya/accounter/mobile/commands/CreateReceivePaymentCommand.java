package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.UserCommand;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.ReceivePaymentTableRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReceivePayment;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionReceivePayment;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class CreateReceivePaymentCommand extends AbstractTransactionCommand {
	private static final String AMOUNT_RECEIVED = "amountreceived";
	private static final String CHECK_NUMBER = "checknum";
	private static final String TRANSACTIONS = "transactions";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	ClientReceivePayment payment;
	private List<ClientTransactionReceivePayment> transactionReceivePayments = new ArrayList<ClientTransactionReceivePayment>();

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterName(Global.get().Customer()), Global.get()
				.Customer(), false, true, new ChangeListner<Customer>() {

			@Override
			public void onSelection(Customer value) {
				try {
					double mostRecentTransactionCurrencyFactor = CommandUtils
							.getMostRecentTransactionCurrencyFactor(
									getCompanyId(),
									value.getCurrency().getID(),
									new ClientFinanceDate().getDate());
					CreateReceivePaymentCommand.this.get(CURRENCY_FACTOR)
							.setValue(mostRecentTransactionCurrencyFactor);
					CreateReceivePaymentCommand.this.customerSelected(value);
				} catch (AccounterException e) {
					e.printStackTrace();
				}
			}
		}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterNameOrNumber(getMessages().Account()),
				getMessages().depositAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().depositAccount());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount", getMessages()
						.create(getMessages().otherCurrentAsset()),
						getMessages().otherCurrentAsset()));
				list.add(new UserCommand("createBankAccount", getMessages()
						.create(getMessages().creditCard()), getMessages()
						.creditCard()));
				list.add(new UserCommand("createBankAccount", getMessages()
						.create(getMessages().fixedAsset()), getMessages()
						.fixedAsset()));
				list.add(new UserCommand("createBankAccount", getMessages()
						.create(getMessages().paypal()), getMessages().paypal()));
			}

			@Override
			protected List<Account> getLists(final Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account acc) {
							return acc.getIsActive()
									&& Arrays
											.asList(ClientAccount.TYPE_BANK,
													ClientAccount.TYPE_CASH,
													ClientAccount.TYPE_PAYPAL,
													ClientAccount.TYPE_CREDIT_CARD,
													ClientAccount.TYPE_OTHER_CURRENT_ASSET,
													ClientAccount.TYPE_INVENTORY_ASSET,
													ClientAccount.TYPE_FIXED_ASSET)
											.contains(acc.getType())
									&& acc.getType() != Account.TYPE_ACCOUNT_RECEIVABLE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
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
				return null;
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT_RECEIVED, getMessages()
				.pleaseEnter(getMessages().amountReceived()), getMessages()
				.amountReceived(), true, true) {

			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateReceivePaymentCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});

		list.add(new ReceivePaymentTableRequirement(TRANSACTIONS, getMessages()
				.pleaseSelect(getMessages().dueForPayment()), getMessages()
				.dueForPayment()) {

			@Override
			protected List<ClientTransactionReceivePayment> getList() {
				return getRequirementList();
			}

			@Override
			protected Payee getPayee() {
				return (Customer) CreateReceivePaymentCommand.this
						.get(CUSTOMER).getValue();
			}

			@Override
			protected long getTransactionId() {
				return payment.getID();
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new NumberRequirement(CHECK_NUMBER, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true));

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), CURRENCY_FACTOR) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateReceivePaymentCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});
	}

	protected void customerSelected(Customer value) {
		((ReceivePaymentTableRequirement) get(TRANSACTIONS)).customerSelected();
		Customer customer = get(CUSTOMER).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		try {
			ArrayList<ReceivePaymentTransactionList> transactionReceivePayments2 = getTransactionReceivePayments(
					customer.getCompany().getID(), customer.getID(),
					date.getDate());
			transactionReceivePayments = getTransactionRecievePayments(transactionReceivePayments2);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	private List<ClientTransactionReceivePayment> getRequirementList() {
		return transactionReceivePayments;
	}

	private ClientTransactionReceivePayment getTransactionPayBillByTransaction(
			int transactionType, long transactionId) {
		for (ClientTransactionReceivePayment bill : payment
				.getTransactionReceivePayment()) {
			if ((transactionType == ClientTransaction.TYPE_INVOICE && bill
					.getInvoice() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS && bill
							.getCustomerRefund() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_JOURNAL_ENTRY && bill
							.getJournalEntry() == transactionId)) {
				return bill;
			}
		}
		return null;
	}

	private List<ClientTransactionReceivePayment> getTransactionRecievePayments(
			List<ReceivePaymentTransactionList> result) {
		ArrayList<ClientTransactionReceivePayment> transactionReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
		if (result == null)
			return transactionReceivePayments;
		List<ClientTransactionReceivePayment> records = new ArrayList<ClientTransactionReceivePayment>();

		for (ReceivePaymentTransactionList rpt : result) {

			ClientTransactionReceivePayment record = getTransactionPayBillByTransaction(
					rpt.getType(), rpt.getTransactionId());
			double amountDue = 0.00D;
			if (record == null) {
				record = new ClientTransactionReceivePayment();
				if (rpt.getType() == ClientTransaction.TYPE_INVOICE) {
					record.isInvoice = true;
					record.setInvoice(rpt.getTransactionId());
				} else if (rpt.getType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS) {
					record.isInvoice = false;
					record.setCustomerRefund(rpt.getTransactionId());
				} else if (rpt.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
					record.isInvoice = false;
					record.setJournalEntry(rpt.getTransactionId());
				}
			} else {
				payment.getTransactionReceivePayment().remove(record);
				amountDue = record.getPayment() + record.getAppliedCredits()
						+ record.getCashDiscount() + record.getWriteOff();
			}
			amountDue += rpt.getAmountDue();
			record.setAmountDue(amountDue);
			record.setDummyDue(amountDue);

			record.setDueDate(rpt.getDueDate() != null ? rpt.getDueDate()
					.getDate() : 0);
			record.setNumber(rpt.getNumber());

			record.setInvoiceAmount(rpt.getInvoiceAmount());

			record.setInvoice(rpt.getTransactionId());

			record.setDiscountDate(rpt.getDiscountDate() != null ? rpt
					.getDiscountDate().getDate() : 0);

			record.setDiscountAccount(0);
			record.setCashDiscount(0);

			record.setWriteOff(0);

			record.setAppliedCredits(rpt.getAppliedCredits(), false);

			record.setPayment(0);
			record.setWriteOffAccount(0);

			records.add(record);

		}

		for (ClientTransactionReceivePayment receivePayment : payment
				.getTransactionReceivePayment()) {
			receivePayment.setAmountDue(receivePayment.getPayment()
					+ receivePayment.getAppliedCredits());
			receivePayment.setPayment(0.00D);
			receivePayment.setCashDiscount(0.0D);
			receivePayment.setWriteOff(0.0D);
			receivePayment.setDiscountAccount(0);
			receivePayment.setWriteOffAccount(0);
			receivePayment.setAppliedCredits(0.00D, false);
			records.add(receivePayment);
		}

		return records;
	}

	private void recalculateGridAmounts() {
		double total = (Double) get(AMOUNT_RECEIVED).getValue();
		payment.setTotal(total);
		payment.setAmount(total);
		payment.setUnUsedPayments(payment.getAmount() - getGridTotal());
		payment.setUnUsedCredits(getUnusedCredits());
	}

	public double getUnusedCredits() {
		double unusedCredits = 0.0;
		ReceivePaymentTableRequirement requirement = (ReceivePaymentTableRequirement) get(TRANSACTIONS);
		for (ClientCreditsAndPayments ccap : requirement
				.getCreditsAndPayments()) {
			unusedCredits += ccap.getBalance();
		}
		return unusedCredits;
	}

	public Double getGridTotal() {
		Double total = 0.0D;
		List<ClientTransactionReceivePayment> records = get(TRANSACTIONS)
				.getValue();
		for (ClientTransactionReceivePayment record : records) {
			total += record.getPayment();
		}
		return total;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Customer customer = get(CUSTOMER).getValue();
		payment.setCustomer(customer.getID());
		payment.setType(ClientTransaction.TYPE_RECEIVE_PAYMENT);
		payment.setCustomerBalance(customer.getBalance());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		payment.setPaymentMethod(paymentMethod);

		ClientFinanceDate date = get(DATE).getValue();
		payment.setDate(date.getDate());

		String receivePaymentNum = get(NUMBER).getValue();
		payment.setNumber(receivePaymentNum);

		Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		payment.setDepositIn(account.getID());

		String memo = get(MEMO).getValue();
		payment.setMemo(memo);

		String checkNumber = get(CHECK_NUMBER).getValue();
		payment.setCheckNumber(checkNumber);
		payment.setCurrency(customer.getCurrency().getID());
		payment.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		recalculateGridAmounts();
		payment.setTransactionReceivePayment(getTransactionRecievePayments(payment));
		if (!isValidRecievePaymentAmount(payment.getAmount(), getGridTotal())) {
			return new Result(getMessages().recievePaymentTotalAmount());
		}
		create(payment, context);

		return null;
	}

	public static boolean isValidRecievePaymentAmount(Double amount,
			Double paymentsTotal) {
		if (DecimalUtil.isGreaterThan(paymentsTotal, amount)) {
			return false;
		}
		return true;
	}

	private List<ClientTransactionReceivePayment> getTransactionRecievePayments(
			ClientReceivePayment receivePayment) {

		List<ClientTransactionReceivePayment> paymentsList = new ArrayList<ClientTransactionReceivePayment>();
		ReceivePaymentTableRequirement requirement = (ReceivePaymentTableRequirement) get(TRANSACTIONS);
		ArrayList<ClientTransactionReceivePayment> clientTransactionReceivePayments = requirement
				.getValue();
		for (ClientTransactionReceivePayment payment : clientTransactionReceivePayments) {
			payment.setID(0);
			payment.setTransaction(receivePayment.getID());

			paymentsList.add(payment);
		}

		return paymentsList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().receivePayment()));
				return "receivedPaymentsList";
			}

			payment = getTransaction(string, AccounterCoreType.RECEIVEPAYMENT,
					context);
			if (payment == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().receivePayment()));
				return "receivedPaymentsList " + string;
			}
			setValues();
		} else {
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			payment = new ClientReceivePayment();
		}
		setTransaction(payment);
		return null;
	}

	private void setValues() {
		Customer customer = (Customer) CommandUtils.getServerObjectById(
				payment.getCustomer(), AccounterCoreType.CUSTOMER);
		get(CUSTOMER).setValue(customer);
		// get(AMOUNT_RECEIVED).setValue(payment.getAmount());
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						payment.getPaymentMethodForCommands(), getMessages()));
		get(DATE).setValue(payment.getDate());
		get(NUMBER).setValue(payment.getNumber());
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(payment.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(TRANSACTIONS).setValue(payment.getTransactionReceivePayment());
		customerSelected(customer);
		/* get(CURRENCY_FACTOR).setValue(payment.getCurrencyFactor()); */
		get(MEMO).setValue(payment.getMemo());
		get(CHECK_NUMBER).setValue(payment.getCheckNumber());
	}

	@Override
	protected String getWelcomeMessage() {
		return payment.getID() == 0 ? getMessages().creating(
				getMessages().receivePayment()) : getMessages().readyToUpdate(
				getMessages().receivePayment());
	}

	@Override
	protected String getDetailsMessage() {
		return payment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().receivePayment()) : getMessages().readyToUpdate(
				getMessages().receivePayment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_RECEIVE_PAYMENT, getCompany()));
		get(MEMO).setDefaultValue("");
		get(AMOUNT_RECEIVED).setDefaultValue(new Double(0));
		get(CHECK_NUMBER).setDefaultValue("1");

	}

	@Override
	public String getSuccessMessage() {
		return payment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().receivePayment()) : getMessages()
				.updateSuccessfully(getMessages().receivePayment());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		recalculateGridAmounts();
		makeResult.add(getMessages().unusedCredits()
				+ payment.getUnUsedCredits());
		makeResult.add(getMessages().unusedPayments("")
				+ payment.getUnUsedPayments());
	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateReceivePaymentCommand.this.get(CUSTOMER)
				.getValue()).getCurrency();
	}
}
