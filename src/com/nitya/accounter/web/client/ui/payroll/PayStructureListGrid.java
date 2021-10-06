package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientPayStructure;
import com.nitya.accounter.web.client.core.ClientPayStructureList;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class PayStructureListGrid extends BaseListGrid<ClientPayStructureList> {

	public PayStructureListGrid() {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "employee", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "employee-value", "delete-value" };
	}

	@Override
	protected void executeDelete(ClientPayStructureList object) {
		AccounterAsyncCallback<ClientPayStructure> callback = new AccounterAsyncCallback<ClientPayStructure>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientPayStructure result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.PAY_STRUCTURE, object.getId(), callback);
	}

	@Override
	protected Object getColumnValue(ClientPayStructureList obj, int index) {
		switch (index) {
		case 0:
			return obj.getEmployee() != null ? obj.getEmployee().getName()
					: obj.getEmployeeGroup().getName();
		case 1:
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
			return 30;
		default:
			return -1;
		}

	}

	@Override
	protected void onClick(ClientPayStructureList obj, int row, int col) {
		switch (col) {
		case 1:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientPayStructureList obj) {
		ReportsRPC.openTransactionView(IAccounterCore.PAY_STRUCTURE,
				obj.getId());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.employeeOrGroup(), messages.delete() };
	}

}
