package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.JobProfitabilityDetailServerReport;

public class JobProfitabilityDetailReport extends
		AbstractReportView<JobProfitabilityDetailByJob> {

	private long customerId;
	private long jobId;

	public JobProfitabilityDetailReport() {
		this.serverReport = new JobProfitabilityDetailServerReport(this);
		this.getElement().setId("JobProfitabilityDetailReport");
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		this.customerId = toolbar.getPayeeId();
		this.jobId = toolbar.getJobId();
		Accounter.createReportService().getJobProfitabilityDetailByJobReport(
				toolbar.getPayeeId(), toolbar.getJobId(), start, end, this);

	}

	public void OnClick(JobProfitabilityDetailByJob record, int rowIndex,
			int columnIndex) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());

		if (columnIndex == 1) {// calls cost method,
			record.setCost(false);
			UIUtils.runAction(record, new ItemActualCostDetailReportAction());

		} else if (columnIndex == 2) {// calls revenue method
			record.setCost(true);
			UIUtils.runAction(record, new ItemActualCostDetailReportAction());
		}

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_JOB;
	}

	@Override
	public void OnRecordClick(JobProfitabilityDetailByJob record) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 190, new NumberReportInput(customerId),
				new NumberReportInput(jobId));
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setPayeeId((Long) map.get("customer"));
		toolbar.setJobId((Long) map.get("job"));
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));

		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		long jobId = toolbar.getJobId();
		long payeeId = toolbar.getPayeeId();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("customer", payeeId);
		map.put("job", jobId);
		return map;
	}

}
