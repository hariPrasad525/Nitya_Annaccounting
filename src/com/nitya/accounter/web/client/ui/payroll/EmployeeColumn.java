package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.nitya.accounter.web.client.ui.edittable.ComboColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;

public class EmployeeColumn extends
		ComboColumn<ClientAttendanceManagementItem, ClientEmployee> {

	EmployeeDropDownTable dropdown = new EmployeeDropDownTable(null) {
		protected void gotEmployees(PaginationList<ClientEmployee> result) {
			super.gotEmployees(result);
			EditTable<ClientAttendanceManagementItem> table = EmployeeColumn.this
					.getTable();
			for (ClientAttendanceManagementItem cmItem : table.getAllRows()) {
				table.update(cmItem);
			}
		};
	};

	@Override
	protected ClientEmployee getValue(ClientAttendanceManagementItem row) {
		return dropdown.getEmployee(row.getEmployee());
	}

	@Override
	protected void setValue(ClientAttendanceManagementItem row,
			ClientEmployee newValue) {
		row.setEmployee(newValue.getID());
	}

	@Override
	public AbstractDropDownTable<ClientEmployee> getDisplayTable(
			ClientAttendanceManagementItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 120;
	}

	@Override
	protected String getColumnName() {
		return messages.employee();
	}

	public void updateList(ClientPayStructureDestination group) {
		dropdown.initList(group);
	}

	@Override
	public String getValueAsString(ClientAttendanceManagementItem row) {
		return getValue(row).toString();
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
