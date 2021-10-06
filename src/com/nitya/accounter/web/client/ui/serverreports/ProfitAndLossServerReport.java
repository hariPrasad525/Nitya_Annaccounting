package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.ISectionHandler;
import com.nitya.accounter.web.client.ui.reports.Section;

public class ProfitAndLossServerReport extends
		AbstractFinaneReport<TrialBalance> {
	protected List<String> types = new ArrayList<String>();
	protected List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;

	protected Double totalincome = 0.0D;
	protected Double totalCGOS = 0.0D;
	protected Double grosProft = 0.0D;
	protected Double totalexpese = 0.0D;
	protected Double netProfit = 0.0D;
	protected Double otherIncome = 0.0D;
	protected Double otherExpense = 0.0D;
	protected Double otherNetIncome = 0.0D;

	protected Double totalincome2 = 0.0D;
	protected Double totalCGOS2 = 0.0D;
	protected Double grosProft2 = 0.0D;
	protected Double totalexpese2 = 0.0D;
	protected Double netProfit2 = 0.0D;
	protected Double otherIncome2 = 0.0D;
	protected Double otherExpense2 = 0.0D;
	protected Double otherNetIncome2 = 0.0D;

	// TrialBalance trialBalance
	public ProfitAndLossServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
	}

	public ProfitAndLossServerReport(long startDate, long endDate,
			int generationType, IFinanceReport<TrialBalance> reportView) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
		this.reportView = reportView;
	}

	public ProfitAndLossServerReport(IFinanceReport<TrialBalance> reportView) {
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (getPreferences().getUseAccountNumbers() == true) {
				return record.getAccountNumber();
			} else {
				return null;
			}
		case 1:
			return record.getAccountName();
		case 2:
			return record.getAmount();
		case 3:
			return record.getAmount();
		case 4:
			return record.getTotalAmount();
		case 5:
			return record.getTotalAmount();
		default:
			return null;
		}

	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().financialYearToDate();
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				getMessages().categoryNumber(),
				"",
				// FIXME for reports these are NULL...
				"" + getDateByCompanyType(getStartDate()) + " - "
						+ getDateByCompanyType(getEndDate()), "",
				getHeaderDates(), "" };
	}

	@Override
	public String[] getDynamicHeaders() {

		return new String[] {
				getMessages().categoryNumber(),
				"",
				"" + getDateByCompanyType(getStartDate()) + " - "
						+ getDateByCompanyType(getEndDate()), "",
				getHeaderDates(),

				"" };
	}

	private String getHeaderDates() {
		ClientFinanceDate currentFiscalYearStartDate = getCurrentFiscalYearStartDate();
		ClientFinanceDate lastMonth = getLastMonth(new ClientFinanceDate());
		if (currentFiscalYearStartDate.getYear() == lastMonth.getYear()
				&& currentFiscalYearStartDate.getMonth() > lastMonth.getMonth()) {
			currentFiscalYearStartDate.setYear(currentFiscalYearStartDate
					.getYear() - 1);
		}
		return getDateByCompanyType(currentFiscalYearStartDate) + " - "
				+ getDateByCompanyType(lastMonth);
	}

	@Override
	public String getTitle() {
		return getMessages().profitAndLoss();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getProfitAndLossReport(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(TrialBalance record) {
		if (this.handler == null)
			iniHandler();

		if (sectionDepth == 0) {
			addTypeSection("", getMessages().netProfit());
		}
		addOrdinaryIncomeOrExpenseTypes(record);
		// if (record.getBaseType() ==
		// ClientAccount.BASETYPE_ORDINARY_INCOME_OR_EXPENSE) {
		// addOrdinaryIncomeOrExpenseTypes(record);
		// } else if (record.getBaseType() ==
		// ClientAccount.BASETYPE_OTHER_INCOME_OR_EXPENSE) {
		// addOtherIncomeOrExpenseTypes(record);
		// } else {
		// closeAllSection();
		// }

		if (closePrevSection(record.getParentAccount() == 0 ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	protected void initValues() {
		totalincome = 0.0D;
		totalCGOS = 0.0D;
		grosProft = 0.0D;
		totalexpese = 0.0D;
		netProfit = 0.0D;
		otherIncome = 0.0D;
		otherExpense = 0.0D;
		otherNetIncome = 0.0D;

		totalincome2 = 0.0D;
		totalCGOS2 = 0.0D;
		grosProft2 = 0.0D;
		totalexpese2 = 0.0D;
		netProfit2 = 0.0D;
		otherIncome2 = 0.0D;
		otherExpense2 = 0.0D;
		otherNetIncome2 = 0.0D;
	}

	protected void iniHandler() {

		initValues();
		this.handler = new ISectionHandler<TrialBalance>() {

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {
				if (section.title.equals(getMessages().grossProfit())) {
					section.data[0] = "";
				}
			}

			@Override
			public void OnSectionEnd(Section<TrialBalance> section) {
				if (section.title.equals(getMessages().income())) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(getMessages().costOfGoodSold())) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(getMessages().otherExpense())) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.footer.equals(getMessages().grossProfit())) {
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.title.equals(getMessages().expense())) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}
				if (section.footer.equals(getMessages().netProfit())) {
					netProfit = grosProft - totalexpese + otherIncome;
					section.data[3] = netProfit;
					netProfit2 = grosProft2 - totalexpese2 + otherIncome2;
					section.data[5] = netProfit2;
				}
				if (section.title.equals(getMessages().otherIncome())) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals(getMessages().otherIncomeOrExpense())) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				// if (section.footer == FinanceApplication.constants()
				// .netIncome()) {
				// section.data[1] = netIncome + otherNetIncome;
				// }
			}
		};

	}

	public String getAccountNameById(long id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId() == id)
				return balance.getAccountName();
		return null;
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public void addOrdinaryIncomeOrExpenseTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense())) {
		// closeAllSection();
		// addTypeSection(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense(), FinanceApplication
		// .constants().netOrdinaryIncome());
		// }
		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains(getMessages().grossProfit())) {
				addTypeSection(getMessages().grossProfit(), "", getMessages()
						.grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains(getMessages().income())) {

					addTypeSection(getMessages().income(), getMessages()
							.incomeTotals());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains(getMessages().costOfGoodSold())) {
					closeOtherSections();
					closeSection(types.indexOf(getMessages().income()));
					addTypeSection(getMessages().costOfGoodSold(),
							getMessages().cogsTotal());

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains(getMessages().otherExpense())) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(getMessages().otherExpense(), getMessages()
						.otherExpenseTotals());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains(getMessages().expense())) {
				closeAllSection();
				addTypeSection(getMessages().expense(), getMessages()
						.expenseTotals());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_INCOME) {

			if (sectiontypes.contains(getMessages().expense())) {
				closeSection(types.indexOf(getMessages().expense()));
			} else {
				closeAllSection();
			}

			if (!sectiontypes.contains(getMessages().otherIncome())) {
				// closeSection(types.indexOf(getMessages().expense()));
				addTypeSection(getMessages().otherIncome(), getMessages()
						.otherIncomeTotal());
			}
		}

	}

	public void addOtherIncomeOrExpenseTypes(TrialBalance record) {
		if (!sectiontypes.contains(getMessages().otherIncomeOrExpense())) {
			closeAllSection();
			addTypeSection(getMessages().otherIncomeOrExpense(), getMessages()
					.netOtherIncome());
		}
		// if (record.getAccountType() == ClientAccount.TYPE_OTHER_INCOME) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .otherIncome())) {
		// addTypeSection(FinanceApplication.constants()
		// .otherIncome(), FinanceApplication.constants()
		// .totalOtherIncome());
		// }
		// }

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
			if (!sectiontypes.contains(getMessages().otherExpense())) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(getMessages().otherExpense(), getMessages()
						.otherExpenseTotals());
			}
		}

	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != 0) {
				if (balance.getParentAccount() == record.getAccountId())
					return true;
			}
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			// System.out.println("Add:" + curentParent);
			addSection(
					record.getAccountNumber() + "-" + record.getAccountName(),
					record.getAccountName() + "  " + getMessages().total(),
					new int[] { 3, 5 });
			return true;
		}
		return false;
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					if (sectionDepth > 0)
						endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		if (index >= 0) {
			types.remove(index);
			curentParent = "";
			endSection();
		}
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3, 5 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void addTypeSection(String sectionType, String title,
			String bottomTitle) {
		if (!sectiontypes.contains(sectionType)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3, 5 });
			types.add(title);
			sectiontypes.add(sectionType);
		}
	}

	public boolean isExist(String title) {
		if (!sectiontypes.contains(title))
			return true;
		return false;
	}

	/**
	 * Reset Variables in Report,
	 */
	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
	}

	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {

		switch (index) {
		case 0:
			return 110;
		case 2:
			return 135;
		case 3:
			return 95;
		case 4:
			return 135;
		case 5:
			return 90;

		default:
			break;
		}
		// if (index == 1)
		// return 310;
		// else if (index == 2)
		// return 270;
		// else
		return 260;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	public ClientFinanceDate getLastMonth(ClientFinanceDate date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date.getDateAsObject());
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new ClientFinanceDate(cal.getTime());
	}

}
