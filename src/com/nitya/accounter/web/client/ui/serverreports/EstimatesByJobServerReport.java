package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.EstimatesByJob;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

/**
 * 
 * @author vimukti10
 * 
 */
public class EstimatesByJobServerReport extends
		AbstractFinaneReport<EstimatesByJob> {

	private String sectionName = "";
	private String jobName = "";

	public EstimatesByJobServerReport(IFinanceReport<EstimatesByJob> reportView) {
		this.reportView = reportView;
	}

	public EstimatesByJobServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), getMessages().memo(),
				getMessages().amount() };
	}

	@Override
	public String getTitle() {
		return getMessages().estimatesbyJob();
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(), getMessages().job(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), getMessages().memo(),
				getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(EstimatesByJob record) {

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
	public Object getColumnData(EstimatesByJob record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return "";
		case 2:
			return getDateByCompanyType(record.getEstimateDate());
		case 3:
			int transactionType = record.getTransactionType();
			if (transactionType != Transaction.TYPE_ESTIMATE) {
				return Utility.getTransactionName(transactionType);
			} else {
				return getEstimateNameByType(record.getEstimateType());
			}
		case 4:
			return record.getNum();
		case 5:
			return record.getMemo();
		case 6:
			return record.getAmount();
		}
		return null;
	}

	private String getEstimateNameByType(int estimateType) {
		String estimateName = messages.quote();
		switch (estimateType) {
		case ClientEstimate.CHARGES:
			estimateName = messages.charge();
			break;
		case ClientEstimate.CREDITS:
			estimateName = messages.credit();
			break;
		case ClientEstimate.SALES_ORDER:
			estimateName = messages.salesOrder();
			break;
		}
		return estimateName;
	}

	@Override
	public ClientFinanceDate getStartDate(EstimatesByJob obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(EstimatesByJob obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 0)
			return 200;
		else if (index == 1)
			return 100;
		else if (index == 2)
			return 100;
		else if (index == 4)
			return 100;
		else if (index == 3)
			return 100;
		else if (index == 5)
			return 100;
		else if (index == 6)
			return 100;
		else
			return 100;
	}
}
