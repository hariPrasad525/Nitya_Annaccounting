package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionSummary;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class InventoryValutionSummaryServerReport extends
		AbstractFinaneReport<InventoryValutionSummary> {

	public InventoryValutionSummaryServerReport(
			IFinanceReport<InventoryValutionSummary> view) {
		this.reportView = view;
	}

	public InventoryValutionSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.itemName(), messages.description(),
				messages.onHand(), messages.avgCost(), messages.assetsTotal(),
				messages.percentOfToTAsset(), messages.salesPrice(),
				messages.retailValue(), messages.perOfTotRetail() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryValutionSummary();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.itemName(), messages.description(),
				messages.onHand(), messages.avgCost(), messages.assetsTotal(),
				messages.percentOfToTAsset(), messages.salesPrice(),
				messages.retailValue(), messages.perOfTotRetail() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_QUATITY, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE };
	}

	@Override
	public void processRecord(InventoryValutionSummary record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { getMessages()
					.total() }, new int[] { 4, 5, 7, 8 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(InventoryValutionSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemName();
		case 1:
			return record.getItemDescription();
		case 2:
			return DecimalUtil.round(record.getOnHand()) + " "
					+ record.getUnit();
		case 3:
			return record.getAvgCost();
		case 4:
			return record.getAssetValue();
		case 5:
			return record.getPercentOfTotAsset();
		case 6:
			return record.getSalesPrice();
		case 7:
			return record.getRetailValue();
		case 8:
			return record.getPercentOfTotRetail();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryValutionSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryValutionSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 2:
			return 60;
		case 3:
			return 90;
		case 4:
			return 100;
		case 5:
			return 70;
		case 6:
			return 90;
		case 7:
			return 100;
		case 8:
			return 70;

		default:
			break;
		}
		return super.getColumnWidth(index);
	}

}
