package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.core.Calendar;

public abstract class AbstractTransactionListCommand extends AbstractCommand {

	private ClientFinanceDate startDate;
	private ClientFinanceDate endDate;
	public static final int VIEW_ALL = 0;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_DRAFT = 4;
	public static final int TYPE_ALL = 1000;

	@Override
	protected void setDefaultValues(Context context) {
		List<ClientFinanceDate> dates = CommandUtils
				.getMinimumAndMaximumTransactionDate(getCompanyId());
		startDate = dates.get(0);
		if (startDate == null) {
			startDate = new ClientFinanceDate();
		}
		get(FROM_DATE).setDefaultValue(startDate);
		endDate = CommandUtils.getCurrentFiscalYearEndDate(getPreferences());
		get(TO_DATE).setDefaultValue(endDate);
		get(DATE_RANGE).setDefaultValue(getMessages().all());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		if (!(this instanceof JournalEntryListCommand)) {
			list.add(new CommandsRequirement(VIEW_BY) {

				@Override
				protected List<String> getList() {
					return getViewByList();
				}
			});
		}
		list.add(new CommandsRequirement(DATE_RANGE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().thisWeek());
				list.add(getMessages().thisMonth());
				list.add(getMessages().lastWeek());
				list.add(getMessages().lastMonth());
				list.add(getMessages().thisFinancialYear());
				list.add(getMessages().lastFinancialYear());
				list.add(getMessages().thisFinancialQuarter());
				return list;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				dateRangeChanged((String) value);
			}
		});

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				setStartDate((ClientFinanceDate) value);
			}
		});

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				setEndDate((ClientFinanceDate) value);
			}
		});

	}

	protected abstract List<String> getViewByList();

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(getMessages().thisWeek())) {
			setStartDate(getWeekStartDate());
			getEndDate().setDay(getStartDate().getDay() + 6);
			getEndDate().setMonth(getStartDate().getMonth());
			getEndDate().setYear(getStartDate().getYear());
		}
		if (dateRange.equals(getMessages().thisMonth())) {
			setStartDate(new ClientFinanceDate(date.getYear(), date.getMonth(),
					1));
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			setEndDate(new ClientFinanceDate(endCal.getTime()));

		}
		if (dateRange.equals(getMessages().lastWeek())) {
			setEndDate(getWeekStartDate());
			getEndDate().setDay(getEndDate().getDay() - 1);
			setStartDate(new ClientFinanceDate(getEndDate().getDate()));
			getStartDate().setDay(getStartDate().getDay() - 6);

		}
		if (dateRange.equals(getMessages().lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				setStartDate(new ClientFinanceDate(date.getYear() - 1, 11, 1));
				setEndDate(new ClientFinanceDate(date.getYear() - 1, 11, day));
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				setStartDate(new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1));
				setEndDate(new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day));
			}
		}
		if (dateRange.equals(getMessages().thisFinancialYear())) {
			setStartDate(CommandUtils
					.getCurrentFiscalYearStartDate(getPreferences()));
			setEndDate(CommandUtils
					.getCurrentFiscalYearEndDate(getPreferences()));
		}
		if (dateRange.equals(getMessages().lastFinancialYear())) {

			setStartDate(CommandUtils
					.getCurrentFiscalYearStartDate(getPreferences()));
			getStartDate().setYear(getStartDate().getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(CommandUtils.getCurrentFiscalYearEndDate(
					getPreferences()).getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			setEndDate(new ClientFinanceDate(endCal.getTime()));
			getEndDate().setYear(getEndDate().getYear() - 1);

		}
		if (dateRange.equals(getMessages().thisFinancialQuarter())) {
			setStartDate(new ClientFinanceDate());
			setEndDate(CommandUtils
					.getCurrentFiscalYearEndDate((getPreferences())));
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(getMessages().lastFinancialQuarter())) {
			setStartDate(new ClientFinanceDate());
			setEndDate(CommandUtils
					.getCurrentFiscalYearEndDate(getPreferences()));
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			getStartDate().setYear(getStartDate().getYear() - 1);
			getEndDate().setYear(getEndDate().getYear() - 1);
		}
		if (dateRange.equals(getMessages().financialYearToDate())) {
			setStartDate(CommandUtils
					.getCurrentFiscalYearStartDate(getPreferences()));
			setEndDate(new ClientFinanceDate());
		}

		FinanceDate[] dates = CommandUtils.getMinimumAndMaximumDates(
				getStartDate(), getEndDate(), getCompanyId());
		get(FROM_DATE).setValue(dates[0].toClientFinanceDate());
		get(TO_DATE).setValue(dates[1].toClientFinanceDate());
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			setStartDate(new ClientFinanceDate(date.getYear(), 0, 1));
			setEndDate(new ClientFinanceDate(date.getYear(), 2, 31));
			break;

		case 2:
			setStartDate(new ClientFinanceDate(date.getYear(), 3, 1));
			setEndDate(new ClientFinanceDate(date.getYear(), 5, 30));
			break;

		case 3:
			setStartDate(new ClientFinanceDate(date.getYear(), 6, 1));
			setEndDate(new ClientFinanceDate(date.getYear(), 8, 30));
			break;
		default:
			setStartDate(new ClientFinanceDate(date.getYear(), 9, 1));
			setEndDate(new ClientFinanceDate(date.getYear(), 11, 31));
			break;
		}
	}
}
