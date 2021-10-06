package com.vimukti.accounter.web.client.ui.win8portlets;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;

public class DateRangeUtil implements IsSerializable {
	private ClientFinanceDate startDate, endDate;
	private AccounterMessages messages = Accounter.getMessages();
	public static final int THIS_MONTH = 0;
	public static final int LAST_MONTH = 1;
	public static final int THIS_FINANCIAL_YEAR = 2;
	public static final int LAST_FINANCIAL_YEAR = 3;
	public static final int THIS_QUARTER = 4;
	public static final int LAST_QUARTER = 5;

	public DateRangeUtil() {
	}

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

	public int getDateRangeType(String dateRange) {
		if (dateRange.equals(messages.thisMonth())) {
			return THIS_MONTH;
		} else if (dateRange.equals(messages.lastMonth())) {
			return LAST_MONTH;
		} else if (dateRange.equals(messages.thisFinancialYear())) {
			return THIS_FINANCIAL_YEAR;
		} else if (dateRange.equals(messages.lastFinancialYear())) {
			return LAST_FINANCIAL_YEAR;
		} else if (dateRange.equals(messages.thisFinancialQuarter())) {
			return THIS_QUARTER;
		} else if (dateRange.equals(messages.lastFinancialQuarter())) {
			return LAST_QUARTER;
		}
		return THIS_MONTH;
	}

	public void dateRangeChanged(String dateRange) {
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (dateRange.equals(messages.all())) {
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (dateRange.equals(messages.today())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
			} else if (dateRange.equals(messages.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
			} else if (dateRange.equals(messages.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
			} else if (dateRange.equals(messages.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
			} else if (dateRange
					.equals(messages.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			} else if (dateRange.equals(messages.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			} else if (dateRange.equals(messages.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endYesterday())) {
				startDate = Accounter.getStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
			} else if (dateRange.equals(messages.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDay());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDay());
			} else if (dateRange.equals(messages.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

			} else if (dateRange.equals(messages.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			} else if (dateRange.equals(messages.lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			} else if (dateRange.equals(messages.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();

			} else if (dateRange.equals(messages.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);

			} else if (dateRange.equals(messages.thisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
			} else if (dateRange.equals(messages.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
			} else if (dateRange.equals(messages.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(endDate.getDateAsObject());
				startCal.set(Calendar.DAY_OF_MONTH,
						startCal.get(Calendar.DAY_OF_MONTH) - 6);
				startDate = new ClientFinanceDate(startCal.getTime());

			} else if (dateRange.equals(messages.thisFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
			} else if (dateRange.equals(messages.lastFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				startDate.setYear(startDate.getYear() - 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(Accounter.getCompany()
						.getCurrentFiscalYearEndDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				endDate.setYear(endDate.getYear() - 1);
			} else if (dateRange.equals(messages.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				getCurrentFiscalYearQuarter();
			} else if (dateRange.equals(messages.lastFinancialQuarter())) {
				getCurrentFiscalYearQuarter();
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(startDate.getDateAsObject());
				startDateCal.set(Calendar.MONTH,
						startDateCal.get(Calendar.MONTH) - 3);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(endDate.getDateAsObject());
				endDateCal.set(Calendar.MONTH,
						endDateCal.get(Calendar.MONTH) - 3);
				startDate = new ClientFinanceDate(startDateCal.getTime());
				endDate = new ClientFinanceDate(endDateCal.getTime());

			} else if (dateRange.equals(messages.financialYearToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				getCurrentQuarter();
			} else if (dateRange.equals(messages.thisVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (dateRange.equals(messages.lastVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				getPreviousQuarter();

			} else if (dateRange.equals(messages.lastVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.nextVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				getNextQuarter();
			} else if (dateRange.equals(messages.custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
			}
			setStartDate(startDate);
			setEndDate(endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getCurrentFiscalYearQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate end = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (!date.before(quarterStart) && !date.after(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
		}
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
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	public void getPreviousQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}
	}

	public void getNextQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear() + 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() + 1, 2, 31);
			break;
		}
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

	public ClientFinanceDate getWeekStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new ClientFinanceDate().getDateAsObject());
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, 1 - i);
		ClientFinanceDate financeDate = new ClientFinanceDate(
				calendar.getTime());
		return financeDate;
	}

	public native double getWeekEndDate()/*-{
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
		return tmp;
	}-*/;
}
