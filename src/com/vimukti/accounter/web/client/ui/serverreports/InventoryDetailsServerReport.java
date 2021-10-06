package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryDetails;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class InventoryDetailsServerReport extends
		AbstractFinaneReport<InventoryDetails> {

	public InventoryDetailsServerReport(
			IFinanceReport<InventoryDetails> reportView) {
		this.reportView = reportView;
	}

	public InventoryDetailsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.itemName(), messages.qtyIn(),
				messages.cost(), messages.qtyOut(), messages.priceSold(),
				messages.onHandQty(), messages.costvaluation() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryDetails();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.itemName(), messages.qtyIn(),
				messages.cost(), messages.qtyOut(), messages.priceSold(),
				messages.onHandQty(), messages.costvaluation() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(InventoryDetails record) {

	}

	@Override
	public Object getColumnData(InventoryDetails record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemName();
		case 1:
			return DecimalUtil.round(record.getQtyIn());
		case 2:
			return (record.getCost());
		case 3:
			return DecimalUtil.round(record.getQtyOut());
		case 4:
			return record.getPricesold();
		case 5:
			return DecimalUtil.round(record.getOnHandqty());
		case 6:
			return record.getCostValuation();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryDetails obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryDetails obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
		case 2:
		case 3:
		case 4:
			return 120;
		case 5:
		case 6:
			return 140;

		default:
			break;
		}
		return super.getColumnWidth(index);
	}
}
