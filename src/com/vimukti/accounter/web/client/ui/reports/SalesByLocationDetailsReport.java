package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.StringReportInput;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;

public class SalesByLocationDetailsReport extends
		AbstractReportView<SalesByLocationDetails> {

	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationDetailsReport(boolean isLocation, boolean isCustomer) {
		this.serverReport = new SalesByLocationDetailsServerReport(this,
				isLocation, isCustomer);
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;
		this.getElement().setId("SalesByLocationDetailsReport");
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByLocationSummary record = (SalesByLocationSummary) data;
		if (record == null) {
			Accounter.createReportService().getSalesByLocationDetailsReport(
					isLocation, isCustomer, start, end, this);
		} else {
			Accounter.createReportService()
					.getSalesByLocationDetailsForLocation(isLocation,
							isCustomer, record.getLocationName(), start, end,
							this);
		}
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(SalesByLocationDetails record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransactionid());
	}

	@Override
	public void export(int generationType) {
		SalesByLocationSummary record = (SalesByLocationSummary) data;
		int reportType;
		if (isCustomer) {
			if (isLocation) {
				reportType = 151;
			} else {
				reportType = 159;
			}
		} else {
			if (isLocation) {
				reportType = 199;
			} else {
				reportType = 200;
			}
		}
		String locationOrClassName;
		if (record != null && record.getLocationName() != null) {
			locationOrClassName = record.getLocationName();
		} else {
			locationOrClassName = "";
		}
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), reportType, new StringReportInput(
				locationOrClassName));
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
	public int sort(SalesByLocationDetails obj1, SalesByLocationDetails obj2,
			int col) {
		String locationOrClassName1 = (obj1.getLocationName() == null || obj1
				.getLocationName().isEmpty()) ? Global.get().messages()
				.notSpecified().toLowerCase() : obj1.getLocationName()
				.toLowerCase();
		String locationOrClassName2 = (obj2.getLocationName() == null || obj2
				.getLocationName().isEmpty()) ? Global.get().messages()
				.notSpecified().toLowerCase() : obj2.getLocationName()
				.toLowerCase();
		int ret = locationOrClassName1.compareTo(locationOrClassName2);
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 1:
			return new ClientFinanceDate(obj1.getDate())
					.compareTo(new ClientFinanceDate(obj2.getDate()));
		case 2:
			String transactionName = Utility.getTransactionName(obj1.getType());
			String transactionName2 = Utility
					.getTransactionName(obj2.getType());
			return transactionName.toLowerCase().compareTo(
					transactionName2.toLowerCase());
		case 3:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getNumber().compareTo(obj2.getNumber());
		case 4:
			return obj1.getAccount().toLowerCase()
					.compareTo(obj2.getAccount().toLowerCase());
		case 5:
			return obj1.getProuductOrService().toLowerCase()
					.compareTo(obj2.getProuductOrService().toLowerCase());
		case 6:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 7:
			return UIUtils.compareDouble(obj1.getBalance(), obj2.getBalance());
		}
		return 0;
	}
}
