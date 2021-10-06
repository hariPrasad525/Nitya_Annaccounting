package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.BudgetActuals;
import com.nitya.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetVsActualsServerReport extends
		AbstractFinaneReport<BudgetActuals> {

	protected List<String> sectiontypes;
	private String sectionName;
	protected Double totalActual;
	protected Double totalBudget;
	protected Double totalOver;
	protected Double totalRemaining;

	public BudgetVsActualsServerReport(IFinanceReport<BudgetActuals> reportView) {
		sectiontypes = new ArrayList<String>();
		this.reportView = reportView;

	}

	public BudgetVsActualsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(BudgetActuals record, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return record.getAccountName();
		case 1:
			return record.getAtualAmount();
		case 2:
			return record.getBudgetAmount();
		case 3:
			return record.getAtualAmount() - record.getBudgetAmount();
		case 4:
			return record.getBudgetAmount() - record.getAtualAmount();
		}

		return null;

	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().all();
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };

	}

	@Override
	public int getColumnWidth(int index) {

		if (index == 0) {
			return 200;
		} else {
			return -1;
		}
	}

	@Override
	public String[] getColunms() {

		return new String[] { messages.accountName(), messages.actual(),
				messages.budget(), messages.overBudget(), messages.remaining() };
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public void processRecord(BudgetActuals record) {

		if (this.handler == null)
			iniHandler();

		String sectionName1 = null;
		int type = record.getType();
		if (type == ClientAccount.TYPE_EXPENSE) {
			sectionName1 = messages.expense();
		} else if (type == ClientAccount.TYPE_OTHER_EXPENSE) {
			sectionName1 = messages.otherExpense();
		} else if (type == ClientAccount.TYPE_OTHER_INCOME) {
			sectionName1 = messages.otherIncome();
		} else if (type == ClientAccount.TYPE_INCOME) {
			sectionName1 = getMessages().income();
		} else if (type == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			sectionName1 = messages.costOfGoodSold();
		} else {
			return;
		}

		if (sectionDepth == 0) {
			addSection("", messages.total(), new int[] { 1, 2 });
		} else if (sectionDepth == 1) {
			this.sectionName = sectionName1;
			addSection(sectionName, sectionName + messages.total(), new int[] {
					1, 2, 3, 4 });
		} else if (sectionDepth == 2) {
			if (!sectionName.equals(sectionName1)) {
				endSection();
			} else {
				return;
			}

		}
		processRecord(record);

	}

	private void iniHandler() {

		initVariables();

		this.handler = new ISectionHandler<BudgetActuals>() {

			@Override
			public void OnSectionAdd(Section<BudgetActuals> section) {
				if (section.title != null) {
					if (section.title.equals(getMessages().total())) {
						section.data[0] = "";
					}
				}
			}

			@Override
			public void OnSectionEnd(Section<BudgetActuals> section) {

				if (section.title.equals(getMessages().income())) {

					totalActual = Double.valueOf(section.data[1].toString());
					totalBudget = Double.valueOf(section.data[2].toString());
					totalOver = Double.valueOf(section.data[3].toString());
					totalRemaining = Double.valueOf(section.data[4].toString());
					section.data[1] = Double.toString(totalActual);
					section.data[2] = Double.toString(totalBudget);
					section.data[3] = Double.toString(totalOver);
					section.data[4] = Double.toString(totalRemaining);
					initVariables();
				}
				if (section.title.equals(getMessages().otherIncome())) {

				}
				if (section.title.equals(getMessages().costOfGoodSold())) {

				}
				if (section.title.equals(getMessages().otherExpense())) {

				}
				if (section.title.equals(messages.expense())) {

				}

			}

		};
	}

	private void initVariables() {
		totalActual = 0.0D;
		totalBudget = 0.0D;
		totalOver = 0.0D;
		totalRemaining = 0.0D;
	}

	@Override
	public ClientFinanceDate getEndDate(BudgetActuals obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(BudgetActuals obj) {
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

		return new String[] { messages.accountName(), messages.actual(),
				messages.budget(), messages.overBudget(), messages.remaining() };

	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
