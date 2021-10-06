package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.TAXReturn;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.managers.TaxManager;

public class TaxHistoryCommand extends AbstractCommand {

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
		get(VIEW_BY).setDefaultValue(getMessages().open());
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

		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().paid());
				list.add(getMessages().unPaid());
				return list;
			}
		});

		list.add(new ShowListRequirement<TAXReturn>(getMessages().taxHistory(),
				getMessages().taxHistory(), 20) {

			@Override
			protected String onSelection(TAXReturn value) {
				return "updatetaxhistory " + value.getTaxAgency().getName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().taxHistory();
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getMessages().taxHistory());

			}

			@Override
			protected Record createRecord(TAXReturn value) {
				Record taxHistory = new Record(value);
				taxHistory.add(getMessages().name(), value.getTaxAgency()
						.getName());
				taxHistory.add(getMessages().periodStartDate(),
						getDateByCompanyType(value.getPeriodStartDate()
								.toClientFinanceDate()));
				taxHistory.add(getMessages().periodEndDate(),
						getDateByCompanyType(value.getPeriodEndDate()
								.toClientFinanceDate()));
				taxHistory.add(getMessages().taxAmount(),
						getAmountWithCurrency(value.getTotalTAXAmount()));
				taxHistory.add(
						getMessages().totalPaymentMade(),
						getAmountWithCurrency(value.getTotal()
								- value.getBalance()));

				return taxHistory;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(TAXReturn e, String name) {
				return false;

			}

			@Override
			protected List<TAXReturn> getLists(Context context) {
				return gettaxReturnEntries(context);
			}

		});

	}

	private List<TAXReturn> gettaxReturnEntries(Context context) {
		TaxManager manager = new TaxManager();
		List<TAXReturn> allTAXReturns = new ArrayList<TAXReturn>();
		List<TAXReturn> result = new ArrayList<TAXReturn>();
		try {
			allTAXReturns = manager.getAllTAXReturnsFromDB(getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		String viewType = get(VIEW_BY).getValue();
		for (TAXReturn tAXReturn : allTAXReturns) {
			if (viewType.equals(getMessages().paid())) {
				if (tAXReturn.getBalance() <= 0) {
					result.add(tAXReturn);
				}
			} else if (viewType.equals(getMessages().unPaid())) {
				if (tAXReturn.getBalance() > 0) {
					result.add(tAXReturn);
				}
			} else {
				result = allTAXReturns;

			}
		}
		return result;
	}
}
