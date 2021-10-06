package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class AttendanceDropDownTable extends
		AbstractDropDownTable<ClientAttendanceOrProductionType> {

	private static List<ClientAttendanceOrProductionType> list = new ArrayList<ClientAttendanceOrProductionType>();

	public AttendanceDropDownTable() {
		super(list, false);
		initList();
	}

	private void initList() {
		Accounter
				.createPayrollService()
				.getAttendanceProductionTypes(
						0,
						0,
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
		return null;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientAttendanceOrProductionType> number = new TextColumn<ClientAttendanceOrProductionType>() {

			@Override
			public String getValue(ClientAttendanceOrProductionType row) {
				return row.getName();
			}
		};
		this.addColumn(number);
	}

	@Override
	protected boolean filter(ClientAttendanceOrProductionType t, String string) {
		if (t.getName() != null && t.getName().toLowerCase().startsWith(string)) {
			return true;
		}
		return false;
	}

	@Override
	protected String getDisplayValue(ClientAttendanceOrProductionType value) {
		return value.getName();
	}

	@Override
	protected void addNewItem(String text) {

	}

	@Override
	protected void addNewItem() {

	}

	public ClientAttendanceOrProductionType getAttendance(long attendanceType) {
		for (ClientAttendanceOrProductionType type : getTotalRowsData()) {
			if (type.getID() == attendanceType) {
				return type;
			}
		}
		return null;
	}

}
