package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.Lists.ClientTDSInfo;
import com.nitya.accounter.web.client.ui.DataUtils;

public class TDSVendorsListGrid extends BaseListGrid<ClientTDSInfo> {

	private boolean isTdsView;

	public TDSVendorsListGrid(boolean isMultiSelectionEnable, boolean isTdsView) {
		super(isMultiSelectionEnable, true);
		this.isTdsView = isTdsView;
		this.getElement().setId("TDSVendorsListGrid");

	}

	@Override
	protected int[] setColTypes() {
		// if (true) {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
		// } else {
		// return new int[] { ListGrid.COLUMN_TYPE_CHECK,
		// ListGrid.COLUMN_TYPE_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_IMAGE };
		// }

	}

	@Override
	protected void executeDelete(ClientTDSInfo object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTDSInfo obj, int index) {
		switch (index) {
		case 0:
			return obj.getVendor().isActive();
		case 1:
			return obj.getVendor().getName();

		case 2:
			return obj.getDate().toString();
		case 3:
			return obj.getOrginalBalance();
		case 4:
			return obj.getPayment();
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					getCompany().getTAXItem(obj.getVendor().getTaxItemCode())
							.getTaxRate(),

					getCompany().getPrimaryCurrency())
					+ "%";
		case 6:
			return DataUtils.amountAsStringWithCurrency(obj.getTdsAmount(), getCompany()
					.getPrimaryCurrency());
		default:
			break;
		}
		return null;

	}

	@Override
	public void onDoubleClick(ClientTDSInfo obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 20;
		else if (index == 3)
			return 20;
		else
			return 50;
	}

	@Override
	protected String[] getColumns() {

		String[] colArray = new String[7];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.active();
				break;
			case 1:
				colArray[index] = messages.payeeName(
						Global.get().Vendor());
				break;

			case 2:
				colArray[index] = messages.date();
				break;
			case 3:
				colArray[index] = messages.originalAmount();
				break;
			case 4:
				colArray[index] = messages.payment();
				break;
			case 5:
				colArray[index] = messages.percentage();
				break;
			case 6:
				colArray[index] = messages.tds();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "active", "payee-name", "date",
				"original-amount", "payment", "percentage", "tds" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "active-value", "payee-name-value", "date-value",
				"original-amount-value", "payment-value", "percentage-value",
				"tds-value" };
	}

}
