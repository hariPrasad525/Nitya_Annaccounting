package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.StringReportInput;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ARAgingDetailServerReport;

/**
 * Modified By Ravi Kiran.G
 * 
 */

public class ARAgingDetailReport extends AbstractReportView<AgedDebtors> {

	private String sectionName = "";
	private List<String> sectiontypes = new ArrayList<String>();

	// private int precategory = 1001;

	public ARAgingDetailReport() {
		this.serverReport = new ARAgingDetailServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		DummyDebitor byCustomerDetail = (DummyDebitor) this.data;

		if (byCustomerDetail == null) {
			Accounter.createReportService().getAgedDebtors(start, end, this);
		} else if (byCustomerDetail.getDebitorName() != null) {
			Accounter.createReportService().getAgedDebtors(
					byCustomerDetail.getDebitorName(), start, end, this);
		}
		sectiontypes.clear();
	}

	@Override
	public void OnRecordClick(AgedDebtors record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(),
					record.getTransactionId());
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
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		String customerName = this.data != null ? ((DummyDebitor) this.data)
				.getDebitorName() : "";
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 118, new StringReportInput(customerName));
	}

	@Override
	public void printPreview() {

	}

	public int sort(AgedDebtors obj1, AgedDebtors obj2, int col) {

		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());

		case 1:
			return UIUtils.compareTo(obj1.getDate(), obj2.getDate());

		case 3:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getNumber(), obj2.getNumber());

		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());

		case 4:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

}
