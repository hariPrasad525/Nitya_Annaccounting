package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;

public class TransactionPayTDSGrid extends
		AbstractTransactionGrid<ClientTransactionPayTAX> {

	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXTBOX };

	public TransactionPayTDSGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public TransactionPayTDSGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionPayTAX obj,
			int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getColumnType(int index) {

		return columns[index];
	}

	@Override
	protected Object getColumnValue(ClientTransactionPayTAX obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(ClientTransactionPayTAX obj, int row, int index) {
		if (index == 2)
			return true;
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxAgency(),
				messages.taxDue(), messages.amountToPay() };
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "taxAgency";
		case 1:
			return "taxDue";
		case 2:
			return "amountToPay";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "taxAgency-value";
		case 1:
			return "taxDue-value";
		case 2:
			return "amountTopay-value";
		default:
			return "";
		}
	}

}
