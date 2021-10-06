package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.PayRollActions;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class AttendanceOrProductionTypeListGrid extends
		BaseListGrid<ClientAttendanceOrProductionType> {

	public AttendanceOrProductionTypeListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name-value", "type-value", "period-value",
				"delete-value" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name", "type", "period", "delete" };
	}

	@Override
	protected void executeDelete(final ClientAttendanceOrProductionType object) {
		Accounter.deleteObject(this, object);
	}

	@Override
	protected Object getColumnValue(ClientAttendanceOrProductionType obj,
			int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return ClientAttendanceOrProductionType.getTypeName(obj.getType());
		case 2:
			if (obj.getType() == ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
				return obj.getClientPayrollUnit().getName();
			}
			return ClientPayHead.getCalculationPeriod(obj.getPeriodType());

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
	public void onDoubleClick(ClientAttendanceOrProductionType obj) {
		PayRollActions newAttendanceProductionTypeAction = PayRollActions
				.newAttendanceProductionTypeAction();
		newAttendanceProductionTypeAction.setInput(obj);
		newAttendanceProductionTypeAction
				.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

					@Override
					public void actionResult(
							ClientAttendanceOrProductionType result) {
						updateGrid();
					}
				});
		newAttendanceProductionTypeAction.run();
	}

	protected void updateGrid() {

	}

	@Override
	protected void onClick(ClientAttendanceOrProductionType obj, int row,
			int col) {
		if (col == 3) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.type(),
				messages2.periodOrUnit(), messages.delete() };
	}
}
