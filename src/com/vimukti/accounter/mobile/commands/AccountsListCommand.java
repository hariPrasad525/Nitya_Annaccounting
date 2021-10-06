package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountsListCommand extends AbstractCommand {

	private static final String ACCOUNT_TYPE = "accountType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<Account>(getMessages().Accounts(),
				getMessages().pleaseEnter(getMessages().name()), 20) {

			@Override
			protected Record createRecord(Account value) {
				Record record = new Record(value);
				record.add(getMessages().name(), value.getName());
				record.add(
						getMessages().balance(),
						getAmountWithCurrency(value.getTotalBalance(), value
								.getCurrency().getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createAccount");
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> list = new ArrayList<Account>();
				Set<Account> accounts = context.getCompany().getAccounts();
				String type = AccountsListCommand.this.get(ACCOUNT_TYPE)
						.getValue();
				if (type.equals(getMessages().allAccounts())) {
					return new ArrayList<Account>(accounts);
				}
				if (type.equals(getMessages().activeAccounts())) {
					for (Account a : accounts) {
						if (a.getIsActive()) {
							list.add(a);
						}
					}
				} else {
					for (Account a : accounts) {
						if (!a.getIsActive()) {
							list.add(a);
						}
					}
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().payeeList(getMessages().Accounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(Account value) {
				return "editAccount " + value.getName();
			}
		});

		list.add(new CommandsRequirement(ACCOUNT_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().activeAccounts());
				list.add(getMessages().inAllactiveAccounts());
				list.add(getMessages().allAccounts());
				return list;
			}
		});

	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;

	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_TYPE).setDefaultValue(getMessages().allAccounts());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String[] split = string.split(",");
		if (split.length > 2) {
			context.setString(split[0]);
		}
		return null;
	}
}
