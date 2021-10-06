package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.ui.DataUtils;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class BankStatementGrid extends BaseListGrid<ClientStatementRecord> {

	public BankStatementGrid() {
		super(false, true);
		this.getElement().setId("BankStatementGrid");
	}

	@Override
	protected int[] setColTypes() {

		return new int[] { ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.description(),
				messages.referenceNo(), messages.reconciled(),
				messages.spend(), messages.received() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 55;
		case 1:
			return 55;
		case 2:
			return 55;
		case 3:
			return 55;
		case 4:
			return 55;
		case 5:
			return 55;

		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientStatementRecord obj, int index) {
		switch (index) {
		case 0:
			return obj.getStatementDate();
		case 1:
			return obj.getDescription();
		case 2:
			return obj.getReferenceNumber();
		case 3:
			boolean reconciled = obj.isMatched();
			return reconciled == true ? messages.yes() : messages.no();
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getSpentAmount(),
					getCompany().getPrimaryCurrency());
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					obj.getReceivedAmount(), getCompany().getPrimaryCurrency());
		}
		return null;
	}

	@Override
	protected void executeDelete(ClientStatementRecord object) {

	}

	@Override
	public void onDoubleClick(ClientStatementRecord obj) {

	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "date", "description",
				"referenceNo", "reconciled",
				"spend", "received" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "dateValue", "descriptionValue",
				"referenceNoValue", "reconciledValue",
				"spendValue", "receivedValue"};
	}

}
