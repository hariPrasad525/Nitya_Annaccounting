package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Lists.DummyDebitor;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.ARAgingSummaryServerReport;

public class ARAgingSummaryReport extends AbstractReportView<DummyDebitor> {

	public ARAgingSummaryReport() {
		this.serverReport = new ARAgingSummaryServerReport(this);
	}

	@Override
	public void OnRecordClick(DummyDebitor record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				PayablesAndReceivablesReportsAction.arAgingDetail());

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		Accounter.createReportService().getDebitors(start, end, this);

	}

	@Override
	public void onEdit() {

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
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 117);
	}

	@Override
	public void printPreview() {

	}

	public int sort(DummyDebitor obj1, DummyDebitor obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getDebitorName().toLowerCase()
					.compareTo(obj2.getDebitorName().toLowerCase());

		case 1:
			return UIUtils.compareDouble(
					(obj1.getDebitdays_in30() + obj1.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_incurrent()));
		case 2:
			return UIUtils.compareDouble(obj1.getDebitdays_in60(),
					obj2.getDebitdays_in60());
		case 3:
			return UIUtils.compareDouble(obj1.getDebitdays_in90(),
					obj2.getDebitdays_in90());
		case 4:
			return UIUtils.compareDouble(obj1.getDebitdays_inolder(),
					obj2.getDebitdays_inolder());
		case 5:
			return UIUtils.compareDouble(
					(obj1.getDebitdays_in30() + obj1.getDebitdays_in60()
							+ obj1.getDebitdays_in90()
							+ obj1.getDebitdays_inolder() + obj1
							.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_in60()
							+ obj2.getDebitdays_in90()
							+ obj2.getDebitdays_inolder() + obj2
							.getDebitdays_incurrent()));
		}
		return 0;
	}

}
