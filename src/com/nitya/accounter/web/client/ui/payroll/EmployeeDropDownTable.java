package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.PayRollActions;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class EmployeeDropDownTable extends
		AbstractDropDownTable<ClientEmployee> {

	private static List<ClientEmployee> list = new ArrayList<ClientEmployee>();

	public EmployeeDropDownTable(boolean isAddNewRequired) {
		super(list, isAddNewRequired);
		initList();
	}

	public EmployeeDropDownTable(ClientPayStructureDestination employee) {
		super(list, false);
		initList(employee);
	}

	public void initList(ClientPayStructureDestination employeeGroup) {
		if (employeeGroup == null) {
			return;
		}
		list.clear();
		if (employeeGroup instanceof ClientEmployee) {
			list.add((ClientEmployee) employeeGroup);
			reInitData();
			return;
		}
		ClientEmployeeGroup eg = (ClientEmployeeGroup) employeeGroup;
		list.addAll(eg.getEmployees());
	}

	private void initList() {
		Accounter.createPayrollService().getEmployees(true, 0, 0,
				new AsyncCallback<PaginationList<ClientEmployee>>() {

					@Override
					public void onSuccess(PaginationList<ClientEmployee> result) {
						gotEmployees(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	protected void gotEmployees(PaginationList<ClientEmployee> result) {
		list = result;
		reInitData();
	}

	@Override
	public List<ClientEmployee> getTotalRowsData() {
		return list;
	}

	@Override
	protected ClientEmployee getAddNewRow() {
		ClientEmployee employee = new ClientEmployee();
		employee.setName(messages.comboDefaultAddNew(messages.employee()));
		return employee;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientEmployee> textColumn = new TextColumn<ClientEmployee>() {

			@Override
			public String getValue(ClientEmployee object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientEmployee t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientEmployee value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		PayRollActions action = PayRollActions.newEmployeeAction();
		action.setCallback(new ActionCallback<ClientEmployee>() {

			@Override
			public void actionResult(ClientEmployee result) {
				selectRow(result);

			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

	public ClientEmployee getEmployee(long employee) {
		for (ClientEmployee emp : getTotalRowsData()) {
			if (emp.getID() == employee) {
				return emp;
			}
		}
		return null;
	}

}
