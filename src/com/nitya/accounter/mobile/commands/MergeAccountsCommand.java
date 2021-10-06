package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.server.FinanceTool;

public class MergeAccountsCommand extends AbstractCommand {

	private static final String ACCOUNT_FROM = "accountFrom";
	private static final String ACCOUNT_TO = "accountTo";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().merging(getMessages().Accounts());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToMerge(getMessages().Accounts());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().mergingCompleted(getMessages().Accounts());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new AccountRequirement(ACCOUNT_FROM, getMessages().payeeFrom(
				getMessages().account()), getMessages().Account(), false, true,
				null) {

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(getCompany().getAccounts());
			}

			@Override
			public void setValue(Object value) {
				Account accountTo = get(ACCOUNT_TO).getValue();
				Account accountFrom = (Account) value;
				String checkDifferentAccounts = null;
				if (accountFrom != null && accountTo != null) {
					checkDifferentAccounts = checkDifferentAccounts(
							accountFrom, accountTo);
				}
				if (checkDifferentAccounts != null) {
					addFirstMessage(checkDifferentAccounts);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeFrom(getMessages().Account()));
			}

			@Override
			protected String getSetMessage() {
				Account value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeFrom(getMessages().Account()));
				}
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}
		});

		list.add(new AccountRequirement(ACCOUNT_TO, getMessages().payeeTo(
				getMessages().account()), getMessages().Account(), false, true,
				null) {

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(getCompany().getAccounts());
			}

			@Override
			public void setValue(Object value) {
				Account accountFrom = get(ACCOUNT_FROM).getValue();
				Account accountTo = (Account) value;
				String checkDifferentAccounts = null;
				if (accountFrom != null && accountTo != null) {
					checkDifferentAccounts = checkDifferentAccounts(
							accountFrom, accountTo);
				}
				if (checkDifferentAccounts != null) {
					addFirstMessage(checkDifferentAccounts);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTo(getMessages().Account()));
			}

			@Override
			protected String getSetMessage() {
				Account value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeTo(getMessages().Account()));
				}
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}
		});

	}

	protected String checkDifferentAccounts(Account accountFrom,
			Account accountTo) {
		if (accountFrom.getID() == accountTo.getID()) {
			return getMessages().notMove(getMessages().Accounts());
		}
		if (getPreferences().isEnableMultiCurrency()) {
			long from = accountFrom.getCurrency().getID();
			long to = accountTo.getCurrency().getID();
			if (from != to) {
				return getMessages().currenciesOfTheBothCustomersMustBeSame(
						getMessages().Accounts());
			}
		}
		if (accountFrom.getType() != accountTo.getType()) {
			return getMessages().typesMustbeSame(getMessages().Accounts());
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		Account accountFrom = get(ACCOUNT_FROM).getValue();
		Account accountTo = get(ACCOUNT_TO).getValue();

		try {
			ClientAccount clientFrom = clientConvertUtil.toClientObject(
					accountFrom, ClientAccount.class);
			ClientAccount clientTo = clientConvertUtil.toClientObject(
					accountTo, ClientAccount.class);

			new FinanceTool().mergeAcoount(clientFrom, clientTo, getCompany()
					.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onCompleteProcess(context);
	}

}
