package com.nitya.accounter.web.client.ui.vendors;

import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class AwaitingAuthorisationgrid extends BaseListGrid<BillsList> {
	AwaitingAuthorisationView view;

	public AwaitingAuthorisationgrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("AwaitingAuthorisationgrid");
	}

	@Override
	protected void executeDelete(BillsList object) {
		// its not using any where

	}

	public void setView(AwaitingAuthorisationView view) {
		this.view = view;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(BillsList billsList, int index) {
		switch (index) {

		case 0:
			return getstatus(billsList.getExpenseStatus());
		case 1:
			return billsList.getVendorName();
		case 2:
			return new ClientFinanceDate(billsList.getDate().getDate());
		case 3:
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
	public ValidationResult validateGrid() {
		// its not using any where
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.status(),
				messages.paidTo(),
				messages.receiptDate(),
				messages.total() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "status",
				"paidTo",
				"receiptDate",
				"total" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "statusValue",
				"paidToValue",
				"receiptDateValue",
				"totalValue", };
	}


}
