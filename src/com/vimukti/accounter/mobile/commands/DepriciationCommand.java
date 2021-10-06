package com.vimukti.accounter.mobile.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientDepreciationDummyEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.FinanceTool;

public class DepriciationCommand extends AbstractCommand {
	private List<FiscalYear> openFiscalYears;
	private ClientFinanceDate depreciationStartDate;
	private List<String> datesArray;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		datesArray = getDatesArray();
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
		// super.setDefaultValues(context);
		get(VIEW_BY).getDefaultValue();
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// super.addRequirements(list);
		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				return datesArray;
			}
		});
		list.add(new ShowListRequirement<ClientDepreciationDummyEntry>(
				getMessages().depreciation(), getMessages().pleaseSelect(
						getMessages().date()), 20) {
			@Override
			protected String onSelection(ClientDepreciationDummyEntry value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().depreciation();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().depreciation());
			}

			@Override
			protected Record createRecord(ClientDepreciationDummyEntry value) {
				Record record = new Record(value);
				record.add(getMessages().account(), value.getFixedAssetName());
				record.add(getMessages().amounttobeDepreciated(),
						value.getAmountToBeDepreciated());
				record.add(getMessages().accumulatedDepreciationAccount(),
						CommandUtils.getServerObjectById(
								value.getAssetAccount(),
								AccounterCoreType.ACCOUNT));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(ClientDepreciationDummyEntry e, String name) {
				return false;
			}

			@Override
			protected List<ClientDepreciationDummyEntry> getLists(
					Context context) {
				return getDepreciationRecords(context);
			}
		});
	}

	private List<ClientDepreciationDummyEntry> getDepreciationRecords(
			Context context) {
		String viewType = get(VIEW_BY).getValue();
		try {
			ClientFinanceDate clientFinanceDate = new ClientFinanceDate(
					viewType);
			new FinanceTool().getFixedAssetManager().getDepreciableFixedAssets(
					getCompany().getPreferences().getDepreciationStartDate()
							.getDate(), clientFinanceDate.getDate(),
					getCompanyId());
		} catch (Exception e) {
		}
		return new ArrayList<ClientDepreciationDummyEntry>();
	}

	private List<String> getDatesArray() {
		ClientFinanceDate date2 = new ClientFinanceDate(getCompany()
				.getPreferences().getDepreciationStartDate().getDate());
		depreciationStartDate = new ClientFinanceDate(date2.getYear(),
				date2.getMonth(), 1);
		SimpleDateFormat format = new SimpleDateFormat(getPreferences()
				.getDateFormat());
		List<String> dates = new ArrayList<String>();
		Calendar fromDateCal = Calendar.getInstance();
		fromDateCal.setTime(depreciationStartDate.getDateAsObject());

		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(new ClientFinanceDate(getPreferences()
				.getDepreciationStartDate()).getDateAsObject());

		Calendar toDateCal = Calendar.getInstance();
		int year = 0;

		if (fromDateCal.get(Calendar.MONTH) >= startDateCal.get(Calendar.MONTH)) {
			year = fromDateCal.get(Calendar.YEAR) + 1;
		} else {
			year = fromDateCal.get(Calendar.YEAR);
		}

		openFiscalYears = getOpenFiscalYears();

		toDateCal.set(Calendar.YEAR, year);
		int month = 0;
		if (startDateCal.get(Calendar.MONTH) - 1 >= 0) {
			month = startDateCal.get(Calendar.MONTH) - 1;
		}
		toDateCal.set(Calendar.DAY_OF_MONTH, 1);
		toDateCal.set(Calendar.MONTH, month);

		while (fromDateCal.getTime().compareTo(toDateCal.getTime()) < 0) {
			Calendar tempCal = Calendar.getInstance();
			tempCal.set(Calendar.YEAR, fromDateCal.get(Calendar.YEAR));
			tempCal.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH));
			tempCal.set(Calendar.DAY_OF_MONTH,
					fromDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			if (validateDate(new ClientFinanceDate(tempCal.date)))
				dates.add(format.format(tempCal.getTime()));
			fromDateCal
					.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH) + 1);

		}

		return dates;
	}

	public List<FiscalYear> getOpenFiscalYears() {
		Set<FiscalYear> fiscalYears = getCompany().getFiscalYears();

		List<FiscalYear> openFiscalYears = new ArrayList<FiscalYear>();
		for (FiscalYear clientFiscalYear : fiscalYears) {
			if (clientFiscalYear.getStatus() == ClientFiscalYear.STATUS_OPEN)
				openFiscalYears.add(clientFiscalYear);

		}
		return openFiscalYears;
	}

	private boolean validateDate(ClientFinanceDate date) {

		boolean validDate = true;
		for (FiscalYear openFiscalYear : openFiscalYears) {
			ClientFinanceDate endDate = openFiscalYear.getEndDate()
					.toClientFinanceDate();
			/**
			 * The below code will increment the fiscal year end date by 1, this
			 * will be helpful when the date and fiscal year end date are same.
			 **/
			endDate.setDay(endDate.getDay() + 1);
			int before = date.compareTo(openFiscalYear.getStartDate()
					.toClientFinanceDate());
			int after = date.compareTo(endDate);

			validDate = (before < 0 || after > 0) ? false : true;
			if (validDate)
				break;

		}
		return validDate;
	}

	@Override
	public String getId() {
		return null;
	}
}