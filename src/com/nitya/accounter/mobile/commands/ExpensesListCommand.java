package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Utility;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ExpensesListCommand extends AbstractTransactionListCommand {

	String type = " ";

	String name = "";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();

		String[] split = string.split(",");

		if (split.length > 0) {
			name = split[0];
			context.setString(name);
		}
		if (split.length > 1) {
			type = split[1];
		}
		if (type.isEmpty()) {
			type = getMessages().all();
		}
		get(VIEW_BY).setDefaultValue(type);
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().all());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<BillsList>(getMessages()
				.expensesList(), getMessages().pleaseSelect(
				getMessages().expensesList()), 20) {

			@Override
			protected String onSelection(BillsList value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().expensesList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(BillsList value) {
				Record record = new Record(value);
				record.add(getMessages().name(),
						Utility.getTransactionName(value.getType()));
				record.add(getMessages().date(),
						getDateByCompanyType(value.getDate()));
				record.add(getMessages().number(), value.getNumber());
				record.add(getMessages().Vendor(), value.getVendorName());
				record.add(getMessages().originalAmount(),
						getAmountWithCurrency(value.getOriginalAmount()));
				record.add(getMessages().balance(),
						getAmountWithCurrency(value.getBalance()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newCashExpense");
				list.add("newCreditCardExpense");
			}

			@Override
			protected boolean filter(BillsList e, String name) {
				return e.getVendorName().startsWith(name);
			}

			@Override
			protected List<BillsList> getLists(Context context) {
				return getExpenses(context);
			}

		});
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private List<BillsList> getExpenses(Context context) {
		FinanceTool tool = new FinanceTool();
		String text = get(VIEW_BY).getValue();
		try {
			ArrayList<BillsList> billsList = tool.getVendorManager()
					.getBillsList(true, context.getCompany().getId(), 0,
							getStartDate().getDate(), getEndDate().getDate(),
							0, -1, getViewByList().indexOf(text) + 1);
			return billsList;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().cash());
		list.add(getMessages().creditCard());
		list.add(getMessages().voided());
		list.add(getMessages().all());
		return list;
	}

}
