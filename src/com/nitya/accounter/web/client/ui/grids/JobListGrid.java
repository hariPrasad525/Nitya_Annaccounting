package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.customers.NewJobAction;

public class JobListGrid extends BaseListGrid<ClientJob> {

	public JobListGrid() {
		super(false);
		this.getElement().setId("JobListGrid");
	}

	@Override
	public void addData(ClientJob obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	protected int[] setColTypes() {

		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientJob object) {
		deleteObject(object);
	}

	@Override
	protected Object getColumnValue(ClientJob obj, int index) {
		switch (index) {
		case 0:
			return obj.isActive();
		case 1:
			return obj.getJobName();
		case 2:
			return getCompany().getCustomer(obj.getCustomer()).getName();
		case 3:
			return obj.getJobStatus();
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onClick(ClientJob obj, int row, int col) {
		if (col == 4) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 4) {
			return 60;
		}else if (index == 1 || index == 2) {
			return 165;
		}else if (index == 3) {
			return 175;
		}
		return -1;
	}

	@Override
	protected int getColumnType(int col) {

		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_CHECK;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}

	}

	@Override
	public void onDoubleClick(ClientJob obj) {
		NewJobAction action = new NewJobAction(null);
		action.run(obj, false);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.isActive(), messages.jobName(),
				messages.payeeName(Global.get().customer()),
				messages.jobStatus(), "" };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "isActive", "jobName", "payeeName", "jobStatus",
				"delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "isActiveValue", "jobNameValue",
				"payeeNameValue", "jobStatusValue", "deleteValue" };
	}

}
