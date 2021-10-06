package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class UnBilledCostsByJobServerReport extends
		AbstractFinaneReport<UnbilledCostsByJob> {
	private String sectionName = "";
	private String jobName = "";

	public UnBilledCostsByJobServerReport(
			IFinanceReport<UnbilledCostsByJob> reportView) {
		this.reportView = reportView;
	}

	public UnBilledCostsByJobServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().type(), getMessages().date(),
				getMessages().memo(), getMessages().account(),
				getMessages().amount() };
	}

	@Override
	public String getTitle() {
		return getMessages().unbilledCostsByJob();
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().type(), getMessages().date(),
				getMessages().memo(), getMessages().account(),
				getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(UnbilledCostsByJob record) {

		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 6 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getCustomerName();
			addSection(new String[] { sectionName }, new String[] { "", "",
					getMessages().reportTotal(sectionName) }, new int[] { 6 });

		} else if (sectionDepth == 2) {
			this.jobName = record.getJobName();
			addSection(new String[] { "", jobName }, new String[] { "", "", "",
					"", "", getMessages().reportTotal(jobName) },
					new int[] { 6 });
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
	public Object getColumnData(UnbilledCostsByJob record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return "";
		case 2:
			return Utility.getTransactionName(record.getType());
		case 3:
			return getDateByCompanyType(record.getTransactionDate());
		case 4:
			return record.getMemo();
		case 5:
			return record.getAccountName();
		case 6:
			return record.getAmount();
		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(UnbilledCostsByJob obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(UnbilledCostsByJob obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
