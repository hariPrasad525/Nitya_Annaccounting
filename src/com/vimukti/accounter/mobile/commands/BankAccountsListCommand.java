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

public class BankAccountsListCommand extends AbstractCommand {
	private static final String VIEW_TYPE = "Current View";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VIEW_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<Account>(getMessages().Accounts(),
				getMessages().pleaseSelect(getMessages().account()), 20) {
			@Override
			protected Record createRecord(Account value) {
				Record record = new Record(value);
				record.add(getMessages().name(), value.getName());
				record.add(
						getMessages().balance(),
						getAmountWithCurrency(value
								.getTotalBalanceInAccountCurrency(), value
								.getCurrency().getSymbol()));
				return record;
			}

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// Account value) {
			// commandList.add(new UserCommand("Bank Registers", value
			// .getName()));
			// commandList.add(new UserCommand("Edit account",
			// value.getName()));
			// commandList.add(new UserCommand("Delete BankAccout", value
			// .getID()));
			//
			// }

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createNewBankAccount");
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> completeList = getAccounts(context);
				List<Account> result = new ArrayList<Account>();

				String type = get(VIEW_TYPE).getValue();

				for (Account account : completeList) {

					if (type.equals(getMessages().active())) {
						if (account.getIsActive())

							result.add(account);
					}
					if (type.equals(getMessages().inActive())) {
						if (!account.getIsActive())
							result.add(account);
					}

				}
				return result;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().bankAccount();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(Account value) {
				return "editBankAccount " + value.getName();
			}

		});
	}

	private List<Account> getAccounts(Context context) {
		Set<Account> accounts = context.getCompany().getAccounts();
		List<Account> result = new ArrayList<Account>();
		for (Account account : accounts) {
			if (account.getType() == Account.TYPE_BANK) {
				result.add(account);
			}
		}
		return result;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
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
		get(VIEW_TYPE).setDefaultValue(getMessages().active());

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

}
