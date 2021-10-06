package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class BudgetOverviewServerReport extends
		AbstractFinaneReport<ClientBudgetList> {

	protected List<String> sectiontypes;
	private String sectionName;

	protected Double janincome = 0.0D;
	protected Double febincome = 0.0D;
	protected Double marincome = 0.0D;
	protected Double aprincome = 0.0D;
	protected Double mayincome = 0.0D;
	protected Double junincome = 0.0D;
	protected Double julincome = 0.0D;
	protected Double augincome = 0.0D;
	protected Double septincome = 0.0D;
	protected Double octincome = 0.0D;
	protected Double novincome = 0.0D;
	protected Double decincome = 0.0D;
	protected Double totalincome = 0.0D;

	protected Double janexpense = 0.0D;
	protected Double febexpense = 0.0D;
	protected Double marexpense = 0.0D;
	protected Double aprexpense = 0.0D;
	protected Double mayexpense = 0.0D;
	protected Double junexpense = 0.0D;
	protected Double julexpense = 0.0D;
	protected Double augexpense = 0.0D;
	protected Double septexpense = 0.0D;
	protected Double octexpense = 0.0D;
	protected Double novexpense = 0.0D;
	protected Double decexpense = 0.0D;
	protected Double totalexpense = 0.0D;

	List<String> preparedList = new ArrayList<String>();

	public BudgetOverviewServerReport(
			IFinanceReport<ClientBudgetList> reportView) {
		sectiontypes = new ArrayList<String>();
		this.reportView = reportView;

	}

	public BudgetOverviewServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ClientBudgetList record, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return record.getAccount().getName();
		case 1:
			return record.getAmountMap().get(columnIndex);
		case 2:
			return record.getAmountMap().get(columnIndex);
		case 3:
			return record.getAmountMap().get(columnIndex);
		case 4:
			return record.getAmountMap().get(columnIndex);
		case 5:
			return record.getAmountMap().get(columnIndex);
		case 6:
			return record.getAmountMap().get(columnIndex);
		case 7:
			return record.getAmountMap().get(columnIndex);
		case 8:
			return record.getAmountMap().get(columnIndex);
		case 9:
			return record.getAmountMap().get(columnIndex);
		case 10:
			return record.getAmountMap().get(columnIndex);
		case 11:
			return record.getAmountMap().get(columnIndex);
		case 12:
			return record.getAmountMap().get(columnIndex);
		case 13:
			return record.getTotalAmount();
		}

		return null;

		// switch (columnIndex) {
		// case 0:
		// return record.getAccount().getName();
		// case 1:
		// return Double.toString(record.getJanuaryAmount()
		// + record.getFebrauaryAmount());
		// case 2:
		// return Double.toString(record.getMarchAmount()
		// + record.getAprilAmount());
		// case 3:
		// return Double.toString(record.getMayAmount()
		// + record.getJuneAmount());
		// case 4:
		// return Double.toString(record.getJulyAmount()
		// + record.getAugustAmount());
		// case 5:
		// return Double.toString(record.getSeptemberAmount()
		// + record.getOctoberAmount());
		// case 6:
		// return Double.toString(record.getNovemberAmount()
		// + record.getDecemberAmount());
		// case 7:
		// return record.getTotalAmount();
		// }
		//
		// return null;
	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().all();
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };

		// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
		// COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
		// COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };

	}

	@Override
	public String[] getColunms() {
		ClientFinanceDate fiscalYearStartDate = getCurrentFiscalYearStartDate();
		int firstMonthOfFiscalYear = fiscalYearStartDate.getMonth();
		prepareDynamicHeaderNames(firstMonthOfFiscalYear);
		String arr[] = new String[preparedList.size()];
		preparedList.toArray(arr);
		return arr;

	}

	@Override
	public String getTitle() {
		return getMessages().expenseReport();
	}

	@Override
	public void processRecord(ClientBudgetList record) {

		/*
		 * if (sectionDepth == 0) { if (record.getAccount().getType() ==
		 * ClientAccount.TYPE_EXPENSE) { if (!sectiontypes.contains("Expense"))
		 * { addSection("Expense", "Expense Total", new int[] { 13 });
		 * sectiontypes.add("Expense"); } else { return; } } else if
		 * (record.getAccount().getType() == ClientAccount.TYPE_OTHER_EXPENSE) {
		 * if (!sectiontypes.contains("Other Expense")) {
		 * addSection("Other Expense", "Other Expense Total", new int[] { 13 });
		 * sectiontypes.add("Other Expense"); } else { return; } } else if
		 * (record.getAccount().getType() == ClientAccount.TYPE_OTHER_INCOME) {
		 * if (!sectiontypes.contains("Other Income")) {
		 * addSection("Other Income", "Other Income Total", new int[] { 13 });
		 * sectiontypes.add("Other Income"); } else { return; } } else if
		 * (record.getAccount().getType() == ClientAccount.TYPE_INCOME) { if
		 * (!sectiontypes.contains("Income")) { addSection("Income",
		 * "Income Total", new int[] { 13 }); sectiontypes.add("Income"); } else
		 * { return; } } else if (record.getAccount().getType() ==
		 * ClientAccount.TYPE_COST_OF_GOODS_SOLD) { if
		 * (!sectiontypes.contains("Cost of Good Sold")) {
		 * addSection("Cost of Good Sold", "Cost of Good Sold Total", new int[]
		 * { 13 }); sectiontypes.add("Cost of Good Sold"); } else { return; } }
		 * else { if (!sectiontypes.contains("Other")) { addSection("Other", "",
		 * new int[] { 13 }); sectiontypes.add("Other"); } else { return; } } }
		 * else if (sectionDepth == 1) {
		 * 
		 * if (record.getAccount().getType() != ClientAccount.TYPE_EXPENSE) {
		 * endSection(); } else if (record.getAccount().getType() !=
		 * ClientAccount.TYPE_OTHER_EXPENSE) { endSection(); } else if
		 * (record.getAccount().getType() != ClientAccount.TYPE_OTHER_INCOME) {
		 * endSection(); } else if (record.getAccount().getType() !=
		 * ClientAccount.TYPE_INCOME) { endSection(); } else if
		 * (record.getAccount().getType() !=
		 * ClientAccount.TYPE_COST_OF_GOODS_SOLD) { endSection(); } else {
		 * return; }
		 * 
		 * }
		 */

		if (this.handler == null)
			iniHandler();

		String sectionName1 = null;

		if (record.getAccount().getType() == ClientAccount.TYPE_EXPENSE) {
			sectionName1 = messages.expense();
		} else if (record.getAccount().getType() == ClientAccount.TYPE_OTHER_EXPENSE) {
			sectionName1 = messages.otherExpense();
		} else if (record.getAccount().getType() == ClientAccount.TYPE_OTHER_INCOME) {
			sectionName1 = messages.otherIncome();
		} else if (record.getAccount().getType() == ClientAccount.TYPE_INCOME) {
			sectionName1 = getMessages().income();
		} else if (record.getAccount().getType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			sectionName1 = messages.costOfGoodSold();
		} else {
			return;
		}

		if (sectionDepth == 0) {
			addSection("", messages.netAmount(), new int[] { 13 });
		} else if (sectionDepth == 1) {
			this.sectionName = sectionName1;
			addSection(sectionName, sectionName + messages.total(), new int[] {
					1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(sectionName1)) {
				endSection();
			} else {
				return;
			}

		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	private void iniHandler() {

		initVariables();

		this.handler = new ISectionHandler<ClientBudgetList>() {

			@Override
			public void OnSectionAdd(Section<ClientBudgetList> section) {
				if (section.title != null) {
					if (section.title.equals(getMessages().grossProfit())) {
						section.data[0] = "";
					}
				}
			}

			@Override
			public void OnSectionEnd(Section<ClientBudgetList> section) {

				if (section.title.equals(getMessages().income())) {
					janincome = Double.valueOf(section.data[1].toString());
					febincome = Double.valueOf(section.data[2].toString());
					marincome = Double.valueOf(section.data[3].toString());
					aprincome = Double.valueOf(section.data[4].toString());
					mayincome = Double.valueOf(section.data[5].toString());
					junincome = Double.valueOf(section.data[6].toString());
					julincome = Double.valueOf(section.data[7].toString());
					augincome = Double.valueOf(section.data[8].toString());
					septincome = Double.valueOf(section.data[9].toString());
					octincome = Double.valueOf(section.data[10].toString());
					novincome = Double.valueOf(section.data[11].toString());
					decincome = Double.valueOf(section.data[12].toString());
					totalincome = Double.valueOf(section.data[13].toString());
				}
				if (section.title.equals(getMessages().otherIncome())) {
					janincome = janincome
							+ Double.valueOf(section.data[1].toString());
					febincome = febincome
							+ Double.valueOf(section.data[2].toString());
					marincome = marincome
							+ Double.valueOf(section.data[3].toString());
					aprincome = aprincome
							+ Double.valueOf(section.data[4].toString());
					mayincome = mayincome
							+ Double.valueOf(section.data[5].toString());
					junincome = junincome
							+ Double.valueOf(section.data[6].toString());
					julincome = julincome
							+ Double.valueOf(section.data[7].toString());
					augincome = augincome
							+ Double.valueOf(section.data[8].toString());
					septincome = septincome
							+ Double.valueOf(section.data[9].toString());
					octincome = octincome
							+ Double.valueOf(section.data[10].toString());
					novincome = novincome
							+ Double.valueOf(section.data[11].toString());
					decincome = decincome
							+ Double.valueOf(section.data[12].toString());
					totalincome = totalincome
							+ Double.valueOf(section.data[13].toString());
				}
				if (section.title.equals(getMessages().costOfGoodSold())) {

					janexpense = Double.valueOf(section.data[1].toString());
					febexpense = Double.valueOf(section.data[2].toString());
					marexpense = Double.valueOf(section.data[3].toString());
					aprexpense = Double.valueOf(section.data[4].toString());
					mayexpense = Double.valueOf(section.data[5].toString());
					junexpense = Double.valueOf(section.data[6].toString());
					julexpense = Double.valueOf(section.data[7].toString());
					augexpense = Double.valueOf(section.data[8].toString());
					septexpense = Double.valueOf(section.data[9].toString());
					octexpense = Double.valueOf(section.data[10].toString());
					novexpense = Double.valueOf(section.data[11].toString());
					decexpense = Double.valueOf(section.data[12].toString());
					totalexpense = Double.valueOf(section.data[13].toString());

				}
				if (section.title.equals(getMessages().otherExpense())) {
					janexpense = janexpense
							+ Double.valueOf(section.data[1].toString());
					febexpense = febexpense
							+ Double.valueOf(section.data[2].toString());
					marexpense = marexpense
							+ Double.valueOf(section.data[3].toString());
					aprexpense = aprexpense
							+ Double.valueOf(section.data[4].toString());
					mayexpense = mayexpense
							+ Double.valueOf(section.data[5].toString());
					junexpense = junexpense
							+ Double.valueOf(section.data[6].toString());
					julexpense = julexpense
							+ Double.valueOf(section.data[7].toString());
					augexpense = augexpense
							+ Double.valueOf(section.data[8].toString());
					septexpense = septexpense
							+ Double.valueOf(section.data[9].toString());
					octexpense = octexpense
							+ Double.valueOf(section.data[10].toString());
					novexpense = novexpense
							+ Double.valueOf(section.data[11].toString());
					decexpense = decexpense
							+ Double.valueOf(section.data[12].toString());
					totalexpense = totalexpense
							+ Double.valueOf(section.data[13].toString());
				}
				if (section.title.equals(messages.expense())) {
					janexpense = janexpense
							+ Double.valueOf(section.data[1].toString());
					febexpense = febexpense
							+ Double.valueOf(section.data[2].toString());
					marexpense = marexpense
							+ Double.valueOf(section.data[3].toString());
					aprexpense = aprexpense
							+ Double.valueOf(section.data[4].toString());
					mayexpense = mayexpense
							+ Double.valueOf(section.data[5].toString());
					junexpense = junexpense
							+ Double.valueOf(section.data[6].toString());
					julexpense = julexpense
							+ Double.valueOf(section.data[7].toString());
					augexpense = augexpense
							+ Double.valueOf(section.data[8].toString());
					septexpense = septexpense
							+ Double.valueOf(section.data[9].toString());
					octexpense = octexpense
							+ Double.valueOf(section.data[10].toString());
					novexpense = novexpense
							+ Double.valueOf(section.data[11].toString());
					decexpense = decexpense
							+ Double.valueOf(section.data[12].toString());
					totalexpense = totalexpense
							+ Double.valueOf(section.data[13].toString());
				}
				if (section.footer.equals(messages.netAmount())) {

					section.data[1] = Double.toString(janincome - janexpense);
					section.data[2] = Double.toString(febincome - febexpense);
					section.data[3] = Double.toString(marincome - marexpense);
					section.data[4] = Double.toString(aprincome - aprexpense);
					section.data[5] = Double.toString(mayincome - mayexpense);
					section.data[6] = Double.toString(junincome - junexpense);
					section.data[7] = Double.toString(julincome - julexpense);
					section.data[8] = Double.toString(augincome - augexpense);
					section.data[9] = Double.toString(septincome - septexpense);
					section.data[10] = Double.toString(octincome - octexpense);
					section.data[11] = Double.toString(novincome - novexpense);
					section.data[12] = Double.toString(decincome - decexpense);
					section.data[13] = Double.toString(totalincome
							- totalexpense);
					initVariables();

				}

			}

		};
	}

	private void initVariables() {
		janincome = 0.0D;
		febincome = 0.0D;
		marincome = 0.0D;
		aprincome = 0.0D;
		mayincome = 0.0D;
		junincome = 0.0D;
		julincome = 0.0D;
		augincome = 0.0D;
		septincome = 0.0D;
		octincome = 0.0D;
		novincome = 0.0D;
		decincome = 0.0D;
		totalincome = 0.0D;

		janexpense = 0.0D;
		febexpense = 0.0D;
		marexpense = 0.0D;
		aprexpense = 0.0D;
		mayexpense = 0.0D;
		junexpense = 0.0D;
		julexpense = 0.0D;
		augexpense = 0.0D;
		septexpense = 0.0D;
		octexpense = 0.0D;
		novexpense = 0.0D;
		decexpense = 0.0D;
		totalexpense = 0.0D;
	}

	@Override
	public ClientFinanceDate getEndDate(ClientBudgetList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ClientBudgetList obj) {
		return obj.getStartDate();

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	/*
	 * public int sort(ExpenseList obj1, ExpenseList obj2, int col) { switch
	 * (col) { case 0: return UIUtils.compareInt(obj1.getTransactionType(),
	 * obj2.getTransactionType()); case 1: return
	 * obj1.getTransactionDate().compareTo( obj2.getTransactionDate()); case 2:
	 * return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal()); case 3:
	 * if (!currentsectionName.toLowerCase().equals(
	 * obj1.getName().toLowerCase())) { return obj1.getName().toLowerCase()
	 * .compareTo(obj2.getName().toLowerCase()); } else { return
	 * UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal()); } } return 0; }
	 */

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
	}

	@Override
	public boolean isWiderReport() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		ClientFinanceDate fiscalYearStartDate = getCurrentFiscalYearStartDate();
		int firstMonthOfFiscalYear = fiscalYearStartDate.getMonth();
		prepareDynamicHeaderNames(firstMonthOfFiscalYear);
		String arr[] = new String[preparedList.size()];
		preparedList.toArray(arr);
		return arr;

	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 125;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			return 55;
		case 13:
			return 70;
		}
		return -1;
	}

	private void prepareDynamicHeaderNames(int firstMonthOfFiscalYear) {
		preparedList.clear();
		preparedList.add(messages.name());
		for (int jj = 1; jj <= 12; jj++) {
			if (firstMonthOfFiscalYear > 12) {
				firstMonthOfFiscalYear = 1;
			}
			String monthName = DayAndMonthUtil.monthNames
					.get(firstMonthOfFiscalYear);
			if (monthName != null && !monthName.isEmpty()) {
				preparedList.add(monthName);
			}
			firstMonthOfFiscalYear++;
		}
		preparedList.add(messages.total());
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
