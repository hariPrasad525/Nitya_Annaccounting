package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.JobActualCostDetail;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class JobActualCostDetailServerReport extends
		AbstractFinaneReport<JobActualCostDetail> {

	private String sectionName = "";
	private String jobName = "";
	private boolean isActualcostDetail;

	public JobActualCostDetailServerReport(
			IFinanceReport<JobActualCostDetail> reportView,
			boolean isActualcostDetail) {
		this.reportView = reportView;
		this.isActualcostDetail = isActualcostDetail;
	}

	public JobActualCostDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), getMessages().memo(),
				getMessages().account(), getMessages().split(),
				getMessages().amount() };
	}

	@Override
	public String getTitle() {

		if (isActualcostDetail) {
			return getMessages().jobActualCostDetail();
		} else {
			return getMessages().jobActualRevenueDetail();
		}
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), getMessages().memo(),
				getMessages().account(), getMessages().split(),
				getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(JobActualCostDetail record) {

		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 8 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getCustomerName();
			addSection(new String[] { sectionName }, new String[] { "", "",
					getMessages().reportTotal(sectionName) }, new int[] { 8 });

		} else if (sectionDepth == 2) {
			this.jobName = record.getJobName();
			addSection(new String[] { "", jobName }, new String[] { "", "", "",
					"", "", getMessages().reportTotal(jobName) },
					new int[] { 8 });
		} else if (sectionDepth == 3) {
			if (!jobName.equals(record.getJobName())) {
				endSection();
			}
			if (!sectionName.equals(record.getCustomerName())) {
				if (!jobName.equals(record.getJobName())) {
					endSection();
				} else {
					endSection();
					endSection();
				}
			}
			if (jobName.equals(record.getJobName())
					&& sectionName.equals(record.getCustomerName())) {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	@Override
	public Object getColumnData(JobActualCostDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return "";
		case 2:
			return getDateByCompanyType(record.getTransactionDate());
		case 3:
			return Utility.getTransactionName(record.getType());
		case 4:
			return record.getNumber();
		case 5:
			return record.getMemo();
		case 6:
			return record.getAccountName();
		case 7:
			return record.getSplitAccountName();
		case 8:
			return record.getTotal();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(JobActualCostDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(JobActualCostDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
