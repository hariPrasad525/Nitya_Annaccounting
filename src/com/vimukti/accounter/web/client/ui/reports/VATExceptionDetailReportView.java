package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.VATExceptionServerReport;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VATExceptionDetailReportView extends AbstractReportView<VATDetail> {

	private long taxReturnId;

	public VATExceptionDetailReportView() {
		super(false, messages.noRecordsToShow());
		this.serverReport = new VATExceptionServerReport(this);

	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {

			List<VATDetail> vatDetail = (List<VATDetail>) data;
			VATDetail obj = vatDetail.get(vatDetail.size() - 1);
			startDate = this.serverReport.getStartDate(obj);
			endDate = this.serverReport.getEndDate(obj);
			DateRangeReportToolbar dateRangeReportToolbar = (DateRangeReportToolbar) this.toolbar;
			dateRangeReportToolbar.fromItem.setEnteredDate(this.startDate);
			dateRangeReportToolbar.toItem.setEnteredDate(this.endDate);
			dateRangeReportToolbar.fromItem.setEnabled(false);
			dateRangeReportToolbar.toItem.setEnabled(false);

			this.serverReport.initRecords(vatDetail);

		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void OnRecordClick(VATDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		Accounter.createReportService().getVATExceptionDetailReport(start, end,
				taxReturnId, this);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 166, new NumberReportInput(this.taxReturnId));
	}

	@Override
	public void printPreview() {

	}

	public int sort(VATDetail obj1, VATDetail obj2, int col) {
		int ret = obj1.getBoxName().compareTo(obj2.getBoxName());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getTransactionName().compareTo(
					obj2.getTransactionName());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareInt(
					Integer.parseInt(obj1.getTransactionNumber()),
					Integer.parseInt(obj2.getTransactionNumber()));
		case 3:
			return obj1.getPayeeName().compareTo(obj2.getPayeeName());
		case 4:
			return UIUtils.compareDouble(obj1.getVatRate(), obj2.getVatRate());
		case 5:
			return UIUtils.compareDouble(obj1.getNetAmount(),
					obj2.getNetAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	public long getTaxReturnId() {
		return taxReturnId;
	}

	public void setTaxReturnId(long taxReturnId) {
		this.taxReturnId = taxReturnId;
	}
}
