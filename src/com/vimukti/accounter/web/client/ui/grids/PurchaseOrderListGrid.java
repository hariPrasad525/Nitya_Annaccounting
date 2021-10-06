package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListView;

public class PurchaseOrderListGrid extends BaseListGrid<PurchaseOrdersList> {

	public PurchaseOrderListGrid(PurchaseOrderListView purchaseOrderListView) {
		super(false);
		this.getElement().setId("PurchaseOrderListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.number(),
				Global.get().messages().payeeName(Global.get().Vendor()),
				messages.purchasePrice() };
	}

	@Override
	protected Object getColumnValue(PurchaseOrdersList obj, int index) {
		switch (index) {
		case 0:
			return UIUtils.getDateByCompanyType(obj.getDate());
		case 1:
			return obj.getNumber();
		case 2:
			return obj.getVendorName();
		case 3:
			return DataUtils.amountAsStringWithCurrency(obj.getPurchasePrice(),
					obj.getCurrency());

		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(PurchaseOrdersList obj) {
		if (isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 100;
		} else if (index == 2) {
			return 100;
		} else if (index == 1 || index == 3) {
			return 150;
		}
		return -1;
	}

	@Override
	protected void onClick(PurchaseOrdersList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType()))
			return;
		super.onClick(obj, row, col);
	}

	@Override
	protected void executeDelete(PurchaseOrdersList object) {

	}

	public AccounterCoreType getType() {
		return null;
	}

	@Override
	protected int sort(PurchaseOrdersList obj1, PurchaseOrdersList obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
		case 1:
			Integer num1 = Integer.parseInt(obj1.getNumber());
			Integer num2 = Integer.parseInt(obj2.getNumber());
			return num1.compareTo(num2);
		case 2:
			String name1 = obj1.getVendorName();
			String name2 = obj2.getVendorName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 3:
			Double price1 = obj1.getPurchasePrice();
			Double price2 = obj2.getPurchasePrice();
			return price1.compareTo(price2);
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "date", "number", "payeename", "purchaseprice" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "date-value", "number-value", "payeename-value",
				"purchaseprice-value" };
	}

}
