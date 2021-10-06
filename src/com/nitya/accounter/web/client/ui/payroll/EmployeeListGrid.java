package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class EmployeeListGrid extends BaseListGrid<ClientEmployee> {

	public EmployeeListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientEmployee emp) {
		Accounter.deleteObject(this, emp);
	}

	@Override
	protected Object getColumnValue(ClientEmployee emp, int col) {
		switch (col) {
		case 0:
			return emp.getNumber();
		case 1:
			return emp.getName();
		case 2:
			return emp.getDesignation();
		case 3:
			return emp.getLocation();
		case 4:
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
			return 120;
		case 2:
			return 170;
		case 3:
			return 170;
		case 4:
			return 50;
		default:
			return -1;
		}

	}

	@Override
	protected void onClick(ClientEmployee obj, int row, int col) {
		if (col == 4) {
			showWarnDialog(obj);
		}
	}

	@Override
	public void onDoubleClick(ClientEmployee emp) {
		ReportsRPC.openTransactionView(IAccounterCore.EMPLOYEE, emp.getID());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.employeeID(), messages.employeeName(),
				messages.designation(), messages.location(), messages.delete() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "employeeID", "employeeName", "designation",
				"location", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "employeeID-value", "employeeName-value",
				"designation-value", "location-value", "delete-value" };
	}
}
