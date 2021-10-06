package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class IncomeByCustomerDetailServerReport extends
		AbstractFinaneReport<IncomeByCustomerDetail> {

	private double previousTotal = 0.0D;
	private String sectionName;
	private String jobName;

	public IncomeByCustomerDetailServerReport(
			IFinanceReport<IncomeByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public IncomeByCustomerDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().Customer(), getMessages().job(),
				getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().Account(),
				getMessages().credit(), getMessages().debit(),
				getMessages().balance() };
	}

	@Override
	public String getTitle() {
		return getMessages2().incomeByCustomerDetail(Global.get().Customer());
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().Customer(), getMessages().job(),
				getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().Account(),
				getMessages().credit(), getMessages().debit(),
				getMessages().balance() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(IncomeByCustomerDetail record) {
		if (sectionDepth == 0) {
			addSection(
					new String[] { "" },
					new String[] {
							getMessages().reportTotal(Global.get().Customers()),
							"", "" }, new int[] { 6, 7, 8 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getName();
			if (!sectionName.equals("")) {
				addSection(new String[] { sectionName }, new String[] { "", "",
						getMessages().reportTotal(sectionName) },
						new int[] { 8 });
			} else {
				addSection(new String[] { sectionName }, new String[] { "", "",
						getMessages().reportTotal(getMessages().other()) },
						new int[] { 8 });
			}
			previousTotal = 0.0D;
		} else if (sectionDepth == 2) {
			this.jobName = record.getJobName();
			if (jobName.equalsIgnoreCase("")) {
				addSection(
						new String[] { "", getMessages().others() },
						new String[] {
								"",
								"",
								"",
								"",
								getMessages().reportTotal(
										getMessages().others()), "" },
						new int[] { 6, 7 });
			} else {
				addSection(new String[] { "", jobName }, new String[] { "", "",
						"", "", getMessages().reportTotal(jobName), "" },
						new int[] { 6, 7 });
			}
			previousTotal = 0.0D;
		} else if (sectionDepth == 3) {
			if (!jobName.equals(record.getJobName())) {
				endSection();
			}
			if (!sectionName.equals(record.getName())) {
				if (!jobName.equals(record.getJobName())) {
					endSection();
				} else if (!jobName.equalsIgnoreCase("")) {
					endSection();
				} else {
					endSection();
					endSection();
				}
			}
			if (jobName.equals(record.getJobName())
					&& sectionName.equals(record.getName())) {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public void updateTotals(Object[] values) {
		Double value = (Double) values[8];
		values[8] = Double.valueOf(String.valueOf(values[7]))
				- Double.valueOf(String.valueOf(values[6]));
		super.updateTotals(values);
		values[8] = value;
	}

	@Override
	public Object getColumnData(IncomeByCustomerDetail record, int index) {
		switch (index) {
		case 0:
			return "";
		case 1:
			return "";
		case 2:
			return Utility.getTransactionName(record.getTransactionType());
		case 3:
			return record.getTransactionDate();
		case 4:
			return record.getTransactionNumber();
		case 5:
			return record.getAccountName();
		case 6:
			return record.getCredit();
		case 7:
			return record.getDebit();
		case 8:
			double total = record.getDebit() - record.getCredit();
			previousTotal = previousTotal + total;
			return previousTotal;
		default:
			break;
		}
		return "";
	}

	@Override
	public ClientFinanceDate getStartDate(IncomeByCustomerDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 4) {
			return 80;
		}
		return 100;
	}

	@Override
	public ClientFinanceDate getEndDate(IncomeByCustomerDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
