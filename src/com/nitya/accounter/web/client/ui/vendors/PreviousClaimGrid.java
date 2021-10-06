package com.nitya.accounter.web.client.ui.vendors;

import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class PreviousClaimGrid extends BaseListGrid<BillsList> {

	PreviousClaimsView view;

	public PreviousClaimGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("PreviousClaimGrid");
	}

	public void setView(PreviousClaimsView view) {
		this.view = view;
	}

	@Override
	protected int getColumnType(int col) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected void executeDelete(BillsList billsList) {
		// NOTHING TO DO.
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(BillsList billsList, int index) {
		switch (index) {
		// case 0:
		// return false;
		case 0:
			return billsList.getVendorName();
		case 1:
			return new ClientFinanceDate(billsList.getDate().getDate());
		case 2:
			return new ClientFinanceDate(billsList.getDueDate().getDate());
		case 3:
			return getstatus(billsList.getExpenseStatus());

		case 4:
			return billsList.getOriginalAmount();
		}
		return null;
	}

	private String getstatus(int status) {
		switch (status) {
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE:
			return messages.draft();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE:
			return messages.delete();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL:
			return messages.submitForApproval();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED:
			return messages.approved();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED:
			return messages.decline();
		default:
			break;
		}
		return messages.draft();
	}

	@Override
	public void onDoubleClick(BillsList billsList) {
		ReportsRPC.openTransactionView(billsList.getType(),
				billsList.getTransactionId());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.receiptFrom(),
				messages.receiptDate(),
				messages.dateEntered(),
				messages.status(), messages.amount() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "receiptfrom", "receiptdate", "dateentered",
				"status", "amount" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "receiptfrom-value", "receiptdate-value",
				"dateentered-value", "status-value", "amount-value" };
	}


}
