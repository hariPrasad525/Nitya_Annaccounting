package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.UserCommand;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.requirements.VendorRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.exception.AccounterException;

public class CreateCashExpenseCommand extends AbstractTransactionCommand {
	ClientCashPurchase cashPurchase;

	@Override
	protected String getWelcomeMessage() {
		return cashPurchase.getID() == 0 ? getMessages().create(
				getMessages().cashExpense())
				: "Update Cash Expense Command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return cashPurchase.getID() == 0 ? getMessages().readyToCreate(
				getMessages().cashExpense()) : getMessages().readyToUpdate(
				getMessages().cashExpense());
	}

	@Override
	public String getSuccessMessage() {
		return cashPurchase.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().cashExpense()) : getMessages()
				.updateSuccessfully(getMessages().cashExpense());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(
				getMessages().bankAccount()), getMessages().bankAccount(),
				false, false, new ChangeListner<Account>() {

					@Override
					public void onSelection(Account value) {
						double mostRecentTransactionCurrencyFactor;
						try {
							mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateCashExpenseCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive()
									&& (e.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET || e
											.getType() == ClientAccount.TYPE_CREDIT_CARD);
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

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount",
						"Create Other CurrentAsset Account", getMessages()
								.otherCurrentAsset()));
				list.add(new UserCommand("createBankAccount",
						"Create Cash Account", getMessages().cash()));
				list.add(new UserCommand("createBankAccount",
						"Create Creditcard Account", getMessages().creditCard()));
				list.add(new UserCommand("createBankAccount",
						"Create Inventory Account", getMessages()
								.inventoryAsset()));
				list.add(new UserCommand("createBankAccount",
						"Create Paypal Account", getMessages().paypal()));
			}
		});
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().Vendor(), true, true,
				null) {

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
				Account account = (Account) CreateCashExpenseCommand.this.get(
						PAY_FROM).getValue();
				return account.getCurrency();
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
						getMessages().directDebit(),
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

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()
						&& !getPreferences().isDiscountPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseSelect(getMessages().account()),
				getMessages().Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account account) {
							if (account.getIsActive()
									&& account.getType() != Account.TYPE_CASH
									&& account.getType() != Account.TYPE_BANK
									&& account.getType() != Account.TYPE_INVENTORY_ASSET
									&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != Account.TYPE_INCOME
									&& account.getType() != Account.TYPE_OTHER_INCOME
									&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != Account.TYPE_OTHER_ASSET
									&& account.getType() != Account.TYPE_EQUITY
									&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
								return true;
							} else {
								return false;
							}
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return false;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Currency getCurrency() {
				return ((Account) CreateCashExpenseCommand.this.get(PAY_FROM)
						.getValue()).getCurrency();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCashExpenseCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});
		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().name()), getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isIBuyThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected Currency getCurrency() {
				return ((Account) CreateCashExpenseCommand.this.get(PAY_FROM)
						.getValue()).getCurrency();
			}

			@Override
			protected double getCurrencyFactor() {
				return CreateCashExpenseCommand.this.getCurrencyFactor();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateCashExpenseCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});
		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTrackPaidTax()
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& getPreferences().isTrackPaidTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context, getMessages()
					.transactiontotalcannotbe0orlessthan0());
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		cashPurchase.setType(ClientTransaction.TYPE_CASH_EXPENSE);
		Vendor vendor = get(VENDOR).getValue();
		if (vendor != null)
			cashPurchase.setVendor(vendor.getID());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		Account account = get(PAY_FROM).getValue();
		cashPurchase.setPayFrom(account.getID());
		ClientFinanceDate date = get(DATE).getValue();
		cashPurchase.setDate(date.getDate());
		String number = get(NUMBER).getValue();
		cashPurchase.setNumber(number);
		String memoText = get(MEMO).getValue();
		cashPurchase.setMemo(memoText);

		items.addAll(accounts);
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		cashPurchase.setTransactionItems(items);
		cashPurchase.setCurrency(account.getCurrency().getID());
		cashPurchase
				.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		updateTotals(context, cashPurchase, false);
		create(cashPurchase, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().cashExpense()));
				return "expensesList ," + getMessages().cash();
			}
			cashPurchase = getTransaction(string,
					AccounterCoreType.CASHPURCHASE, context);

			if (cashPurchase == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().cashExpense()));
				return "expensesList " + string + "," + getMessages().cash();
			}
			setValues(context);
		} else {
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			cashPurchase = new ClientCashPurchase();
		}
		setTransaction(cashPurchase);
		return null;
	}

	/***
	 * set init data
	 * 
	 * @param context
	 */
	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(cashPurchase.getCurrencyFactor());
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItems = cashPurchase
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && getPreferences().isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							cashPurchase.getTransactionItems(), context));
			get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(cashPurchase));
		}
		if (preferences.isTrackDiscounts()
				&& !preferences.isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(cashPurchase
							.getTransactionItems()));
		}
		get(ACCOUNTS).setValue(accounts);
		get(ITEMS).setValue(items);
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getVendor(),
						AccounterCoreType.VENDOR));
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						cashPurchase.getPaymentMethodForCommands(),
						getMessages()));
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(DATE).setValue(cashPurchase.getDate());
		get(NUMBER).setValue(cashPurchase.getNumber());
		get(MEMO).setValue(cashPurchase.getMemo());

	}

	@Override
	protected void setDefaultValues(Context context) {
		get(NUMBER).setValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CASH_EXPENSE,
						context.getCompany()));
		get(DISCOUNT).setDefaultValue(0.0);
	}

	@Override
	protected Currency getCurrency() {
		return ((Account) CreateCashExpenseCommand.this.get(PAY_FROM)
				.getValue()).getCurrency();
	}

}
