package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.PayRollActions;

public class EmployeeGroupCombo extends CustomCombo<ClientEmployeeGroup> {

	private long group;
	protected boolean isItemsAdded;

	public EmployeeGroupCombo(String title, boolean isEmp) {
		super(title, true, 1, "employeegroupcombo");
		getEmployeeGroups();
	}

	private void getEmployeeGroups() {
		EmployeeGroupCombo.this.isItemsAdded = false;
		Accounter.createPayrollService().getEmployeeGroups(
				new AsyncCallback<ArrayList<ClientEmployeeGroup>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<ClientEmployeeGroup> result) {
						initCombo(result);
						EmployeeGroupCombo.this.isItemsAdded = true;
						selectRow();
					}
				});
	}

	protected void selectRow() {
		if (this.group != 0) {
			for (ClientEmployeeGroup eGroup : getComboItems()) {
				if (eGroup.getID() == group) {
					setComboItem(eGroup);
					this.group = 0;
					break;
				}
			}
		}
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.employeeGroup();
	}

	@Override
	public void onAddNew() {
		PayRollActions action = PayRollActions.newEmployeeGroupAction();
		action.setCallback(new ActionCallback<ClientEmployeeGroup>() {

			@Override
			public void actionResult(ClientEmployeeGroup result) {
				addItemThenfireEvent(result);
			}
		});
		action.setFromEmployeeView(true);
		action.run(null, true);
	}

	@Override
	protected String getDisplayName(ClientEmployeeGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientEmployeeGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	public void setGroupValue(long group) {
		this.group = group;
		if (isItemsAdded) {
			selectRow();
		}
	}
}
