package com.vimukti.accounter.web.client.ui.reports;

import java.util.Date;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public abstract class ReportToolbar extends DynamicForm {
	protected static AccounterMessages messages = Global.get().messages();
	protected static AccounterMessages2 messages2 = Global.get().messages2();
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	private DynamicForm form;
	protected ReportToolBarItemSelectionHandler itemSelectionHandler;
	public static final int TYPE_ACCRUAL = 0;
	public static final int TYPE_CASH = 1;
	private String selectedDateRange = "";
	protected AbstractReportView<?> reportview;
	private long payeeId;
	private int viewId = 0;
	private long jobId;
	private long accId;
	private long wareHouseId;
	public boolean isToolBarComponentChanged;

	public ReportToolbar() {
		super("reportToolbar");
		createControls();
	}

	private void createControls() {
		form = new DynamicForm("form");

		startDate = Accounter.getStartDate();
		endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		itemSelectionHandler = new ReportToolBarItemSelectionHandler() {

			@Override
			public void onItemSelectionChanged(int type,
					ClientFinanceDate startDate, ClientFinanceDate endDate) {

			}
		};
		add(form);
	}

	public ReportToolBarItemSelectionHandler getItemSelectionHandler() {
		return itemSelectionHandler;
	}

	public void setItemSelectionHandler(
			ReportToolBarItemSelectionHandler itemSelectionHandler) {
		this.itemSelectionHandler = itemSelectionHandler;
	}

	public void addItems(FormItem<?>... items) {
		form.add(items);
	}

	public ClientFinanceDate getStartDate() {
		return (startDate == null || startDate.getDate() == 0) ? new ClientFinanceDate()
				: startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return (endDate == null || endDate.getDate() == 0) ? new ClientFinanceDate()
				: endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	@SuppressWarnings("deprecation")
	public void dateRangeChanged(String dateRange) {
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (!getSelectedDateRange().equals(messages.all())
					&& dateRange.equals(messages.all())) {
				setSelectedDateRange(messages.all());
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = Accounter.getCompany().getTransactionStartDate();
				endDate = new ClientFinanceDate();

			}  else if (!getSelectedDateRange().equals(messages2.firstQuarter())
					&& dateRange.equals(messages2.firstQuarter())) {
				int startMonth = 1;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages2.firstQuarter());
			}  else if (!getSelectedDateRange().equals(messages2.secondQuarter())
					&& dateRange.equals(messages2.secondQuarter())) {
				int startMonth = 4;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages2.secondQuarter());
			} else if (!getSelectedDateRange().equals(messages2.thirdQuarter())
					&& dateRange.equals(messages2.thirdQuarter())) {
				int startMonth = 7;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages2.thirdQuarter());
			} else if (!getSelectedDateRange().equals(messages2.fourthQuarter())
					&& dateRange.equals(messages2.fourthQuarter())) {
				int startMonth = 10;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages2.fourthQuarter());
			} else if (!getSelectedDateRange().equals(messages.today())
					&& dateRange.equals(messages.today())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.today());
			} else if (!getSelectedDateRange().equals(messages.endThisWeek())
					&& dateRange.equals(messages.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				setSelectedDateRange(messages.endThisWeek());
			} else if (!getSelectedDateRange().equals(
					messages.endThisWeekToDate())
					&& dateRange.equals(messages.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.endThisWeekToDate());
			} else if (!getSelectedDateRange().equals(messages.endThisMonth())
					&& dateRange.equals(messages.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				setSelectedDateRange(messages.endThisMonth());
			} else if (!getSelectedDateRange().equals(
					messages.endThisMonthToDate())
					&& dateRange.equals(messages.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endThisMonthToDate());
			} else if (!getSelectedDateRange().equals(
					messages.endThisFiscalQuarter())
					&& dateRange.equals(messages.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages.endThisFiscalQuarter());
			} else if (!getSelectedDateRange().equals(
					messages.endThisFiscalQuarterToDate())
					&& dateRange.equals(messages.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endThisFiscalQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					messages.endThisCalanderQuarter())
					&& dateRange.equals(messages.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(messages.endThisCalanderQuarter());
			} else if (!getSelectedDateRange().equals(
					messages.endThisCalanderQuarterToDate())
					&& dateRange
							.equals(messages.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endThisCalanderQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					messages.endThisFiscalYear())
					&& dateRange.equals(messages.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(messages.endThisFiscalYear());
			} else if (!getSelectedDateRange().equals(
					messages.endThisFiscalYearToDate())
					&& dateRange.equals(messages.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endThisFiscalYearToDate());
			} else if (!getSelectedDateRange().equals(
					messages.endThisCalanderYear())
					&& dateRange.equals(messages.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(messages.endThisCalanderYear());
			} else if (!getSelectedDateRange().equals(
					messages.endThisCalanderYearToDate())
					&& dateRange.equals(messages.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endThisCalanderYearToDate());
				changeDates(startDate, endDate);
			} else if (!getSelectedDateRange().equals(messages.endYesterday())
					&& dateRange.equals(messages.endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = Accounter.getStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
				setSelectedDateRange((messages.endYesterday()));
			} else if (!getSelectedDateRange().equals(
					messages.endPreviousFiscalQuarter())
					&& dateRange.equals(messages.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endPreviousFiscalQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					messages.endLastCalendarQuarter())
					&& dateRange.equals(messages.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.endLastCalendarQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					messages.previousFiscalYearSameDates())
					&& dateRange.equals(messages.previousFiscalYearSameDates())) {
				setSelectedDateRange(messages.previousFiscalYearSameDates());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					messages.previousFiscalYearSameDates())
					&& dateRange.equals(messages.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDay());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDay());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				setSelectedDateRange(messages.previousFiscalYearSameDates());
			} else if (!getSelectedDateRange().equals(
					messages.lastCalenderYear())
					&& dateRange.equals(messages.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				setSelectedDateRange(messages.lastCalenderYear());
			} else if (!getSelectedDateRange().equals(
					messages.previousCalenderYear())
					&& dateRange.equals(messages.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				// startDate = new ClientFinanceDate(this.startDate.getYear() -
				// 1,
				// this.startDate
				// .getMonth(), this.startDate.getDate());
				// startDate = new ClientFinanceDate(this.endDate.getYear() - 1,
				// this.endDate
				// .getMonth(), this.endDate.getDate());
				// startDate.setYear(this.startDate.getYear() - 1);
				// endDate.setYear(this.endDate.getYear() - 1);
				setSelectedDateRange(messages.previousCalenderYear());
			} else if (!getSelectedDateRange().equals(messages.lastMonth())
					&& dateRange.equals(messages.lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					Date dateAsObject = date.getDateAsObject();
					int month = dateAsObject.getMonth();
					day = getMonthLastDate(month - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(messages.lastMonth());
			} else if (!getSelectedDateRange().equals(messages.last3Months())
					&& dateRange.equals(messages.last3Months())) {
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
				setSelectedDateRange(messages.last3Months());
			} else if (!getSelectedDateRange().equals(messages.last6Months())
					&& dateRange.equals(messages.last6Months())) {
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
				setSelectedDateRange(messages.last6Months());
			} else if (!getSelectedDateRange().equals(messages.lastYear())
					&& dateRange.equals(messages.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(messages.lastYear());
			} else if (!getSelectedDateRange().equals(messages.present())
					&& dateRange.equals(messages.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.present());

			} else if (!getSelectedDateRange()
					.equals(messages.untilEndOfYear())
					&& dateRange.equals(messages.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				setSelectedDateRange(messages.untilEndOfYear());
			} else if ((!getSelectedDateRange().equals(messages.thisWeek()))
					&& dateRange.equals(messages.thisWeek())) {
				startDate = getWeekStartDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
				setSelectedDateRange(messages.thisWeek());
			} else if (!getSelectedDateRange().equals(messages.thisMonth())
					&& dateRange.equals(messages.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				setSelectedDateRange(messages.thisMonth());
			} else if (!getSelectedDateRange().equals(messages.lastWeek())
					&& dateRange.equals(messages.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(endDate.getDateAsObject());
				startCal.set(Calendar.DAY_OF_MONTH,
						startCal.get(Calendar.DAY_OF_MONTH) - 6);
				startDate = new ClientFinanceDate(startCal.getTime());

				setSelectedDateRange(messages.lastWeek());

			} else if (!getSelectedDateRange().equals(
					messages.thisFinancialYear())
					&& dateRange.equals(messages.thisFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(messages.thisFinancialYear());
			} else if (!getSelectedDateRange().equals(
					messages.lastFinancialYear())
					&& dateRange.equals(messages.lastFinancialYear())) {
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
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(messages.lastFinancialYear());
			} else if (!getSelectedDateRange().equals(
					messages.thisFinancialQuarter())
					&& dateRange.equals(messages.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(messages.thisFinancialQuarter());
				getCurrentFiscalYearQuarter();
			} else if (!getSelectedDateRange().equals(
					messages.lastFinancialQuarter())
					&& dateRange.equals(messages.lastFinancialQuarter())) {
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
				setSelectedDateRange(messages.lastFinancialQuarter());
				// getCurrentQuarter();
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);

			} else if (!getSelectedDateRange().equals(
					messages.financialYearToDate())
					&& dateRange.equals(messages.financialYearToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.financialYearToDate());
			} else if (!getSelectedDateRange()
					.equals(messages.thisVATQuarter())
					&& dateRange.equals(messages.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(messages.thisVATQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					messages.thisVATQuarterToDate())
					&& dateRange.equals(messages.thisVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.thisVATQuarterToDate());
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (!getSelectedDateRange()
					.equals(messages.lastVATQuarter())
					&& dateRange.equals(messages.lastVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.lastVATQuarter());
				getPreviousQuarter();

			} else if (!getSelectedDateRange().equals(
					messages.lastVATQuarterToDate())
					&& dateRange.equals(messages.lastVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.lastVATQuarterToDate());
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (!getSelectedDateRange()
					.equals(messages.nextVATQuarter())
					&& dateRange.equals(messages.nextVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(messages.nextVATQuarter());
				getNextQuarter();
			} else if (!getSelectedDateRange().equals(messages.custom())
					&& dateRange.equals(messages.custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
				setSelectedDateRange(messages.custom());
			}
			setStartDate(startDate);
			setEndDate(endDate);
			changeDates(startDate, endDate);
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

	// public native double getWeekStartDate()/*-{
	// var date= new ClientFinanceDate();
	// var day=date.getDay();
	// var newDate=new ClientFinanceDate();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	public native double getWeekEndDate()/*-{
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
		return tmp;
	}-*/;

	public abstract void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setDefaultDateRange(String defaultDateRange);

	public void setDateRanageOptions(String... dateRanages) {

	}

	public AbstractReportView<?> getView() {
		return reportview;
	}

	public void setView(AbstractReportView<?> view) {
		this.reportview = view;
	}

	/**
	 * @param selectedDateRange
	 *            the selectedDateRange to set
	 */
	public void setSelectedDateRange(String selectedDateRange) {
		this.selectedDateRange = selectedDateRange;
	}

	/**
	 * @return the selectedDateRange
	 */
	public String getSelectedDateRange() {
		return selectedDateRange;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
		payeeData();
	}

	protected void payeeData() {

	}

	public void setViewId(int viewOpen) {
		this.viewId = viewOpen;
	}

	public int getViewId() {
		return viewId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
		jobData();
	}

	protected void jobData() {

	}

	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
		accountData();
	}

	protected void accountData() {

	}

	public long getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(long wareHouseId) {
		this.wareHouseId = wareHouseId;
		wareHouseData();
	}

	protected void wareHouseData() {

	}
}
