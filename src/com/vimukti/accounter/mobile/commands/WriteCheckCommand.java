package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PayeeRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class WriteCheckCommand extends AbstractTransactionCommand {

	private static final String PAYEE = "payee";
	private static final String BANK_ACCOUNT = "bankAccount";
	private static final String AMOUNT = "amount";

	ClientWriteCheck writeCheck;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new PayeeRequirement(PAYEE, getMessages().pleaseEnterName(
				getMessages().payee()), getMessages().payee(), false, true,
				new ChangeListner<Payee>() {

					@Override
					public void onSelection(Payee value) {
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							WriteCheckCommand.this
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
				return getMessages().hasSelected(getMessages().payee());
			}

			@Override
			protected List<Payee> getLists(Context context) {
				return new ArrayList<Payee>(context.getCompany().getPayees());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().payee());
			}

			@Override
			protected boolean filter(Payee e, String name) {
				return e.getName().startsWith(name);
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Payee payee = (Payee) WriteCheckCommand.this.get(PAYEE)
						.getValue();
				return payee.getCurrency();
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

		list.add(new AccountRequirement(BANK_ACCOUNT, getMessages()
				.pleaseEnterNameOrNumber(getMessages().bankAccount()),
				getMessages().bankAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().bankAccount());
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
							return e.getIsActive()
									&& Arrays.asList(Account.TYPE_BANK,
											Account.TYPE_OTHER_CURRENT_ASSET)
											.contains(e.getType());
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
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(getMessages().Account()),
				getMessages().Account(), false, true) {

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
			protected void getRequirementsValues(ClientTransactionItem obj) {
				super.getRequirementsValues(obj);
				WriteCheckCommand.this.setAmountValue();
			}

			@Override
			protected Currency getCurrency() {
				return ((Payee) WriteCheckCommand.this.get(PAYEE).getValue())
						.getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return false;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected double getDiscount() {
				Double value2 = WriteCheckCommand.this.get(DISCOUNT).getValue();
				return value2;
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
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
				return e.getName().startsWith(name);
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				WriteCheckCommand.this.setAmountValue();
			}
		});
		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().includeVATwithAmountenabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().includeVATwithAmountDisabled();
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				WriteCheckCommand.this.setAmountValue();
			}
		});

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getMessages().amount()), getMessages().amount(), true, true) {
			@Override
			public void setValue(Object value) {
				if (DecimalUtil.isLessThan((Double) value, 0.0)) {
					addFirstMessage(getMessages().valueCannotBe0orlessthan0(
							getMessages().amount()));
					return;
				}
				super.setValue(value);
			}
		});

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Payee payee = (Payee) get(PAYEE).getValue();

		if (payee != null) {
			// In Edit mode If payee was changed to customer to vendor or
			// anything else
			// previous object should become null;
			writeCheck.setCustomer(0);
			writeCheck.setVendor(0);
			writeCheck.setTaxAgency(0);

			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				writeCheck.setCustomer(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				writeCheck.setVendor(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				writeCheck.setTaxAgency(payee.getID());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
		}
		// if (payee.getType() == Payee.TYPE_CUSTOMER) {
		// writeCheck.setCustomer(payee.getID());
		// } else if (payee.getType() == Payee.TYPE_VENDOR) {
		// writeCheck.setVendor(payee.getID());
		// } else {
		// writeCheck.setTaxAgency(payee.getID());
		// }

		Account bankAccount = get(BANK_ACCOUNT).getValue();
		writeCheck.setBankAccount(bankAccount.getID());
		writeCheck.setToBePrinted(true);

		ClientFinanceDate date = get(DATE).getValue();
		writeCheck.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		writeCheck.setNumber(number);

		Double amount = get(AMOUNT).getValue();
		writeCheck.setAmount(amount);

		writeCheck.setType(ClientTransaction.TYPE_WRITE_CHECK);
		writeCheck.setPaymentMethod("Check");

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		writeCheck.setCurrency(payee.getCurrency().getID());

		writeCheck.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());

		writeCheck.setTransactionItems(accounts);
		updateTotals(context, writeCheck, true);
		// if (amount < writeCheck.getTotal()) {
		// amount = writeCheck.getTotal();
		// writeCheck.setTotal(amount);
		// writeCheck.setAmount(amount);
		// writeCheck.setInWords(amount.toString());
		//
		// }
		String memo = get(MEMO).getValue();
		writeCheck.setMemo(memo);
		if (!DecimalUtil.isEquals(writeCheck.getTotal(), amount)) {
			return new Result(getMessages().amountAndTotalShouldEqual());
		}
		create(writeCheck, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().writeCheck()));
				return "invoicesList";
			}
			writeCheck = getTransaction(string, AccounterCoreType.WRITECHECK,
					context);
			if (writeCheck == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().writeCheck()));
				return "invoicesList " + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			writeCheck = new ClientWriteCheck();
		}
		setTransaction(writeCheck);
		return null;

	}

	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(writeCheck.getCurrencyFactor());
		if (writeCheck.getCustomer() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getCustomer(),
							AccounterCoreType.PAYEE));
		} else if (writeCheck.getVendor() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getVendor(),
							AccounterCoreType.PAYEE));
		} else if (writeCheck.getTaxAgency() != 0) {
			get(PAYEE).setValue(
					CommandUtils.getServerObjectById(writeCheck.getTaxAgency(),
							AccounterCoreType.PAYEE));
		}
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && preferences.isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							writeCheck.getTransactionItems(), context));
		}
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(writeCheck
							.getTransactionItems()));
		}
		get(BANK_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(writeCheck.getBankAccount(),
						AccounterCoreType.ACCOUNT));
		get(DATE).setValue(writeCheck.getDate());
		get(NUMBER).setValue(writeCheck.getNumber());
		get(AMOUNT).setValue(writeCheck.getNetAmount());
		get(ACCOUNTS).setValue(writeCheck.getTransactionItems());
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(writeCheck));

		get(MEMO).setValue(writeCheck.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return writeCheck.getID() == 0 ? getMessages().creating(
				getMessages().writeCheck()) : "Updating write check";
	}

	@Override
	protected String getDetailsMessage() {
		return writeCheck.getID() == 0 ? getMessages().readyToCreate(
				getMessages().writeCheck()) : getMessages().readyToUpdate(
				getMessages().writeCheck());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_WRITE_CHECK,
						context.getCompany()));
		get(AMOUNT).setDefaultValue(0.0);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		if (get(DISCOUNT) != null) {
			get(DISCOUNT).setDefaultValue(0.0);
		}
	}

	private void setAmountValue() {
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		List<ClientTransactionItem> accountItems = get(ACCOUNTS).getValue();
		ClientCompanyPreferences preferences = getPreferences();
		if (!accountItems.isEmpty() && preferences.isTrackTax()
				&& preferences.isTrackPaidTax()
				&& !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = (TAXCode) (get(TAXCODE) == null ? null : get(
					TAXCODE).getValue());
			if (taxCode != null) {
				for (ClientTransactionItem item : accountItems) {
					item.setTaxCode(taxCode.getID());
				}
			}
		}

		double[] result = getTransactionTotal(isVatInclusive, accountItems,
				true);
		Double amount = get(AMOUNT).getValue();
		amount = result[0] + result[1];
		get(AMOUNT).setValue(amount);
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		super.beforeFinishing(context, makeResult);
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		List<ClientTransactionItem> accountItems = get(ACCOUNTS).getValue();
		double[] result = getTransactionTotal(isVatInclusive, accountItems,
				true);
		Double amount = get(AMOUNT).getValue();
		if (!DecimalUtil.isEquals(result[0] + result[1], amount)) {
			makeResult.add(getMessages().amountAndTotalShouldEqual());
		}
	}

	@Override
	public String getSuccessMessage() {
		return writeCheck.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().writeCheck()) : getMessages().updateSuccessfully(
				getMessages().writeCheck());
	}

	@Override
	protected Currency getCurrency() {
		return ((Payee) WriteCheckCommand.this.get(PAYEE).getValue())
				.getCurrency();
	}
}
