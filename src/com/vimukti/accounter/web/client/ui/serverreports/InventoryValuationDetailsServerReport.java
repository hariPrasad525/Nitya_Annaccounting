package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionDetail;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class InventoryValuationDetailsServerReport extends
		AbstractFinaneReport<InventoryValutionDetail> {
	private String sectionName = "";

	private String unitName = "";

	public InventoryValuationDetailsServerReport(
			IFinanceReport<InventoryValutionDetail> view) {
		this.reportView = view;
	}

	public InventoryValuationDetailsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.type(), messages.date(),
				messages.name(), messages.number(), messages.quantity(),
				messages.cost(), messages.onHand(), messages.avgCost(),
				messages.assetsTotal() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryValuationDetails();
	}

	@Override
	public String[] getColunms() {

		return new String[] { messages.type(), messages.date(),
				messages.name(), messages.number(), messages.quantity(),
				messages.cost(), messages.onHand(), messages.avgCost(),
				messages.assetsTotal() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_QUATITY,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };

	}

	@Override
	public void processRecord(InventoryValutionDetail record) {
		if (sectionDepth == 0) {
			this.sectionName = record.getItemName();
			this.unitName = record.getUnit();
			addSection(
					new String[] { sectionName },
					new String[] { getMessages().totalOf()
							+ record.getItemName() }, new int[] { 4, 8 });
			// addSection(sectionName, "", new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getItemName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(InventoryValutionDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getTransactionName(record);
		case 1:
			return getDateByCompanyType(new ClientFinanceDate(
					record.getTransactionDate()));
		case 2:
			return record.getPayeeName();
		case 3:
			return record.getTransactionNo();
		case 4:
			return DecimalUtil.round(record.getQuantity());
		case 5:
			return record.getCost();
		case 6:
			return DecimalUtil.round(record.getOnHand()) + " "
					+ record.getOnHandUnit();
		case 7:
			return record.getAvgCost();
		case 8:
			return record.getQuantity() * record.getCost();
		}
		return null;
	}

	private String getTransactionName(InventoryValutionDetail record) {
		if (record.getEstimateType() != 0) {
			return getEstimateStringByType(record.getEstimateType());
		}
		return ReportUtility.getTransactionName(record.getTransType());
	}

	private String getEstimateStringByType(int type) {
		if (type == ClientEstimate.CREDITS) {
			return messages.credit();
		} else if (type == ClientEstimate.CHARGES) {
			return messages.charge();
		} else if (type == ClientEstimate.SALES_ORDER) {
			return messages.salesOrder();
		}
		return messages.quote();
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 100;
		case 3:
			return 50;
		case 4:
			return 100;
		case 5:
			return 80;
		case 6:
			return 100;
		case 7:
			return 80;
		case 8:
			return 100;
		}
		return -1;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryValutionDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryValutionDetail obj) {
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
	public String getQuantityValue(Object object, int column) {
		if (sectionName != null && !sectionName.isEmpty() && column == 4) {
			List<InventoryValutionDetail> records = getRecords();
			for (InventoryValutionDetail inventoryValutionDetail : records) {
				if (inventoryValutionDetail.getItemName().equals(sectionName)
						&& inventoryValutionDetail.getUnit().equals(
								this.unitName)) {
					Double value = (Double) object;
					return value + " " + inventoryValutionDetail.getUnit();
				}
			}
		}
		return super.getQuantityValue(object, column);
	}

	@Override
	public void resetVariables() {
		this.unitName = "";
		this.sectionDepth = 0;
		this.sectionName = "";
	}
}
