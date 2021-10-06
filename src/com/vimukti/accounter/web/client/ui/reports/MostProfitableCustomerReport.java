package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.MostProfitableCustomerServerReport;

/**
 * 
 * @author kumar kasimala
 * 
 * 
 */

public class MostProfitableCustomerReport extends
		AbstractReportView<MostProfitableCustomers> {

	public MostProfitableCustomerReport() {
		this.serverReport = new MostProfitableCustomerServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getMostProfitableCustomers(start, end,
				this);
	}

	@Override
	public void OnRecordClick(MostProfitableCustomers record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, SalesReportsAction.customerDetail());
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 147);
	}

	@Override
	public void printPreview() {

	}

	public int sort(MostProfitableCustomers obj1, MostProfitableCustomers obj2,
			int col) {
		switch (col) {
		case 0:
			return obj1.getCustomer().toLowerCase()
					.compareTo(obj2.getCustomer().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getInvoicedAmount(),
					obj2.getInvoicedAmount());
		case 2:
			return UIUtils.compareDouble(obj1.getCost(), obj2.getCost());
		case 3:
			return UIUtils.compareDouble(obj1.getMargin(), obj2.getMargin());
		case 4:
			return UIUtils.compareDouble(obj1.getMarginPercentage(),
					obj2.getMarginPercentage());
		}
		return 0;
	}

}
