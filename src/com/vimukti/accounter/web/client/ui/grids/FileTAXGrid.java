package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;

public class FileTAXGrid extends ListGrid<ClientTAXReturnEntry> {

	public FileTAXGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		isEnable = false;
		init();
		addEmptyMessage(messages.selectTAXAgency());
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_DATE;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		}
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(ClientTAXReturnEntry obj, int index) {
		switch (index) {
		case 0:
			ClientTAXItem taxItem = Accounter.getCompany().getTAXItem(
					obj.getTaxItem());
			return messages.taxItemWithRate(taxItem.getName(),
					taxItem.getTaxRate());
		case 1:
			return Utility.getTransactionName(obj.getTransactionType());
		case 2:
			return new ClientFinanceDate(obj.getTransactionDate());
		case 3:
			return obj.getGrassAmount();
		case 4:
			return obj.getTaxAmount();
		case 5:
			return obj.getNetAmount();
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientTAXReturnEntry obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(ClientTAXReturnEntry obj, int index,
			Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(ClientTAXReturnEntry obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientTAXReturnEntry obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientTAXReturnEntry obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientTAXReturnEntry obj1, ClientTAXReturnEntry obj2,
			int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 100;
		case 2:
			return 100;
		case 3:
			return 80;
		case 4:
			return 80;
		case 5:
			return 80;
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxItem(), messages.transactionType(),
				messages.transactionDate(), messages.grossAmount(),
				messages.taxAmount(), messages.netAmount() };
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "taxItem";
		case 1:
			return "transactionType";
		case 2:
			return "transactionDate";
		case 3:
			return "grossAmount";
		case 4:
			return "taxAmount";
		case 5:
			return "netAmount";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "taxItem-value";
		case 1:
			return "transactionType-value";
		case 2:
			return "transactionDate-value";
		case 3:
			return "grossAmount-value";
		case 4:
			return "taxAmount-value";
		case 5:
			return "netAmount-value";
		default:
			return "";
		}
	}

}
