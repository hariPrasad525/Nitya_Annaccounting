package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class CreateCustomerRefundCommand extends AbstractTransactionCommand {
	ClientCustomerRefund customerRefund;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerRefund(
										Global.get().Customer())));
				return "customerRefundList";
			}

			customerRefund = getTransaction(string,
					AccounterCoreType.CUSTOMERREFUND, context);

			if (customerRefund == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerRefund(
										Global.get().Customer())));
				return "customerRefundList " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			customerRefund = new ClientCustomerRefund();
		}
		setTransaction(customerRefund);
		return null;
	}

	private void setValues() {
		get(NUMBER).setValue(customerRefund.getNumber());
		get(DATE).setValue(customerRefund.getDate());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(customerRefund.getPayTo(),
						AccounterCoreType.CUSTOMER));
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(customerRefund.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						customerRefund.getPaymentMethodForCommands(),
						getMessages()));
		get(AMOUNT).setValue(customerRefund.getTotal());
		get(TO_BE_PRINTED).setValue(customerRefund.getIsToBePrinted());
		get(CHEQUE_NO).setValue(customerRefund.getCheckNumber());
		get(MEMO).setValue(customerRefund.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return customerRefund.getID() == 0 ? getMessages().create(
				getMessages().customerRefund(Global.get().Customer()))
				: "Update Customer Refund command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return customerRefund.getID() == 0 ? getMessages().readyToCreate(
				getMessages().customerRefund(Global.get().Customer()))
				: getMessages().readyToUpdate(
						getMessages().customerRefund(Global.get().Customer()));
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_REFUNDS,
						context.getCompany()));
		get(CHEQUE_NO).setDefaultValue(getMessages().toBePrinted());
	}

	@Override
	public String getSuccessMessage() {
		return customerRefund.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().customerRefund(Global.get().Customer()))
				: getMessages().updateSuccessfully(
						getMessages().customerRefund(Global.get().Customer()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				getMessages().payTo()), getMessages().payTo(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateCustomerRefundCommand.this.get(
									CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
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
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateCustomerRefundCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});
		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(
				getMessages().transferTo()), getMessages().transferTo(), false,
				false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("newBankAccount", getMessages().bank()));
				list.add(new UserCommand("newBankAccount",
						"Create Other CurrentAsset Account", getMessages()
								.otherCurrentAsset()));
				list.add(new UserCommand("newBankAccount",
						"Create Cash Account", getMessages().cash()));
				list.add(new UserCommand("newBankAccount",
						"Create Inventory Account", getMessages()
								.inventoryAsset()));
				list.add(new UserCommand("newBankAccount",
						"Create Paypal Account", getMessages().paypal()));
				list.add(new UserCommand("newBankAccount",
						"Create Creditcard Account", getMessages().creditCard()));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET || e
											.getType() == ClientAccount.TYPE_CREDIT_CARD)) {
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
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

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
		list.add(new CurrencyAmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				false, true) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateCustomerRefundCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getMessages().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return "Not Printed ";
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getMessages().chequeNo()), getMessages().chequeNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String paymentMethod = get(PAYMENT_METHOD).getValue();
				if (paymentMethod.equals(getMessages().check())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		Customer clientcustomer = get(CUSTOMER).getValue();
		Account account = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		double amount = get(AMOUNT).getValue();
		boolean istobePrinted = get(TO_BE_PRINTED).getValue();
		customerRefund.setPayTo(clientcustomer.getID());
		customerRefund.setPayFrom(account.getID());
		customerRefund.setPaymentMethod(paymentMethod);
		customerRefund.setIsToBePrinted(istobePrinted);
		String cheqNum = get(CHEQUE_NO).getValue();
		customerRefund.setCheckNumber(cheqNum);
		customerRefund.setNumber((String) get(NUMBER).getValue());
		customerRefund.setMemo(get(MEMO).getValue() == null ? "" : get(MEMO)
				.getValue().toString());
		customerRefund.setTotal(amount);
		customerRefund.setDate(date.getDate());
		customerRefund.setType(ClientCustomerRefund.TYPE_CUSTOMER_REFUNDS);
		customerRefund.setStatus(ClientCustomerRefund.STATUS_OPEN);
		adjustBalance(amount, clientcustomer, customerRefund, context);
		if (clientcustomer.getCurrency() != null) {
			customerRefund.setCurrency(clientcustomer.getCurrency().getID());
		}
		customerRefund.setCurrencyFactor((Double) get(CURRENCY_FACTOR)
				.getValue());

		create(customerRefund, context);

		return null;

	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		makeResult.add(getMessages().total() + " : " + get(AMOUNT).getValue());
	}

	private void adjustBalance(double amount, Customer customer,
			ClientCustomerRefund refund, Context context) {
		double enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			enteredBalance = 0D;
		}

	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateCustomerRefundCommand.this.get(CUSTOMER)
				.getValue()).getCurrency();
	}

}