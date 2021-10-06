package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ListFilter;

public class CreateMakeDepositCommand extends AbstractTransactionCommand {
	private static final String DEPOSIT_OR_TRANSFER_FROM = "depositOrTransferFrom";
	private static final String DEPOSIT_OR_TRANSFER_TO = "DepositOrTransferTo";
	ClientTransferFund makeDeposit;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_FROM, getMessages()
				.pleaseEnterName(getMessages().fromAccount()), getMessages()
				.fromAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().fromAccount());
			}

			@Override
			public void setValue(Object value) {
				Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
				if (account != null) {
					account = (Account) HibernateUtil.getCurrentSession().load(
							Account.class, account.getID());
					Account from = (Account) value;
					from = (Account) HibernateUtil.getCurrentSession().load(
							Account.class, from.getID());
					String checkDifferentAccounts = checkDifferentAccounts(
							from, account);
					if (checkDifferentAccounts != null) {
						addFirstMessage(checkDifferentAccounts);
						super.setValue(null);
						return;
					}
				}
				super.setValue(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				CreateMakeDepositCommand.this.addCreateAccountCommands(list);
			}

			@Override
			protected List<Account> getLists(Context context) {
				return CreateMakeDepositCommand.this.getAccounts(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						Global.get().messages().Accounts());
			}
		});

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterName(getMessages().depositAccount()), getMessages()
				.depositAccount(), false, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				CreateMakeDepositCommand.this.addCreateAccountCommands(list);
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().depositAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return CreateMakeDepositCommand.this.getAccounts(context);
			}

			@Override
			public void setValue(Object value) {
				Account fromAcc = get(DEPOSIT_OR_TRANSFER_FROM).getValue();
				Account depositTo = (Account) value;
				depositTo = (Account) HibernateUtil.getCurrentSession().load(
						Account.class, depositTo.getID());
				String checkDifferentAccounts = checkDifferentAccounts(fromAcc,
						depositTo);
				if (checkDifferentAccounts != null) {
					addFirstMessage(checkDifferentAccounts);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Account fromAccount = CreateMakeDepositCommand.this.get(
						DEPOSIT_OR_TRANSFER_FROM).getValue();
				Account toAccount = CreateMakeDepositCommand.this.get(
						DEPOSIT_OR_TRANSFER_TO).getValue();
				if (getPreferences().isEnableMultiCurrency()
						&& (!fromAccount
								.getCurrency()
								.getFormalName()
								.equalsIgnoreCase(
										toAccount.getCurrency().getFormalName()))) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected Currency getCurrency() {
				Account account = (Account) get(DEPOSIT_OR_TRANSFER_FROM)
						.getValue();
				return account.getCurrency();
			}
		});
		list.add(new CurrencyAmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				false, true) {
			@Override
			protected Currency getCurrency() {
				Account account = (Account) CreateMakeDepositCommand.this.get(
						DEPOSIT_OR_TRANSFER_FROM).getValue();
				return account.getCurrency();
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	protected String checkDifferentAccounts(Account depositFrom,
			Account depositTo) {
		if (getPreferences().isEnableMultiCurrency()) {
			long primaryCurrencyId = getPreferences().getPrimaryCurrency()
					.getID();
			long from = depositFrom.getCurrency().getID();
			long to = depositTo.getCurrency().getID();
			if (primaryCurrencyId != from && primaryCurrencyId != to) {
				return getMessages()
						.oneOfTheAccountCurrencyShouldBePrimaryCurrency();
			}
		}
		if (depositFrom.getID() == depositTo.getID()) {
			return getMessages().dipositAccountAndTransferAccountShouldBeDiff();
		}
		return null;
	}

	protected void addCreateAccountCommands(CommandList list) {
		list.add(new UserCommand("createBankAccount", getMessages().bank()));
		list.add(new UserCommand("createBankAccount",
				"Create Other CurrentAsset Account", getMessages()
						.otherCurrentAsset()));
		list.add(new UserCommand("createBankAccount",
				"Create Current Liability Account", getMessages()
						.currentLiability()));
		list.add(new UserCommand("createBankAccount", "Create Equity Account",
				getMessages().equity()));
		list.add(new UserCommand("createBankAccount", "Create Paypal Account",
				getMessages().paypal()));
	}

	protected List<Account> getAccounts(Context context) {
		List<Account> filteredList = new ArrayList<Account>();
		for (Account obj : context.getCompany().getAccounts()) {
			if (new ListFilter<Account>() {

				@Override
				public boolean filter(Account e) {
					return e.getIsActive()
							&& Arrays
									.asList(ClientAccount.SUBBASETYPE_CURRENT_ASSET,
											ClientAccount.SUBBASETYPE_CURRENT_LIABILITY,
											ClientAccount.SUBBASETYPE_EQUITY)
									.contains(e.getSubBaseType());
				}
			}.filter(obj)) {
				filteredList.add(obj);
			}
		}
		return filteredList;
	}

	protected boolean isDifferentAccounts(Account depositTo) {
		Account depositFrom = get(DEPOSIT_OR_TRANSFER_FROM).getValue();
		if (depositFrom.getID() == depositTo.getID()) {
			return true;
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		makeDeposit.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		makeDeposit.setNumber(number);

		makeDeposit.setType(ClientTransaction.TYPE_TRANSFER_FUND);

		Account depositOrTransferToAccount = get(DEPOSIT_OR_TRANSFER_TO)
				.getValue();
		makeDeposit.setDepositIn(depositOrTransferToAccount.getID());

		Account depositOrTransferFromAccount = get(DEPOSIT_OR_TRANSFER_FROM)
				.getValue();
		makeDeposit.setDepositFrom(depositOrTransferFromAccount.getID());

		double amount = get(AMOUNT).getValue();
		makeDeposit.setCashBackAmount(amount);

		makeDeposit.setTotal(amount);

		String memo = get(MEMO).getValue();
		makeDeposit.setMemo(memo);
		Currency depositOrTransferFromCurrency = depositOrTransferFromAccount
				.getCurrency();
		Currency depositOrTransferToCurrency = depositOrTransferToAccount
				.getCurrency();
		ClientCurrency primaryCurrency = getPreferences().getPrimaryCurrency();
		if (!primaryCurrency.equals(depositOrTransferToCurrency)) {
			makeDeposit.setCurrency(depositOrTransferToCurrency.getID());
		}
		if (!primaryCurrency.equals(depositOrTransferFromCurrency)) {
			makeDeposit.setCurrency(depositOrTransferFromCurrency.getID());
		}
		makeDeposit.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(makeDeposit, context);

		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {

		double amount = get(AMOUNT).getValue();
		Account depositOrTransferToAccount = get(DEPOSIT_OR_TRANSFER_TO)
				.getValue();
		Account depositOrTransferFromAccount = get(DEPOSIT_OR_TRANSFER_FROM)
				.getValue();
		if (!depositOrTransferToAccount
				.getCurrency()
				.getFormalName()
				.equalsIgnoreCase(
						depositOrTransferFromAccount.getCurrency()
								.getFormalName()))
			makeResult.add("Total" + "("
					+ getPreferences().getPrimaryCurrency().getFormalName()
					+ ")" + ": " + (amount * getCurrencyFactor()));
		makeResult.add("Total" + "("
				+ depositOrTransferFromAccount.getCurrency().getFormalName()
				+ ")" + ": " + (amount));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a transaction to update.");
				return "transactionDetailByAccount";
			}
			makeDeposit = getTransaction(string, AccounterCoreType.MAKEDEPOSIT,
					context);
			if (makeDeposit == null) {
				addFirstMessage(context, "Select a transction to update.");
				return "transactionDetailByAccount ," + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			makeDeposit = new ClientTransferFund();
		}
		setTransaction(makeDeposit);
		return null;
	}

	private void setValues(Context context) {
		get(DATE).setValue(makeDeposit.getDate());
		get(NUMBER).setValue(makeDeposit.getNumber());
		get(DEPOSIT_OR_TRANSFER_FROM).setValue(
				CommandUtils.getServerObjectById(makeDeposit.getDepositFrom(),
						AccounterCoreType.ACCOUNT));
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(makeDeposit.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(AMOUNT).setValue(makeDeposit.getTotal());
		get(MEMO).setValue(makeDeposit.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return makeDeposit.getID() == 0 ? getMessages().creating(
				getMessages().makeDeposit())
				: "Maker deposit is ready to updating ";
	}

	@Override
	protected String getDetailsMessage() {
		return makeDeposit.getID() == 0 ? getMessages().readyToCreate(
				getMessages().makeDeposit())
				: "Make deposit is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_TRANSFER_FUND, getCompany()));
		get(MEMO).setDefaultValue("");
	}

	@Override
	public String getSuccessMessage() {
		return makeDeposit.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().makeDeposit()) : getMessages()
				.updateSuccessfully(getMessages().makeDeposit());
	}

	@Override
	protected Currency getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}
