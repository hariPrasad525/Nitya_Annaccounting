package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class BillsAndExpensesListCommand extends AbstractTransactionListCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isKeepTrackofBills()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
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
		get(VIEW_BY).setDefaultValue(getMessages().open());

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<BillsList>("BillsAndExpenses", "", 20) {

			@Override
			protected String onSelection(BillsList value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().bills();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().bills());
			}

			@Override
			protected Record createRecord(BillsList value) {
				Record rec = new Record(value);
				rec.add(getMessages().transactionName(),
						Utility.getTransactionName((value.getType())));
				rec.add(getMessages().date(),
						getDateByCompanyType(value.getDate()));
				rec.add(getMessages().number(), value.getNumber());
				rec.add(Global.get().Vendor(), value.getVendorName());
				String symbol = getServerObject(Currency.class,
						value.getCurrency()).getSymbol();

				rec.add(getMessages().originalAmount(),
						getAmountWithCurrency(value.getOriginalAmount(), symbol));
				rec.add(getMessages().balance(),
						getAmountWithCurrency(value.getBalance() == null ? 0.0
								: value.getBalance(), symbol));
				return rec;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newEnterBill");
			}

			@Override
			protected boolean filter(BillsList e, String name) {
				return e.getVendorName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<BillsList> getLists(Context context) {

				return getListData(context);
			}
		});
	}

	/**
	 * 
	 * @param context
	 * @return {@link List<BillsList>}
	 */
	protected List<BillsList> getListData(Context context) {
		String viewBY = get(VIEW_BY).getValue();
		ArrayList<BillsList> allRecords = new ArrayList<BillsList>();
		try {
			allRecords = new FinanceTool().getVendorManager().getBillsList(
					false, context.getCompany().getID(), 0,
					getStartDate().getDate(), getEndDate().getDate(), 0, -1,
					checkViewType(viewBY));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return allRecords;
	}

	private int checkViewType(String view) {
		if (view.equalsIgnoreCase(getMessages().open())) {
			return (VIEW_OPEN);
		} else if (view.equalsIgnoreCase(getMessages().voided())) {
			return (VIEW_VOIDED);
		} else if (view.equalsIgnoreCase(getMessages().overDue())) {
			return (VIEW_OVERDUE);
		} else if (view.equalsIgnoreCase(getMessages().drafts())) {
			return (VIEW_DRAFT);
		}
		return TYPE_ALL;
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().voided());
		list.add(getMessages().overDue());
		list.add(getMessages().drafts());
		list.add(getMessages().all());
		return list;
	}

}