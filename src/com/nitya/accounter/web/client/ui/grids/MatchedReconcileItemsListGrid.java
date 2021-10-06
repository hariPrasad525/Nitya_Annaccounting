package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.core.Lists.TransactionsList;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.customers.ReconcileItemsListDialog;

/**
 * this is used to display all the transactions based on spent or withdrawl
 * amount
 */
public class MatchedReconcileItemsListGrid extends
		BaseListGrid<TransactionsList> {
	private ReconcileItemsListDialog dialog;
	private boolean isSpent;

	public MatchedReconcileItemsListGrid(int transactionType) {
		super(false, transactionType);
		this.getElement().setId("MatchedReconcileItemsListGrid");
	}

	public MatchedReconcileItemsListGrid(ReconcileItemsListDialog dialog,
			boolean isSpent) {
		super(false);
		this.isSpent = isSpent;
		this.dialog = dialog;
		this.getElement().setId("MatchedReconcileItemsListGrid");
	}

	@Override
	protected int[] setColTypes() {

		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT };

	}

	@Override
	protected Object getColumnValue(TransactionsList obj, int col) {

		switch (col) {
		case 0:
			return false;
		case 1:
			return Utility.getTransactionName((obj.getType()));
		case 2:
			return UIUtils.getDateByCompanyType(obj.getDate());
		case 3:
			return obj.getCustomerName();
		case 4:
			if (isSpent) {
				return obj.getSpentAmount() > 0 ? DataUtils
						.amountAsStringWithCurrency(obj.getSpentAmount(),
								obj.getCurrency()) : " ";
			} else {
				return obj.getReceivedAmount() > 0 ? DataUtils
						.amountAsStringWithCurrency(obj.getReceivedAmount(),
								obj.getCurrency()) : " ";
			}

		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		String amountHeading;
		if (isSpent) {
			amountHeading = messages.spent();
		} else {
			amountHeading = messages.received();
		}
		return new String[] { " ", messages.type(), messages.date(),
				Global.get().messages().payeeName(Global.get().Customer()),
				amountHeading };
	}

	@Override
	public void onDoubleClick(TransactionsList obj) {
	}

	protected void onClick(TransactionsList obj, int row, int col) {
		if (!Utility.isUserHavePermissions(obj.getType())) {
			return;
		}
		if (col == 0) {
			boolean isSelected = ((CheckBox) this.getWidget(row, col))
					.getValue();
			obj.setSelected(isSelected);
			dialog.checkSelectedRecords();
		}
	}

	protected void voidTransaction(final InvoicesList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final InvoicesList obj) {
	}

	@Override
	protected int getCellWidth(int index) {

		switch (index) {
		case 0:
			return 15;
			// case 1:
			// return 60;
			// case 2:
			// return 60;
			// case 3:
			// return 60;
			// case 4:
			// return 60;
		default:
			return -1;
		}
	}

	@Override
	protected void executeDelete(TransactionsList object) {
		// NOTHING TO DO.
	}

	public AccounterCoreType getAccounterCoreType(InvoicesList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void editComplete(TransactionsList item, Object value, int col) {
		// TODO Auto-generated method stub
		super.editComplete(item, value, col);
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "col-1-value", "type-value", "date-value",
				"payeeName-value", "amountHeading-value" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "col-1", "type", "date", "payeeName",
		"amountHeading" };
}
}
