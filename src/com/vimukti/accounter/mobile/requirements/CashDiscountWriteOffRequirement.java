package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CashDiscountWriteOffRequirement extends MultiRequirement<Double> {

	private static final String AMOUNT = "amount";
	private static final String ACCOUNT = "account";

	public CashDiscountWriteOffRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		setValue(0.0D);
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		AccountRequirement accountRequirement = new AccountRequirement(ACCOUNT,
				getMessages().pleaseSelect(getMessages().account()),
				getMessages().account(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getAccounts(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		};
		list.add(accountRequirement);
		AmountRequirement amountRequirement = new AmountRequirement(AMOUNT,
				getMessages().pleaseEnter(getMessages().amount()),
				getMessages().amount(), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getAccount() != null) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		};
		list.add(amountRequirement);
	}

	protected List<Account> getAccounts(Context context) {
		return new ArrayList<Account>(context.getCompany().getAccounts());
	}

	@Override
	protected Result onFinish(Context context) {
		setValue(getRequirement(AMOUNT).getValue());
		return null;
	}

	@Override
	protected String getDisplayValue() {
		return String.valueOf((Double) getValue());
	}

	public void setAccount(Account account) {
		getRequirement(ACCOUNT).setValue(account);
	}

	public Account getAccount() {
		return getRequirement(ACCOUNT).getValue();
	}

}
