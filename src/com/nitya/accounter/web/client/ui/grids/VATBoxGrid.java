package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.core.ClientBox;
import com.nitya.accounter.web.client.ui.DataUtils;

public class VATBoxGrid extends ListGrid<ClientBox> {

	public VATBoxGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable, false);

	}

	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected int getColumnType(int index) {
		if (index == 1)
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		else
			return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(ClientBox obj, int index) {
		// if (index == 0) {
		// return Utility.getDescription(obj.getBoxNumber());
		// }
		if (index == 0) {
			return obj.getName();
		}
		if (index == 1) {
			return DataUtils.amountAsStringWithCurrency(obj.getAmount(), getCompany()
					.getPrimaryCurrency());
		}
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected String[] getSelectValues(ClientBox obj, int index) {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected boolean isEditable(ClientBox obj, int row, int index) {
		return false;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onClick(ClientBox obj, int row, int index) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onDoubleClick(ClientBox obj) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onValueChange(ClientBox obj, int index, Object value) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected int sort(ClientBox obj1, ClientBox obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		// if (index == 1) {
		// return 600;
		// }
		if (index == 1) {
			return 200;
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxLine(),
				messages.amount() };
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "taxLine";
		case 1:
			return "amount";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "taxLine-value";
		case 1:
			return "amount-value";
		default:
			return "";
		}
	}

}
