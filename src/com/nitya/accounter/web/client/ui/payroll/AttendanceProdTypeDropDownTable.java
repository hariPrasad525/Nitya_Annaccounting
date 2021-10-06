package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.PayRollActions;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class AttendanceProdTypeDropDownTable extends
		AbstractDropDownTable<ClientAttendanceOrProductionType> {

	private static List<ClientAttendanceOrProductionType> list = new ArrayList<ClientAttendanceOrProductionType>();

	public AttendanceProdTypeDropDownTable(boolean isAddNewRequired) {
		super(list, isAddNewRequired);
		initList();
	}

	private void initList() {
		Accounter
				.createPayrollService()
				.getAttendanceProductionTypes(
						0,
						-1,
						new AsyncCallback<PaginationList<ClientAttendanceOrProductionType>>() {

							@Override
							public void onSuccess(
									PaginationList<ClientAttendanceOrProductionType> result) {
								list = result;
								reInitData();
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
	}

	@Override
	public List<ClientAttendanceOrProductionType> getTotalRowsData() {
		return list;
	}

	@Override
	protected ClientAttendanceOrProductionType getAddNewRow() {
		ClientAttendanceOrProductionType employee = new ClientAttendanceOrProductionType();
		employee.setName(messages.comboDefaultAddNew(messages
				.attendanceOrProductionType()));
		return employee;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientAttendanceOrProductionType> textColumn = new TextColumn<ClientAttendanceOrProductionType>() {

			@Override
			public String getValue(ClientAttendanceOrProductionType object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientAttendanceOrProductionType t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientAttendanceOrProductionType value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		PayRollActions action = PayRollActions
				.newAttendanceProductionTypeAction();
		action.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

			@Override
			public void actionResult(ClientAttendanceOrProductionType result) {
				selectRow(result);

			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

}
