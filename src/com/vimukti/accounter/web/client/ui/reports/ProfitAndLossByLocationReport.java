package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;

public class ProfitAndLossByLocationReport extends
		AbstractReportView<ProfitAndLossByLocation> {

	private int category_type;
	ArrayList<ClientAccounterClass> reportHeaders = new ArrayList<ClientAccounterClass>();

	public ProfitAndLossByLocationReport(int category_type) {
		this.category_type = category_type;
		int numcolumns = 0;
		if (category_type == ProfitAndLossByLocationServerReport.JOB) {
			ProfitAndLossByLocationServerReport.jobs = Accounter.getCompany()
					.getJobs();
			numcolumns = ProfitAndLossByLocationServerReport.jobs.size() + 2;
		} else if (category_type == ProfitAndLossByLocationServerReport.LOCATION) {
			ProfitAndLossByLocationServerReport.locations = Accounter
					.getCompany().getLocations();
			numcolumns = ProfitAndLossByLocationServerReport.locations.size() + 2;
		} else {
			ProfitAndLossByLocationServerReport.classes = getHeaderTitles();
			numcolumns = ProfitAndLossByLocationServerReport.classes.size() + 2;
		}
		ProfitAndLossByLocationServerReport.noColumns = numcolumns;
		this.serverReport = new ProfitAndLossByLocationServerReport(this,
				category_type);
	}

	private ArrayList<ClientAccounterClass> getHeaderTitles() {
		ArrayList<ClientAccounterClass> accounterClasses = new ArrayList<ClientAccounterClass>(
				Accounter.getCompany().getAccounterClasses());
		// sort by depth
		Collections.sort(accounterClasses,
				new Comparator<ClientAccounterClass>() {
					@Override
					public int compare(ClientAccounterClass o1,
							ClientAccounterClass o2) {
						int res = o1.getPath().compareTo(o2.getPath());
						return true ? (-1 * res) : (res);
					}
				});
		long previousparentID = 0;
		int count = 0;

		for (ClientAccounterClass clientAccounterClass : accounterClasses) {
			String className = clientAccounterClass.getClassName();
			if (previousparentID != clientAccounterClass.getParent()
					&& previousparentID != 0) {
				String concat = className.concat("-Other");
				clientAccounterClass.setModifiedName(concat);
				reportHeaders.add(clientAccounterClass);
				ClientAccounterClass totalobj = new ClientAccounterClass();
				totalobj.setModifiedName(messages.total() + "(" + className
						+ ")");
				reportHeaders.add(totalobj);
				previousparentID = 0;
			} else if (accounterClasses.size() - 1 == count) {
				String concat = className.concat("-Other");
				clientAccounterClass.setModifiedName(concat);
				reportHeaders.add(clientAccounterClass);
				ClientAccounterClass totalobj = new ClientAccounterClass();
				totalobj.setModifiedName(messages.total() + "(" + className
						+ ")");
				reportHeaders.add(totalobj);
			} else {
				reportHeaders.add(clientAccounterClass);
				previousparentID = clientAccounterClass.getParent();
			}
			count++;
		}
		return reportHeaders;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getProfitAndLossByLocationReport(
				category_type, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ProfitAndLossByLocation p) {
		TrialBalance record = getTrailBalance(p);
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				new TransactionDetailByAccountAndCategoryAction());
	}

	public void OnClick(ProfitAndLossByLocation pAndLossByLocation,
			int rowIndex, int cellIndex) {
		TrialBalance record = getTrailBalance(pAndLossByLocation);

		if (category_type == ProfitAndLossByLocationServerReport.JOB) {
			ClientJob clientJob = ProfitAndLossByLocationServerReport.jobs
					.get(cellIndex - 1);
			record.setCategoryId(clientJob.getID());
		} else if (category_type == ProfitAndLossByLocationServerReport.LOCATION) {
			ClientLocation clientLocation = ProfitAndLossByLocationServerReport.locations
					.get(cellIndex - 1);
			record.setCategoryId(clientLocation.getID());
		} else {
			ClientAccounterClass clientAccounterClass = ProfitAndLossByLocationServerReport.classes
					.get(cellIndex - 1);
			record.setCategoryId(clientAccounterClass.getID());
		}
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				new TransactionDetailByAccountAndCategoryAction());
	}

	private TrialBalance getTrailBalance(ProfitAndLossByLocation p) {
		TrialBalance record = new TrialBalance();
		record.setAccountId(p.getAccountId());
		record.setAccountType(p.getAccountType());
		record.setAccountName(p.getAccountName());
		record.setAccountNumber(p.getAccountNumber());
		record.setCategoryType(category_type);
		return record;
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
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	@Override
	public void export(int generationType) {
		int reportType = 0;
		if (category_type == 1) {
			reportType = 161;
		} else if (category_type == 2) {
			reportType = 153;
		} else {
			reportType = 189;
		}
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), reportType);
	}

}
