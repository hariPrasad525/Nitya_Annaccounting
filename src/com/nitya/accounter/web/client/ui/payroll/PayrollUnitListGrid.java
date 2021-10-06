package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientPayrollUnit;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.PayRollActions;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class PayrollUnitListGrid extends BaseListGrid<ClientPayrollUnit> {

	public PayrollUnitListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientPayrollUnit object) {
		AccounterAsyncCallback<ClientPayrollUnit> callback = new AccounterAsyncCallback<ClientPayrollUnit>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientPayrollUnit result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.PAY_ROLL_UNIT, object.getID(), callback);
	}

	@Override
	protected void onClick(ClientPayrollUnit obj, int row, int col) {

		switch (col) {
		case 3:
			showWarnDialog(obj);
			break;

		default:
			break;
		}
	}

	@Override
	protected Object getColumnValue(ClientPayrollUnit obj, int col) {
		switch (col) {
		case 0:
			return obj.getSymbol();
		case 1:
			return obj.getFormalname();
		case 2:
			return obj.getNoofDecimalPlaces();

		case 3:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 220;
		case 1:
			return 220;
		case 2:
			return 220;
		case 3:
			return 30;
		default:
			return -1;
		}
	}

	@Override
	public void onDoubleClick(ClientPayrollUnit obj) {
		PayRollActions newPayRollUnitAction = PayRollActions
				.newPayRollUnitAction();
		newPayRollUnitAction.setFromEmployeeView(true);
		newPayRollUnitAction.setInput(obj);
		newPayRollUnitAction
				.setCallback(new ActionCallback<ClientPayrollUnit>() {

					@Override
					public void actionResult(ClientPayrollUnit result) {
						updateGrid();
					}
				});
		newPayRollUnitAction.run();
	}

	protected void updateGrid() {
		
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

}
