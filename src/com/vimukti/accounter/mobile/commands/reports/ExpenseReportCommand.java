package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.server.FinanceTool;

public class ExpenseReportCommand extends NewAbstractReportCommand<ExpenseList> {

	private static String EXPENSETYPE = "expenseType";

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(EXPENSETYPE, getMessages()
				.pleaseSelectExpenseType(), "Expense Type", true, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] statusArray = new String[] {
						getMessages().allExpenses(), getMessages().cash(),
						getMessages().creditCard() };
				return Arrays.asList(statusArray);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<ExpenseList>() {

			@Override
			protected String onSelection(ExpenseList selection, String name) {
				return "updateTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<ExpenseList> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<ExpenseList>> recordGroups = new HashMap<String, List<ExpenseList>>();
				for (ExpenseList transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount.getName();
					List<ExpenseList> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<ExpenseList>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				for (String accountName : keySet) {
					List<ExpenseList> group = recordGroups.get(accountName);
					addSelection(accountName);
					double totalAmount = 0.0;
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (ExpenseList rec : group) {
						totalAmount += rec.getTotal();
						Record createReportRecord = createReportRecord(rec);
						resultList.add(createReportRecord);
					}
					makeResult.add(resultList);
					makeResult.add(getMessages().total()
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(ExpenseList record) {
		Record expenseRecord = new Record(record);
		expenseRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactionType()));
		expenseRecord.add(getMessages().transactionDate(),
				getDateByCompanyType(record.getTransactionDate()));
		expenseRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getTotal()));
		return expenseRecord;
	}

	protected List<ExpenseList> getRecords() {
		List<ExpenseList> expensesList = new ArrayList<ExpenseList>();
		try {
			expensesList = new FinanceTool().getReportManager()
					.getExpenseReportByType(getExpenseType(), getStartDate(),
							getEndDate(), getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
			expensesList = new ArrayList<ExpenseList>();
		}
		return expensesList;
	}

	private int getExpenseType() {
		String type = get(EXPENSETYPE).getValue();
		if (type.equals(getMessages().cash())) {
			return ClientTransaction.TYPE_CASH_EXPENSE;
		} else if (type.equals(getMessages().creditCard())) {
			return ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
		}
		return 0;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(EXPENSETYPE).setDefaultValue(getMessages().allExpenses());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().viewActivatedSuccessfully(
				getMessages().expenseReport());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages()
				.viewDetailsAsFollows(getMessages().expenseReport());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().viewClosedSuccessfully(
				getMessages().expenseReport());
	}
}
