package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.core.Item;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.ItemActualCostDetail;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class ItemActualCostDetailServerReport extends
		AbstractFinaneReport<ItemActualCostDetail> {
	private String sectionName = "";
	private String itemName = "";
	private boolean isActualCostDetail;

	public ItemActualCostDetailServerReport(
			IFinanceReport<ItemActualCostDetail> reportView,
			boolean isActualCostDetail) {
		this.reportView = reportView;
		this.isActualCostDetail = isActualCostDetail;
	}

	public ItemActualCostDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().itemName(), getMessages().type(),
				getMessages().date(), getMessages().number(),
				Global.get().customer(), getMessages().memo(),
				getMessages().quantity(), getMessages().amount() };
	}

	@Override
	public String getTitle() {
		if (isActualCostDetail) {
			return messages.itemActualRevenueDetail();
		} else {
			return messages.itemActualCostDetail();
		}
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().itemName(),
				getMessages().date(), getMessages().number(),
				Global.get().customer(), getMessages().memo(),
				getMessages().quantity(), getMessages().amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(ItemActualCostDetail record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "" }, new String[] { "", "", "", "",
		// getMessages().total() }, new int[] { 7 });
		// } else
		if (sectionDepth == 0) {
			String itemType = getItemType(record.getItemType());
			this.sectionName = itemType;
			addSection(new String[] { sectionName }, new String[] { "", "",
					getMessages().reportTotal(sectionName) }, new int[] { 7 });

		} else if (sectionDepth == 1) {
			this.itemName = record.getItemName();
			addSection(new String[] { "", itemName }, new String[] { "", "",
					"", "", "", "" }, new int[] { 7 });
		} else if (sectionDepth == 2) {
			if (!itemName.equals(record.getItemName())) {
				endSection();
			}
			if (!sectionName.equals(getItemType(record.getItemType()))) {
				if (!itemName.equals(record.getItemName())) {
					endSection();
				} else {
					endSection();
					endSection();
				}
			}
			if (itemName.equals(record.getItemName())
					&& sectionName.equals(getItemType(record.getItemType()))) {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	public String getItemType(int itemType) {
		String itemTypeName = null;
		switch (itemType) {
		case Item.TYPE_SERVICE:
		case Item.TYPE_NON_INVENTORY_PART:
			itemTypeName = Global.get().messages().productOrServiceItem();
			break;
		case Item.TYPE_INVENTORY_PART:
			itemTypeName = Global.get().messages().inventory();
			break;
		case Item.TYPE_INVENTORY_ASSEMBLY:
			itemTypeName = Global.get().messages().assemblyItem();
		default:
			break;
		}
		return itemTypeName;
	}

	@Override
	public Object getColumnData(ItemActualCostDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return Utility.getTransactionName(record.getTransationType());
		case 2:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return record.getCustomerName();
		case 5:
			return record.getMemo();
		case 6:
			return record.getQuantity();
		case 7:
			return record.getAmount();
		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(ItemActualCostDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ItemActualCostDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

}
