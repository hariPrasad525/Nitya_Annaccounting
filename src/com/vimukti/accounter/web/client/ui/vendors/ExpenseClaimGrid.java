package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ExpenseClaimGrid extends BaseListGrid<BillsList> {

	ExpenseClaimList view;

	public ExpenseClaimGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("ExpenseClaimGrid");
	}

	@Override
	protected int getColumnType(int index) {
		// if (index == 0) {
		// return ListGrid.COLUMN_TYPE_CHECK;
		// }
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	public void setView(ExpenseClaimList view) {
		this.view = view;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT, };

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ValidationResult validateGrid() {
		return new ValidationResult();
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.receiptFrom(), messages.receiptDate(),
				messages.dateEntered(), messages.status(), messages.amount() };
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void executeDelete(BillsList bills) {
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
		if (isCanOpenTransactionView(billsList.getSaveStatus(),
				billsList.getType())) {
			ReportsRPC.openTransactionView(billsList.getType(),
					billsList.getTransactionId());
		}
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "receipt-from", "receipt-date", "date-entered",
				"status", "amount" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "receipt-from-value", "receipt-date-value",
				"date-entered-value", "status-value", "amount-value" };
	}

}
