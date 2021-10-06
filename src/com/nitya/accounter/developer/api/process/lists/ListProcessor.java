package com.nitya.accounter.developer.api.process.lists;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.developer.api.core.ApiProcessor;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.IAccounterHomeViewService;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.server.managers.CompanyManager;

public abstract class ListProcessor extends ApiProcessor {
	protected int start;
	protected int length;
	protected Boolean isActive;
	protected IAccounterHomeViewService service;
	protected ClientFinanceDate from;
	protected ClientFinanceDate to;
	protected String viewName;
	protected long companyId;

	@Override
	public void beforeProcess(HttpServletRequest req, HttpServletResponse resp) {
		super.beforeProcess(req, resp);
		service = getS2sSyncProxy(req, "/do/accounter/home/rpc/service",
				IAccounterHomeViewService.class);
	}

	/**
	 * Reads active,start and length parameters
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	protected void initObjectsList(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		isActive = readBoolean(req, "active");

		start = readInt(req, "start", 0);
		if (start < 0) {
			sendFail("Wrong start parameter should not be -ve)");
		}

		length = readInt(req, "length", -1);
		sendFail("Wrong length parameter should not be -ve)");
	}

	/**
	 * Reads viewType, dateType, from, to, start and length
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	protected void initTransactionList(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		viewName = readString(req, "view_type", "all");

		String dateType = readString(req, "date_type", "all");

		if (dateType.equals("custom")) {
			from = getClientFinanceDate(req.getParameter("from"));
			if (from == null) {
				sendFail("Wrong parameter value(s)");
				return;
			}
			to = getClientFinanceDate(req.getParameter("to"));
			if (to == null) {
				sendFail("Wrong parameter value(s)");
				return;
			}
		} else {
			ClientFinanceDate[] dates = dateRangeChanged(dateType);
			if (dates == null) {
				sendFail("Unable get the date ranges.");
				return;
			}
			from = dates[0];
			to = dates[1];
		}

		start = readInt(req, "start", 0);
		if (start < 0) {
			sendFail("Wrong start parameter should not be -ve)");
		}

		length = readInt(req, "length", -1);
		sendFail("Wrong length parameter should not be -ve)");
	}

	private ClientFinanceDate[] dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate startDate = getStartDate(companyId);
		if (startDate == null) {
			return null;
		}
		ClientFinanceDate currentFiscalYearStartDate = getCurrentFiscalYearStartDate();
		ClientFinanceDate currentFiscalYearEndDate = getCurrentFiscalYearEndDate(startDate);
		ClientFinanceDate endDate = currentFiscalYearEndDate;

		if (dateRange.equals("thisweek")) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals("thisMonth")) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals("lastWeek")) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals("lastMonth")) {
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
		}
		if (dateRange.equals("thisFinancialYear")) {
			startDate = currentFiscalYearStartDate;
			endDate = currentFiscalYearEndDate;
		}
		if (dateRange.equals("lastFinancialYear")) {

			startDate = currentFiscalYearStartDate;
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(currentFiscalYearEndDate.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals("thisFinancialQuarter")) {
			ClientFinanceDate[] currentQuarter = getCurrentQuarter();
			startDate = currentQuarter[0];
			endDate = currentQuarter[1];
		}
		if (dateRange.equals("lastFinancialQuarter")) {
			ClientFinanceDate[] currentQuarter = getCurrentQuarter();
			startDate = currentQuarter[0];
			endDate = currentQuarter[1];

			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals("financialYearToDate")) {
			startDate = currentFiscalYearStartDate;
			endDate = new ClientFinanceDate();
		}
		return new ClientFinanceDate[] { startDate, endDate };
	}

	private ClientFinanceDate getStartDate(long companyId) {
		try {
			ClientFinanceDate[] dates = new CompanyManager()
					.getMinimumAndMaximumTransactionDate(companyId);
			return dates[0];
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ClientFinanceDate getCurrentFiscalYearStartDate() {
		Calendar cal = Calendar.getInstance();
		ClientFinanceDate startDate = new ClientFinanceDate();
		cal.setTime(startDate.getDateAsObject());
		Company company = (Company) HibernateUtil.getCurrentSession().load(
				Company.class, companyId);
		cal.set(Calendar.MONTH, company.getPreferences()
				.getFiscalYearFirstMonth());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		while (new ClientFinanceDate(cal.getTime())
				.after(new ClientFinanceDate())) {
			cal.add(Calendar.YEAR, -1);
		}
		startDate = new ClientFinanceDate(cal.getTime());
		return startDate;
	}

	private ClientFinanceDate getCurrentFiscalYearEndDate(
			ClientFinanceDate startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());

		return endDate;
	}

	private ClientFinanceDate[] getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate startDate = null;
		ClientFinanceDate endDate = null;

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
		return new ClientFinanceDate[] { startDate, endDate };
	}

	private int getMonthLastDate(int month, int year) {
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

	private ClientFinanceDate getWeekStartDate() {
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
}
