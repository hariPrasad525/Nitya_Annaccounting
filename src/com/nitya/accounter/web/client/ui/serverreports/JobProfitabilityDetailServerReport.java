package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class JobProfitabilityDetailServerReport extends
		AbstractFinaneReport<JobProfitabilityDetailByJob> {

	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_INVENTORY_PART = 2;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	public static final int TYPE_INVENTORY_ASSEMBLY = 4;
	private long itemType;

	public JobProfitabilityDetailServerReport(
			IFinanceReport<JobProfitabilityDetailByJob> reportView) {
		this.reportView = reportView;
	}

	public JobProfitabilityDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", "Act. Cost", "Act. Revenue", "Difference" };
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", "Act. Cost", "Act. Revenue", "Difference" };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(JobProfitabilityDetailByJob record) {
		if (sectionDepth == 0) {
			this.itemType = record.getItemType();
			String type = getItemTypeName(itemType);
			addSection(new String[] { type }, new String[] { getMessages()
					.total() }, new int[] { 1, 2, 3 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (this.itemType != record.getItemType()) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(JobProfitabilityDetailByJob record, int index) {
		switch (index) {
		case 0:
			return record.getItemName();
		case 1:
			return record.getCostAmount();
		case 2:
			return record.getRevenueAmount();
		case 3:
			return record.getRevenueAmount() - record.getCostAmount();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(JobProfitabilityDetailByJob obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(JobProfitabilityDetailByJob obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	private String getItemTypeName(long type) {

		switch ((int) type) {
		case TYPE_SERVICE:
			return messages.serviceItem();
		case TYPE_INVENTORY_PART:
			return messages.inventoryItem();
		case TYPE_NON_INVENTORY_PART:
			return messages.nonInventoryItem();
		case TYPE_INVENTORY_ASSEMBLY:
			return messages.inventoryAssembly();
		}
		return "";
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
		case 1:
		case 2:
		case 3:
			return 230;
		}
		return -1;
	}

}
