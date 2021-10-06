package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.DepreciationShedule;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class DepreciationSheduleServerReport extends
		AbstractFinaneReport<DepreciationShedule> {
	private String sectionName = "";
	private final String currentsectionName = "";
	private final double totalCostOfAnAsset = 0.0D;
	private final double purchaseTotal = 0.0D;

	public DepreciationSheduleServerReport(
			IFinanceReport<DepreciationShedule> reportView) {
		this.reportView = reportView;
	}

	public DepreciationSheduleServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.name(), messages.assetNumber(),
				messages.type(), messages.cost(), messages.rate(),
				messages.purchaseDate(), messages.disposalDate(),
				messages.purchase(), messages.depreciation(),
				messages.dispose(), messages.accumulatedDepreciation(),
				messages.bookValue() };
	}

	@Override
	public String getTitle() {
		return messages.depreciationShedule();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.name(), messages.assetNumber(),
				messages.type(), messages.cost(), messages.rate(),
				messages.purchaseDate(), messages.disposalDate(),
				messages.purchase(), messages.depreciation(),
				messages.dispose(), messages.accumulatedDepreciation(),
				messages.netAmount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_DATE, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 55;
		case 2:
			return 50;
		case 3:
			return 65;
		case 4:
			return 45;
		case 9:
			return 65;
		case 10:
			return 90;
		default:
			return -1;
		}
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public void processRecord(DepreciationShedule record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getAssetAccountName();
			addSection("", messages.total() + " " + sectionName, new int[] { 3,
					7, 8, 9, 10, 11 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getAssetAccountName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(DepreciationShedule record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAssetName();
		case 1:
			return record.getNumber();
		case 2:
			return record.getType();
		case 3:
			return record.getPurchaseCost();
		case 4:
			return record.getDepreciationRate() + "%";
		case 5:
			return getDateByCompanyType(record.getPurchaseDate());
		case 6:
			return getDateByCompanyType(record.getDisposeDate());
		case 7:
			return record.getPurchaseCost();
		case 8:
			return record.getDepreciationAmount();
		case 9:
			return record.getSoldOrDisposalAmount();
		case 10:
			return record.getAccumulatedDepreciationAmount();
		case 11:
			return record.getNetTotalOfAnFixedAssetAmount();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(DepreciationShedule obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(DepreciationShedule obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}
}
