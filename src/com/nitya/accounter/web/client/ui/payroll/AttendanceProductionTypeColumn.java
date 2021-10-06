package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.nitya.accounter.web.client.ui.edittable.ComboColumn;

public class AttendanceProductionTypeColumn
		extends
		ComboColumn<ClientAttendanceManagementItem, ClientAttendanceOrProductionType> {

	private AttendanceProdTypeDropDownTable dropdown = new AttendanceProdTypeDropDownTable(
			true);

	@Override
	protected ClientAttendanceOrProductionType getValue(
			ClientAttendanceManagementItem row) {
		return null;// row.getAttendanceType();
	}

	@Override
	protected void setValue(ClientAttendanceManagementItem row,
			ClientAttendanceOrProductionType newValue) {
	}

	@Override
	public AbstractDropDownTable<ClientAttendanceOrProductionType> getDisplayTable(
			ClientAttendanceManagementItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return messages.attendanceOrProductionType();
	}

	@Override
	public String getValueAsString(ClientAttendanceManagementItem row) {
		return getValueAsString(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
