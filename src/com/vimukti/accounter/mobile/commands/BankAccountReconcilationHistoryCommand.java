package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class BankAccountReconcilationHistoryCommand extends AbstractCommand {

	private static final String BANK_ACCOUNT = "BankAccount";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
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

		get(VIEW_BY).setDefaultValue(getMessages().open());

	}

	@Override
	public String getSuccessMessage() {

		return getMessages().success();

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				return list;
			}
		});
		list.add(new AccountRequirement(BANK_ACCOUNT, getMessages()
				.pleaseEnter(getMessages().bankAccount()), getMessages()
				.bankAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(Account.TYPE_BANK).contains(
									e.getType());
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new ShowListRequirement<Reconciliation>(
				"Reconsolationhistory", "", 20) {

			@Override
			protected String onSelection(Reconciliation value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().ReconciliationsList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().Reconciliation());
			}

			@Override
			protected Record createRecord(Reconciliation value) {
				Record rec = new Record(value);
				rec.add(getMessages().ReconciliationDate(),
						getDateByCompanyType(value.getReconcilationDate()
								.toClientFinanceDate()));
				rec.add(getDateByCompanyType(value.getStartDate()
						.toClientFinanceDate())
						+ "  "
						+ getMessages().to()
						+ " "
						+ getDateByCompanyType(value.getEndDate()
								.toClientFinanceDate()));
				rec.add(getMessages().openingBalance(),
						getAmountWithCurrency(value.getOpeningBalance()));
				rec.add(getMessages().ClosingBalance(),
						getAmountWithCurrency(value.getClosingBalance()));
				return rec;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(Reconciliation e, String name) {
				return false;
			}

			@Override
			protected List<Reconciliation> getLists(Context context) {

				return getListData(context);
			}
		});
	}

	protected List<Reconciliation> getListData(Context context) {
		Account viewBY = get(BANK_ACCOUNT).getValue();
		List<Reconciliation> allRecords = null;

		allRecords = new FinanceTool().getReconciliationslist(viewBY.getID(),
				getCompanyId());

		return allRecords;
	}
}